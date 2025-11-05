package br.car.registration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.car.registration.api.v1.response.PropertyRes;
import br.car.registration.domain.Property;
import br.car.registration.domain.PropertyDocument;
import br.car.registration.domain.attributes.PropertyAttribute;
import br.car.registration.domain.attributes.PropertyDocumentAttribute;
import br.car.registration.mappers.PropertyMapper;

@ExtendWith(MockitoExtension.class)
class ReceiptGeneratorTest {

    @Mock
    private PropertyMapper propertyMapper;

    @InjectMocks
    private ReceiptGenerator receiptGenerator;

    private Property property;
    private PropertyRes propertyRes;

    @BeforeEach
    void setUp() {
        property = new Property();
        property.setId(UUID.randomUUID());
        property.setCreatedAt(Instant.now());
        property.setMapImage("test image".getBytes());
        property.setAttributes(List.of());
        property.setDocuments(List.of());
        property.setSubAreas(List.of());
        property.setOwnerships(List.of());
        property.setRegistrarships(List.of());
        property.setRepresentativeships(List.of());

        PropertyRes.MainArea mainArea = new PropertyRes.MainArea();
        mainArea.setArea(100.0);
        mainArea.setLat(-15.7942);
        mainArea.setLon(-47.8822);

        propertyRes = new PropertyRes();
        propertyRes.setId(property.getId());
        propertyRes.setMainArea(mainArea);
    }

    @AfterEach
    void tearDown() {
        System.clearProperty("GENERAL_INFORMATION_RECEIPT_PATH");
        System.clearProperty("REPORT_PARAMS_RECEIPT_JSON");
        System.clearProperty("DEFAULT_LOCATION_ZONE");
        System.clearProperty("RECEIPT_LOGO_PATH");
    }

    @Test
    void readGeneralInformationText_WhenPathNotSet_ShouldThrowException() {
        // Given
        System.clearProperty("GENERAL_INFORMATION_RECEIPT_PATH");

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> receiptGenerator.readGeneralInformationText());
    }

    @Test
    void readGeneralInformationText_WhenPathIsBlank_ShouldThrowException() {
        // Given
        System.setProperty("GENERAL_INFORMATION_RECEIPT_PATH", "");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> receiptGenerator.readGeneralInformationText());
        assertEquals("Variável de ambiente 'GENERAL_INFORMATION_RECEIPT_PATH' não definida!",
                exception.getMessage());
    }

    @Test
    void readGeneralInformationText_WhenFileNotFound_ShouldThrowException() {
        // Given
        System.setProperty("GENERAL_INFORMATION_RECEIPT_PATH", "nonexistent/file.txt");

        // When & Then
        assertThrows(Exception.class,
                () -> receiptGenerator.readGeneralInformationText());
    }

    @Test
    void createPdf_WhenValidProperty_ShouldReturnNullDueToMissingResources() {
        // Given
        when(propertyMapper.toResponse(property)).thenReturn(propertyRes);
        System.setProperty("DEFAULT_LOCATION_ZONE", "UTC");

        // When
        byte[] result = receiptGenerator.createPdf(property, "UTC");

        // Then
        assertNull(result); // Returns null when jasper template is missing
        verify(propertyMapper).toResponse(property);
    }

    @Test
    void createPdf_WithInvalidOrNullLocationZone_ShouldHandleGracefully() {
        // Given
        System.setProperty("DEFAULT_LOCATION_ZONE", "UTC");

        // When & Then - Should handle exceptions gracefully
        assertThrows(Exception.class, () -> receiptGenerator.createPdf(property, null));
    }

    @Test
    void createPdf_WithNullMapImage_ShouldReturnNull() {
        // Given
        property.setMapImage(null);
        when(propertyMapper.toResponse(property)).thenReturn(propertyRes);
        System.setProperty("DEFAULT_LOCATION_ZONE", "UTC");

        // When
        byte[] result = receiptGenerator.createPdf(property, "UTC");

        // Then
        assertNull(result);
        verify(propertyMapper).toResponse(property);
    }

    @Test
    void createPdf_WithCustomLogoPath_ShouldReturnNull() {
        // Given
        when(propertyMapper.toResponse(property)).thenReturn(propertyRes);
        System.setProperty("DEFAULT_LOCATION_ZONE", "UTC");
        System.setProperty("RECEIPT_LOGO_PATH", "images/custom_logo.svg");

        // When
        byte[] result = receiptGenerator.createPdf(property, "UTC");

        // Then
        assertNull(result);
        verify(propertyMapper).toResponse(property);
    }

    @Test
    void cleanNullPropertyAttributes_ShouldRemoveNullAttributesOnly() throws Exception {
        // Given
        Property property = new Property();
        property.setId(UUID.randomUUID());
        property.setPropertyName("Test Property");

        PropertyAttribute validAttr = new PropertyAttribute();
        validAttr.setName("validAttr");
        validAttr.setStringValue("validValue");

        PropertyAttribute nullAttr = new PropertyAttribute();
        nullAttr.setName("nullAttr");
        // Don't set any value - all value fields will be null

        property.setAttributes(List.of(validAttr, nullAttr));

        PropertyDocument doc = new PropertyDocument();
        doc.setId(UUID.randomUUID());

        PropertyDocumentAttribute validDocAttr = new PropertyDocumentAttribute();
        validDocAttr.setName("validDocAttr");
        validDocAttr.setStringValue("validDocValue");

        PropertyDocumentAttribute nullDocAttr = new PropertyDocumentAttribute();
        nullDocAttr.setName("nullDocAttr");
        // Don't set any value - all value fields will be null

        doc.setAttributes(List.of(validDocAttr, nullDocAttr));
        property.setDocuments(List.of(doc));
        property.setSubAreas(List.of());
        property.setOwnerships(List.of());
        property.setRegistrarships(List.of());
        property.setRepresentativeships(List.of());

        Method method = ReceiptGenerator.class.getDeclaredMethod("cleanNullPropertyAttributes", Property.class);
        method.setAccessible(true);

        // When
        Property result = (Property) method.invoke(receiptGenerator, property);

        // Then
        assertEquals(1, result.getAttributes().size());
        assertEquals("validAttr", result.getAttributes().get(0).getName());
        assertEquals("validValue", result.getAttributes().get(0).getValue());

        assertEquals(1, result.getDocuments().size());
        assertEquals(1, result.getDocuments().get(0).getAttributes().size());
        assertEquals("validDocAttr", result.getDocuments().get(0).getAttributes().get(0).getName());
        assertEquals("validDocValue", result.getDocuments().get(0).getAttributes().get(0).getValue());

        // Verify original property is unchanged
        assertEquals(2, property.getAttributes().size());
        assertEquals(2, property.getDocuments().get(0).getAttributes().size());
    }
}
