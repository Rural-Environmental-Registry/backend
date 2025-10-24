package br.car.registration.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import br.car.registration.domain.User;
import br.car.registration.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.car.registration.api.v1.request.PropertyFilter;
import br.car.registration.domain.Person;
import br.car.registration.domain.Property;
import br.car.registration.domain.SubArea;
import br.car.registration.domain.attributes.PersonAttribute;
import br.car.registration.repository.PersonRepository;
import br.car.registration.repository.PropertyRepository;
import br.car.registration.repository.PropertySpecification;
import br.car.registration.util.PropertyHashGenerator;
import br.car.registration.util.ReceiptGenerator;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PersonRepository personRepository;
    private final PropertyHashGenerator hashGenerator;
    private final ReceiptGenerator receiptGenerator;
    private final UserRepository userRepository;

    public PropertyService(PropertyRepository propertyRepository, PersonRepository personRepository,
            PropertyHashGenerator hashGenerator, ReceiptGenerator receiptGenerator, UserRepository userRepository) {
        this.propertyRepository = propertyRepository;
        this.personRepository = personRepository;
        this.hashGenerator = hashGenerator;
        this.receiptGenerator = receiptGenerator;
        this.userRepository = userRepository;
    }

    @Transactional
    public Property addProperty(Property property) {
        addOwnerships(property);
        addRegistrarships(property);
        addRepresentativeships(property);
        addSubAreas(property);
        addDocuments(property);

        property.getAttributes().forEach(attribute -> attribute.setProperty(property));

        String hash_code = hashGenerator.generatePropertyHash(property.getId().toString(), property.getCreatedAt());
        property.setCode(hash_code);

        return propertyRepository.save(property);

    }

    @Transactional
    public Property updateProperty(Property property) {
        addOwnerships(property);
        addRegistrarships(property);
        addRepresentativeships(property);
        addSubAreas(property);
        addDocuments(property);

        property.getAttributes().forEach(attribute -> attribute.setProperty(property));

        return propertyRepository.save(property);

    }

    private void addDocuments(Property property) {
        property.getDocuments().forEach(document -> {
            document.setProperty(property);
            document.getAttributes().forEach(attribute -> attribute.setPropertyDocument(document));
        });
    }

    private void addSubAreas(Property property) {
        List<SubArea> subAreas = property.getSubAreas();

        if (subAreas != null && !subAreas.isEmpty()) {
            subAreas.forEach(subArea -> {
                subArea.setProperty(property);
                if (subArea.getAttributes() != null) {
                    subArea.getAttributes().forEach(attribute -> attribute.setSubArea(subArea));
                }
            });
        }
    }

    private void addRepresentativeships(Property property) {
        property.getRepresentativeships().forEach(representativeship -> {
            representativeship.setRepresentative(addPerson(representativeship.getRepresentative()));
            representativeship.setProperty(property);
        });
    }

    private void addRegistrarships(Property property) {
        property.getRegistrarships().forEach(registrarship -> {
            registrarship.setRegistrar(addPerson(registrarship.getRegistrar()));
            registrarship.setProperty(property);
        });
    }

    private void addOwnerships(Property property) {
        property.getOwnerships().forEach(ownership -> {
            ownership.setOwner(addPerson(ownership.getOwner()));
            ownership.setProperty(property);
        });
    }

    // private Person addPerson(Person person) {
    // Person toUpdate;
    // Optional<Person> existing =
    // personRepository.findFirstByIdentifier(person.getIdentifier());
    // if (existing.isPresent()) {
    // toUpdate = existing.get();
    // Map<String, PersonAttribute> existingAttrMap = new HashMap<>();
    // try {
    // existingAttrMap = toUpdate.getAttributes().stream()
    // .collect(Collectors.toMap(PersonAttribute::getName, Function.identity(), (a,
    // b) -> a));
    // } catch (Exception e) {
    // // TODO: handle exception
    // }

    // if (person.getAttributes() != null) {
    // for (PersonAttribute newAttr : person.getAttributes()) {
    // PersonAttribute managedAttr = existingAttrMap.get(newAttr.getName());
    // if (managedAttr != null) {
    // managedAttr.setValue(newAttr.getValue());
    // } else {
    // newAttr.setPerson(toUpdate);
    // toUpdate.getAttributes().add(newAttr);
    // }
    // }
    // }
    // } else {
    // toUpdate = person;
    // }
    // if (toUpdate.getAttributes() != null) {
    // toUpdate.getAttributes().forEach(attribute -> attribute.setPerson(toUpdate));
    // }
    // return personRepository.save(toUpdate);
    // }

    public Person addPerson(Person person) {
        return personRepository.findFirstByIdentifier(person.getIdentifier())
                .map(existing -> updatePerson(existing, person))
                .orElseGet(() -> prepareNewPerson(person));
    }

    private Person updatePerson(Person existing, Person incoming) {
        Map<String, PersonAttribute> existingAttrMap = (existing.getAttributes() != null
                && !existing.getAttributes().isEmpty())
                        ? existing.getAttributes().stream()
                                .collect(Collectors.toMap(PersonAttribute::getName, Function.identity(), (a, b) -> a))
                        : new HashMap<>();

        if (incoming.getAttributes() != null && !incoming.getAttributes().isEmpty()) {
            for (PersonAttribute newAttr : incoming.getAttributes()) {
                PersonAttribute existingAttr = existingAttrMap.get(newAttr.getName());
                if (existingAttr != null) {
                    existingAttr.setValue(newAttr.getValue());
                } else {
                    newAttr.setPerson(existing);
                    existing.getAttributes().add(newAttr);
                }
            }
        }

        existing.setIdentifier(incoming.getIdentifier());
        existing.setName(incoming.getName());
        existing.setDateOfBirth(incoming.getDateOfBirth());
        existing.setMothersName(incoming.getMothersName());

        if (existing.getAttributes() != null && !existing.getAttributes().isEmpty()) {
            existing.getAttributes().forEach(attr -> attr.setPerson(existing));
        }
        return personRepository.save(existing);
    }

    private Person prepareNewPerson(Person person) {
        if (person.getAttributes() != null) {
            person.getAttributes().forEach(attr -> attr.setPerson(person));
        }
        return personRepository.save(person);
    }

    public Optional<Property> getProperty(UUID propertyId) {
        return propertyRepository.findById(propertyId);
    }

    public List<Property> getAllProperties() {
        return StreamSupport.stream(propertyRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public byte[] generatePropertyReceipt(UUID id, String locationZone) {
        Property property = this.getProperty(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not founded"));
        return receiptGenerator.createPdf(property, locationZone);
    }

    public Page<Property> getFilteredProperties(PropertyFilter filter, Pageable pageable) {
        User user = userRepository.findByIdKeycloak(filter.getSub()).orElseThrow(EntityNotFoundException::new);
        return propertyRepository.findAll(PropertySpecification.withFilters(filter,user.getIdNational()), pageable);
    }
}
