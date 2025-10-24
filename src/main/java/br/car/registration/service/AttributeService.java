package br.car.registration.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import br.car.registration.domain.attributes.AttributeDefinition;
import br.car.registration.domain.attributes.AttributeSet;
import br.car.registration.enums.EntityTypesEnum;
import br.car.registration.repository.AttributeDefinitionRepository;
import br.car.registration.repository.AttributeSetRepository;

@Service
public class AttributeService {

    private final AttributeDefinitionRepository attributeDefinitionRepository;
    private final AttributeSetRepository attributeSetRepository;

    public AttributeService(AttributeDefinitionRepository attributeDefinitionRepository,
            AttributeSetRepository attributeSetRepository) {
        this.attributeDefinitionRepository = attributeDefinitionRepository;
        this.attributeSetRepository = attributeSetRepository;
    }

    public AttributeDefinition saveAttributeDefinition(AttributeDefinition definition) {
        return attributeDefinitionRepository.save(definition);
    }

    public AttributeDefinition getAttributeDefinition(String id) {
        return attributeDefinitionRepository.findById(id).orElse(null);
    }

    public Optional<AttributeSet> getAttributeSet(String id) {
        return attributeSetRepository.findById(id);
    }

    public List<AttributeDefinition> getAttributeDefinitions() {
        return StreamSupport.stream(attributeDefinitionRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public AttributeDefinition createAttributeDefinition(AttributeDefinition attributeDefinition) {
        return attributeDefinitionRepository.save(attributeDefinition);
    }

    public List<AttributeSet> getAttributeSets() {
        return StreamSupport.stream(attributeSetRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public AttributeSet createAttributeSet(AttributeSet attributeSet) {
        AttributeSet toUpdate = attributeSet;
        Optional<AttributeSet> existing = attributeSetRepository.findByEntityType(attributeSet.getEntityType());
        if (existing.isPresent()) {
            toUpdate = existing.get();
        }

        List<AttributeDefinition> attributes = attributeSet.getAttributes().stream().map(this::findOrCreateDefinition)
                .collect(Collectors.toList());

        toUpdate.setAttributes(attributes);

        return attributeSetRepository.save(toUpdate);
    }

    /**
     * This method checks if an AttributeDefinition with the same (type, name)
     * already exists. If so, reuse it; otherwise create a new one.
     */
    private AttributeDefinition findOrCreateDefinition(AttributeDefinition definition) {
        return attributeDefinitionRepository.findByTypeAndName(definition.getType(), definition.getName())
                .orElseGet(() -> {
                    AttributeDefinition newDef = new AttributeDefinition();
                    newDef.setType(definition.getType());
                    newDef.setName(definition.getName());
                    newDef.setAllowedValues(definition.getAllowedValues());
                    return attributeDefinitionRepository.save(newDef);
                });
    }

    public Optional<AttributeSet> findAttributeSetByEntityType(EntityTypesEnum entityType) {
        return attributeSetRepository.findByEntityType(entityType);
    }

}
