package br.car.registration.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import br.car.registration.api.v1.request.PropertyFilter;
import br.car.registration.api.v1.request.PropertyReq;
import br.car.registration.api.v1.response.PagedRes;
import br.car.registration.api.v1.response.PropertyRes;
import br.car.registration.domain.Property;
import br.car.registration.mappers.PropertyMapper;
import br.car.registration.service.PropertyService;

@ExtendWith(MockitoExtension.class)
class PropertyControllerTest {

    @Mock
    private PropertyMapper propertyMapper;

    @Mock
    private PropertyService propertyService;

    @Mock
    private MultipartFile mapImage;

    private PropertyController propertyController;

    @BeforeEach
    void setUp() {
        propertyController = new PropertyController(propertyMapper, propertyService);
    }

    @Test
    void getProperty_WhenPropertyExists_ShouldReturnPropertyRes() {
        // Given
        UUID id = UUID.randomUUID();
        Property property = new Property();
        PropertyRes expectedResponse = new PropertyRes();
        
        when(propertyService.getProperty(id)).thenReturn(Optional.of(property));
        when(propertyMapper.toResponse(property)).thenReturn(expectedResponse);

        // When
        PropertyRes result = propertyController.getProperty(id);

        // Then
        assertEquals(expectedResponse, result);
    }

    @Test
    void getProperty_WhenPropertyNotExists_ShouldThrowException() {
        // Given
        UUID id = UUID.randomUUID();
        when(propertyService.getProperty(id)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> propertyController.getProperty(id));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void addProperty_WhenValidRequest_ShouldReturnPropertyRes() {
        // Given
        PropertyReq request = new PropertyReq();
        Property entity = new Property();
        Property savedEntity = new Property();
        PropertyRes expectedResponse = new PropertyRes();
        
        when(propertyMapper.toEntity(request)).thenReturn(entity);
        when(propertyService.addProperty(entity)).thenReturn(savedEntity);
        when(propertyMapper.toResponse(savedEntity)).thenReturn(expectedResponse);

        // When
        PropertyRes result = propertyController.addProperty(request, null);

        // Then
        assertEquals(expectedResponse, result);
    }

    @Test
    void addProperty_WhenWithMapImage_ShouldSetImageAndReturnPropertyRes() {
        // Given
        PropertyReq request = new PropertyReq();
        Property entity = new Property();
        Property savedEntity = new Property();
        PropertyRes expectedResponse = new PropertyRes();
        
        when(propertyMapper.toEntity(request)).thenReturn(entity);
        when(propertyService.addProperty(entity)).thenReturn(savedEntity);
        when(propertyMapper.toResponse(savedEntity)).thenReturn(expectedResponse);

        // When
        PropertyRes result = propertyController.addProperty(request, mapImage);

        // Then
        assertEquals(expectedResponse, result);
        assertEquals(mapImage, request.getMapImage());
    }

    @Test
    void updateProperty_WhenValidRequest_ShouldReturnPropertyRes() {
        // Given
        UUID id = UUID.randomUUID();
        PropertyReq request = new PropertyReq();
        Property entity = new Property();
        Property updatedEntity = new Property();
        PropertyRes expectedResponse = new PropertyRes();
        
        when(propertyMapper.toEntity(request)).thenReturn(entity);
        when(propertyService.updateProperty(entity)).thenReturn(updatedEntity);
        when(propertyMapper.toResponse(updatedEntity)).thenReturn(expectedResponse);

        // When
        PropertyRes result = propertyController.updateProperty(request, null, id);

        // Then
        assertEquals(expectedResponse, result);
        assertEquals(id, entity.getId());
    }

    @Test
    void getPropertyImage_WhenPropertyHasImage_ShouldReturnImage() {
        // Given
        UUID id = UUID.randomUUID();
        Property property = new Property();
        byte[] imageData = "test image".getBytes();
        property.setMapImage(imageData);
        
        when(propertyService.getProperty(id)).thenReturn(Optional.of(property));

        // When
        ResponseEntity<byte[]> result = propertyController.getPropertyImage(id);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(imageData, result.getBody());
    }

    @Test
    void getPropertyImage_WhenPropertyNotFound_ShouldThrowException() {
        // Given
        UUID id = UUID.randomUUID();
        when(propertyService.getProperty(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseStatusException.class,
                () -> propertyController.getPropertyImage(id));
    }

    @Test
    void getReceipt_WhenValidRequest_ShouldReturnPdf() {
        // Given
        UUID id = UUID.randomUUID();
        String locationZone = "UTC";
        byte[] receiptData = "test pdf".getBytes();
        
        when(propertyService.generatePropertyReceipt(id, locationZone)).thenReturn(receiptData);

        // When
        ResponseEntity<byte[]> result = propertyController.getReceipt(id, locationZone);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(receiptData, result.getBody());
    }

    @Test
    void getProperties_WhenValidFilter_ShouldReturnPagedResponse() {
        // Given
        PropertyFilter filter = new PropertyFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Property property = new Property();
        PropertyRes propertyRes = new PropertyRes();
        Page<Property> page = new PageImpl<>(List.of(property), pageable, 1);
        
        when(propertyService.getFilteredProperties(filter, pageable)).thenReturn(page);
        when(propertyMapper.toResponse(property)).thenReturn(propertyRes);

        // When
        PagedRes<PropertyRes> result = propertyController.getProperties(filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getProperties().size());
        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());
        assertEquals(1, result.getTotalElements());
    }
}