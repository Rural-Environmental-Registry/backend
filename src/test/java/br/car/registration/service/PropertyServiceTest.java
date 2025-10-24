package br.car.registration.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import br.car.registration.api.v1.request.PropertyFilter;
import br.car.registration.domain.Ownership;
import br.car.registration.domain.Person;
import br.car.registration.domain.Property;
import br.car.registration.domain.PropertyDocument;
import br.car.registration.domain.Registrarship;
import br.car.registration.domain.Representativeship;
import br.car.registration.domain.SubArea;
import br.car.registration.domain.User;
import br.car.registration.domain.attributes.PropertyAttribute;
import br.car.registration.repository.PersonRepository;
import br.car.registration.repository.PropertyRepository;
import org.springframework.data.jpa.domain.Specification;
import br.car.registration.repository.UserRepository;
import br.car.registration.util.PropertyHashGenerator;
import br.car.registration.util.ReceiptGenerator;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;
    
    @Mock
    private PersonRepository personRepository;
    
    @Mock
    private PropertyHashGenerator hashGenerator;
    
    @Mock
    private ReceiptGenerator receiptGenerator;
    
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PropertyService propertyService;

    private Property property;
    private UUID propertyId;
    private Person person;
    private User user;

    @BeforeEach
    void setUp() {
        propertyId = UUID.randomUUID();
        property = new Property();
        property.setId(propertyId);
        property.setCreatedAt(Instant.now());
        property.setOwnerships(Arrays.asList());
        property.setRegistrarships(Arrays.asList());
        property.setRepresentativeships(Arrays.asList());
        property.setSubAreas(Arrays.asList());
        property.setDocuments(Arrays.asList());
        property.setAttributes(Arrays.asList());
        
        person = new Person();
        person.setId(UUID.randomUUID());
        person.setIdentifier("123456789");
        person.setName("Test Person");
        person.setAttributes(Arrays.asList());
        
        user = new User();
        user.setId(UUID.randomUUID());
        user.setIdKeycloak("keycloak-123");
        user.setIdNational("987654321");
    }

    @Test
    void getProperty_WhenPropertyExists_ShouldReturnProperty() {
        // Given
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));

        // When
        Optional<Property> result = propertyService.getProperty(propertyId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(property, result.get());
        verify(propertyRepository).findById(propertyId);
    }

    @Test
    void getProperty_WhenPropertyNotExists_ShouldReturnEmpty() {
        // Given
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.empty());

        // When
        Optional<Property> result = propertyService.getProperty(propertyId);

        // Then
        assertFalse(result.isPresent());
        verify(propertyRepository).findById(propertyId);
    }

    @Test
    void addProperty_ShouldGenerateCodeAndSave() {
        // Given
        when(hashGenerator.generatePropertyHash(any(), any())).thenReturn("HASH123");
        when(propertyRepository.save(property)).thenReturn(property);

        // When
        Property result = propertyService.addProperty(property);

        // Then
        assertEquals(property, result);
        verify(hashGenerator).generatePropertyHash(propertyId.toString(), property.getCreatedAt());
        verify(propertyRepository).save(property);
        assertEquals("HASH123", property.getCode());
    }

    @Test
    void updateProperty_ShouldSaveProperty() {
        // Given
        when(propertyRepository.save(property)).thenReturn(property);

        // When
        Property result = propertyService.updateProperty(property);

        // Then
        assertEquals(property, result);
        verify(propertyRepository).save(property);
    }

    @Test
    void addPerson_WhenPersonNotExists_ShouldCreateNew() {
        // Given
        when(personRepository.findFirstByIdentifier("123456789")).thenReturn(Optional.empty());
        when(personRepository.save(person)).thenReturn(person);

        // When
        Person result = propertyService.addPerson(person);

        // Then
        assertEquals(person, result);
        verify(personRepository).findFirstByIdentifier("123456789");
        verify(personRepository).save(person);
    }

    @Test
    void addPerson_WhenPersonExists_ShouldUpdateExisting() {
        // Given
        Person existing = new Person();
        existing.setId(UUID.randomUUID());
        existing.setIdentifier("123456789");
        existing.setAttributes(Arrays.asList());
        
        when(personRepository.findFirstByIdentifier("123456789")).thenReturn(Optional.of(existing));
        when(personRepository.save(existing)).thenReturn(existing);

        // When
        Person result = propertyService.addPerson(person);

        // Then
        assertEquals(existing, result);
        verify(personRepository).findFirstByIdentifier("123456789");
        verify(personRepository).save(existing);
    }

    @Test
    void getAllProperties_ShouldReturnList() {
        // Given
        List<Property> properties = Arrays.asList(property);
        when(propertyRepository.findAll()).thenReturn(properties);

        // When
        List<Property> result = propertyService.getAllProperties();

        // Then
        assertEquals(1, result.size());
        assertEquals(property, result.get(0));
        verify(propertyRepository).findAll();
    }

    @Test
    void generatePropertyReceipt_WhenPropertyExists_ShouldReturnPdf() {
        // Given
        byte[] pdfBytes = "PDF_CONTENT".getBytes();
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));
        when(receiptGenerator.createPdf(property, "ZONE1")).thenReturn(pdfBytes);

        // When
        byte[] result = propertyService.generatePropertyReceipt(propertyId, "ZONE1");

        // Then
        assertArrayEquals(pdfBytes, result);
        verify(propertyRepository).findById(propertyId);
        verify(receiptGenerator).createPdf(property, "ZONE1");
    }

    @Test
    void generatePropertyReceipt_WhenPropertyNotExists_ShouldThrowException() {
        // Given
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseStatusException.class, 
            () -> propertyService.generatePropertyReceipt(propertyId, "ZONE1"));
        verify(propertyRepository).findById(propertyId);
    }

    @Test
    void getFilteredProperties_ShouldReturnPagedResults() {
        // Given
        PropertyFilter filter = new PropertyFilter();
        filter.setSub("keycloak-123");
        
        Pageable pageable = mock(Pageable.class);
        Page<Property> page = new PageImpl<>(Arrays.asList(property));
        
        when(userRepository.findByIdKeycloak("keycloak-123")).thenReturn(Optional.of(user));
        when(propertyRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        // When
        Page<Property> result = propertyService.getFilteredProperties(filter, pageable);

        // Then
        assertEquals(1, result.getContent().size());
        assertEquals(property, result.getContent().get(0));
        verify(userRepository).findByIdKeycloak("keycloak-123");
        verify(propertyRepository).findAll(any(Specification.class), eq(pageable));
    }
}