package br.car.registration.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.car.registration.domain.Property;
import br.car.registration.mappers.PropertyMapper;
import br.car.registration.service.PropertyService;

@ExtendWith(MockitoExtension.class)
class PropertyControllerAdvancedTest {

    @Mock
    private PropertyMapper propertyMapper;

    @Mock
    private PropertyService propertyService;

    private PropertyController propertyController;

    @BeforeEach
    void setUp() {
        propertyController = new PropertyController(propertyMapper, propertyService);
    }

    @Test
    void getPropertyImage_WhenPropertyHasNullImage_ShouldThrowException() {
        // Given
        UUID id = UUID.randomUUID();
        Property property = new Property();
        property.setMapImage(null);
        
        when(propertyService.getProperty(id)).thenReturn(Optional.of(property));

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> propertyController.getPropertyImage(id));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Imagem não encontrada", exception.getReason());
    }

    @Test
    void getPropertyImage_WhenPropertyHasEmptyImage_ShouldThrowException() {
        // Given
        UUID id = UUID.randomUUID();
        Property property = new Property();
        property.setMapImage(new byte[0]);
        
        when(propertyService.getProperty(id)).thenReturn(Optional.of(property));

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> propertyController.getPropertyImage(id));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Imagem não encontrada", exception.getReason());
    }

    @Test
    void getPropertyImage_WhenPropertyNotFound_ShouldThrowException() {
        // Given
        UUID id = UUID.randomUUID();
        when(propertyService.getProperty(id)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> propertyController.getPropertyImage(id));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Propriedade não encontrada", exception.getReason());
    }
}