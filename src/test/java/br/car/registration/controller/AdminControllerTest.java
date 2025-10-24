package br.car.registration.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import br.car.registration.api.v1.request.AttributeDefinitionReq;
import br.car.registration.api.v1.request.AttributeSetReq;
import br.car.registration.api.v1.response.AttributeDefinitionRes;
import br.car.registration.api.v1.response.AttributeSetRes;
import br.car.registration.domain.attributes.AttributeDefinition;
import br.car.registration.domain.attributes.AttributeSet;
import br.car.registration.enums.AttributeTypesEnum;
import br.car.registration.mappers.AttributeDefinitionMapper;
import br.car.registration.mappers.AttributeSetMapper;
import br.car.registration.service.AttributeService;
import br.car.registration.util.ReceiptGenerator;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AttributeService attributeService;

    @Mock
    private AttributeDefinitionMapper attributeDefinitionMapper;

    @Mock
    private AttributeSetMapper attributeSetMapper;

    @Mock
    private ReceiptGenerator receiptGenerator;

    private AdminController adminController;

    @BeforeEach
    void setUp() {
        adminController = new AdminController(attributeService, attributeDefinitionMapper, 
                attributeSetMapper, receiptGenerator);
        
        ReflectionTestUtils.setField(adminController, "applicationName", "test-app");
        ReflectionTestUtils.setField(adminController, "contextPath", "/test");
        ReflectionTestUtils.setField(adminController, "frontendUrls", "http://localhost:3000");
    }

    @Test
    void getAttributeSets_WhenCalled_ShouldReturnList() {
        // Given
        List<AttributeSet> attributeSets = new ArrayList<>();
        
        when(attributeService.getAttributeSets()).thenReturn(attributeSets);

        // When
        List<AttributeSetRes> result = adminController.getAttributeSets();

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void createAttributeSet_WhenValidRequest_ShouldReturnResponse() {
        // Given
        AttributeSetReq request = new AttributeSetReq();
        AttributeSet entity = new AttributeSet();
        AttributeSetRes expectedResponse = new AttributeSetRes();
        
        when(attributeSetMapper.toEntity(request)).thenReturn(entity);
        when(attributeService.createAttributeSet(entity)).thenReturn(entity);
        when(attributeSetMapper.toResponse(entity)).thenReturn(expectedResponse);

        // When
        AttributeSetRes result = adminController.createAttributeSet(request);

        // Then
        assertEquals(expectedResponse, result);
    }

    @Test
    void getAttributeTypes_WhenCalled_ShouldReturnAllTypes() {
        // When
        List<AttributeTypesEnum> result = adminController.getAttributeTypes();

        // Then
        assertNotNull(result);
        assertEquals(AttributeTypesEnum.values().length, result.size());
    }

    @Test
    void getAttributeDefinitions_WhenCalled_ShouldReturnList() {
        // Given
        List<AttributeDefinition> definitions = new ArrayList<>();
        
        when(attributeService.getAttributeDefinitions()).thenReturn(definitions);

        // When
        List<AttributeDefinitionRes> result = adminController.getAttributeDefinitions();

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void createAttributeDefinition_WhenValidRequest_ShouldReturnResponse() {
        // Given
        AttributeDefinitionReq request = new AttributeDefinitionReq();
        AttributeDefinition entity = new AttributeDefinition();
        AttributeDefinitionRes expectedResponse = new AttributeDefinitionRes();
        
        when(attributeDefinitionMapper.toEntity(request)).thenReturn(entity);
        when(attributeService.createAttributeDefinition(entity)).thenReturn(entity);
        when(attributeDefinitionMapper.toResponse(entity)).thenReturn(expectedResponse);

        // When
        AttributeDefinitionRes result = adminController.createAttributeDefinition(request);

        // Then
        assertEquals(expectedResponse, result);
    }

    @Test
    void getAppInfo_WhenCalled_ShouldReturnInfo() throws IOException {
        // Given
        when(receiptGenerator.readGeneralInformationText()).thenReturn("test info");
        when(receiptGenerator.loadJsonParameters()).thenReturn(Map.of("test", "value"));

        // When
        Map<String, Object> result = adminController.getAppInfo();

        // Then
        assertNotNull(result);
        assertEquals("test-app", result.get("application_name"));
        assertEquals("/test", result.get("context_path"));
        assertNotNull(result.get("default_attributes"));
    }

    @Test
    void getDefaultAttributes_WhenCalled_ShouldReturnDefaultAttributes() {
        // When
        Map<String, Object> result = adminController.getDefaultAttributes();

        // Then
        assertNotNull(result);
        assertNotNull(result.get("fields.person"));
        assertNotNull(result.get("fields.property"));
        assertNotNull(result.get("customAttributes.person.owner"));
    }
}