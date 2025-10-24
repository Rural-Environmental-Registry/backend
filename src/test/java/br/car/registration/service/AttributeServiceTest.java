package br.car.registration.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

import br.car.registration.domain.attributes.AttributeDefinition;
import br.car.registration.domain.attributes.AttributeSet;
import br.car.registration.enums.AttributeTypesEnum;
import br.car.registration.enums.EntityTypesEnum;
import br.car.registration.repository.AttributeDefinitionRepository;
import br.car.registration.repository.AttributeSetRepository;

@ExtendWith(MockitoExtension.class)
class AttributeServiceTest {

    @Mock
    private AttributeDefinitionRepository attributeDefinitionRepository;
    
    @Mock
    private AttributeSetRepository attributeSetRepository;

    @InjectMocks
    private AttributeService attributeService;

    private AttributeDefinition attributeDefinition;
    private AttributeSet attributeSet;

    @BeforeEach
    void setUp() {
        attributeDefinition = new AttributeDefinition();
        attributeDefinition.setAttributeDefinitionId(UUID.randomUUID());
        attributeDefinition.setName("test-attr");
        attributeDefinition.setType(AttributeTypesEnum.STRING);
        
        attributeSet = new AttributeSet();
        attributeSet.setAttributeSetId(UUID.randomUUID());
        attributeSet.setEntityType(EntityTypesEnum.PROPERTY);
        attributeSet.setAttributes(Arrays.asList(attributeDefinition));
    }

    @Test
    void saveAttributeDefinition_ShouldReturnSavedDefinition() {
        // Given
        when(attributeDefinitionRepository.save(attributeDefinition)).thenReturn(attributeDefinition);

        // When
        AttributeDefinition result = attributeService.saveAttributeDefinition(attributeDefinition);

        // Then
        assertEquals(attributeDefinition, result);
        verify(attributeDefinitionRepository).save(attributeDefinition);
    }

    @Test
    void getAttributeDefinition_WhenExists_ShouldReturnDefinition() {
        // Given
        when(attributeDefinitionRepository.findById("def-1")).thenReturn(Optional.of(attributeDefinition));

        // When
        AttributeDefinition result = attributeService.getAttributeDefinition("def-1");

        // Then
        assertEquals(attributeDefinition, result);
        verify(attributeDefinitionRepository).findById("def-1");
    }

    @Test
    void getAttributeDefinition_WhenNotExists_ShouldReturnNull() {
        // Given
        when(attributeDefinitionRepository.findById("def-1")).thenReturn(Optional.empty());

        // When
        AttributeDefinition result = attributeService.getAttributeDefinition("def-1");

        // Then
        assertNull(result);
        verify(attributeDefinitionRepository).findById("def-1");
    }

    @Test
    void getAttributeSet_ShouldReturnOptional() {
        // Given
        when(attributeSetRepository.findById("set-1")).thenReturn(Optional.of(attributeSet));

        // When
        Optional<AttributeSet> result = attributeService.getAttributeSet("set-1");

        // Then
        assertTrue(result.isPresent());
        assertEquals(attributeSet, result.get());
        verify(attributeSetRepository).findById("set-1");
    }

    @Test
    void getAttributeDefinitions_ShouldReturnList() {
        // Given
        List<AttributeDefinition> definitions = Arrays.asList(attributeDefinition);
        when(attributeDefinitionRepository.findAll()).thenReturn(definitions);

        // When
        List<AttributeDefinition> result = attributeService.getAttributeDefinitions();

        // Then
        assertEquals(1, result.size());
        assertEquals(attributeDefinition, result.get(0));
        verify(attributeDefinitionRepository).findAll();
    }

    @Test
    void createAttributeDefinition_ShouldReturnCreatedDefinition() {
        // Given
        when(attributeDefinitionRepository.save(attributeDefinition)).thenReturn(attributeDefinition);

        // When
        AttributeDefinition result = attributeService.createAttributeDefinition(attributeDefinition);

        // Then
        assertEquals(attributeDefinition, result);
        verify(attributeDefinitionRepository).save(attributeDefinition);
    }

    @Test
    void getAttributeSets_ShouldReturnList() {
        // Given
        List<AttributeSet> sets = Arrays.asList(attributeSet);
        when(attributeSetRepository.findAll()).thenReturn(sets);

        // When
        List<AttributeSet> result = attributeService.getAttributeSets();

        // Then
        assertEquals(1, result.size());
        assertEquals(attributeSet, result.get(0));
        verify(attributeSetRepository).findAll();
    }

    @Test
    void createAttributeSet_WhenNotExists_ShouldCreateNew() {
        // Given
        when(attributeSetRepository.findByEntityType(EntityTypesEnum.PROPERTY)).thenReturn(Optional.empty());
        when(attributeDefinitionRepository.findByTypeAndName(any(), any())).thenReturn(Optional.of(attributeDefinition));
        when(attributeSetRepository.save(attributeSet)).thenReturn(attributeSet);

        // When
        AttributeSet result = attributeService.createAttributeSet(attributeSet);

        // Then
        assertEquals(attributeSet, result);
        verify(attributeSetRepository).findByEntityType(EntityTypesEnum.PROPERTY);
        verify(attributeSetRepository).save(attributeSet);
    }

    @Test
    void createAttributeSet_WhenExists_ShouldUpdateExisting() {
        // Given
        AttributeSet existing = new AttributeSet();
        existing.setAttributeSetId(UUID.randomUUID());
        existing.setEntityType(EntityTypesEnum.PROPERTY);
        
        when(attributeSetRepository.findByEntityType(EntityTypesEnum.PROPERTY)).thenReturn(Optional.of(existing));
        when(attributeDefinitionRepository.findByTypeAndName(any(), any())).thenReturn(Optional.of(attributeDefinition));
        when(attributeSetRepository.save(existing)).thenReturn(existing);

        // When
        AttributeSet result = attributeService.createAttributeSet(attributeSet);

        // Then
        assertEquals(existing, result);
        verify(attributeSetRepository).findByEntityType(EntityTypesEnum.PROPERTY);
        verify(attributeSetRepository).save(existing);
    }

    @Test
    void createAttributeSet_WhenDefinitionNotExists_ShouldCreateNewDefinition() {
        // Given
        when(attributeSetRepository.findByEntityType(EntityTypesEnum.PROPERTY)).thenReturn(Optional.empty());
        when(attributeDefinitionRepository.findByTypeAndName(any(), any())).thenReturn(Optional.empty());
        when(attributeDefinitionRepository.save(any(AttributeDefinition.class))).thenReturn(attributeDefinition);
        when(attributeSetRepository.save(attributeSet)).thenReturn(attributeSet);

        // When
        AttributeSet result = attributeService.createAttributeSet(attributeSet);

        // Then
        assertEquals(attributeSet, result);
        verify(attributeDefinitionRepository).save(any(AttributeDefinition.class));
        verify(attributeSetRepository).save(attributeSet);
    }

    @Test
    void findAttributeSetByEntityType_ShouldReturnOptional() {
        // Given
        when(attributeSetRepository.findByEntityType(EntityTypesEnum.PROPERTY)).thenReturn(Optional.of(attributeSet));

        // When
        Optional<AttributeSet> result = attributeService.findAttributeSetByEntityType(EntityTypesEnum.PROPERTY);

        // Then
        assertTrue(result.isPresent());
        assertEquals(attributeSet, result.get());
        verify(attributeSetRepository).findByEntityType(EntityTypesEnum.PROPERTY);
    }
}