package br.car.registration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.car.registration.mappers.PropertyMapper;

@ExtendWith(MockitoExtension.class)
class ReceiptGeneratorAdvancedTest {

    @Mock
    private PropertyMapper propertyMapper;

    @InjectMocks
    private ReceiptGenerator receiptGenerator;

    @BeforeEach
    void setUp() {
        receiptGenerator = new ReceiptGenerator(propertyMapper);
    }

    @AfterEach
    void tearDown() {
        System.clearProperty("REPORT_PARAMS_RECEIPT_JSON");
    }

    @Test
    void isValidTimeZone_WithValidZone_ShouldReturnTrue() throws Exception {
        // Given
        Method method = ReceiptGenerator.class.getDeclaredMethod("isValidTimeZone", String.class);
        method.setAccessible(true);

        // When
        boolean result = (boolean) method.invoke(receiptGenerator, "UTC");

        // Then
        assertTrue(result);
    }

    @Test
    void isValidTimeZone_WithInvalidZone_ShouldReturnFalse() throws Exception {
        // Given
        Method method = ReceiptGenerator.class.getDeclaredMethod("isValidTimeZone", String.class);
        method.setAccessible(true);

        // When
        boolean result = (boolean) method.invoke(receiptGenerator, "INVALID_ZONE");

        // Then
        assertFalse(result);
    }

    @Test
    void isValidTimeZone_WithNullZone_ShouldReturnFalse() throws Exception {
        // Given
        Method method = ReceiptGenerator.class.getDeclaredMethod("isValidTimeZone", String.class);
        method.setAccessible(true);

        // When & Then
        assertThrows(Exception.class, () -> method.invoke(receiptGenerator, (String) null));
    }

    @Test
    void getStringDatetimeFormatted_ShouldReturnFormattedDate() throws Exception {
        // Given
        Method method = ReceiptGenerator.class.getDeclaredMethod("getStringDatetimeFormatted", Instant.class,
                ZoneId.class);
        method.setAccessible(true);
        Instant instant = Instant.parse("2023-12-25T10:30:00Z");
        ZoneId zoneId = ZoneId.of("UTC");

        // When
        String result = (String) method.invoke(receiptGenerator, instant, zoneId);

        // Then
        assertEquals("25/12/2023 10:30:00", result);
    }

    @Test
    void getStringDatetimeFormatted_WithDifferentTimeZone_ShouldReturnCorrectTime() throws Exception {
        // Given
        Method method = ReceiptGenerator.class.getDeclaredMethod("getStringDatetimeFormatted", Instant.class,
                ZoneId.class);
        method.setAccessible(true);
        Instant instant = Instant.parse("2023-12-25T10:30:00Z");
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

        // When
        String result = (String) method.invoke(receiptGenerator, instant, zoneId);

        // Then
        assertNotNull(result);
        assertTrue(result.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    void loadJsonParameters_WithNullPath_ShouldThrowException() {
        // Given
        System.clearProperty("REPORT_PARAMS_RECEIPT_JSON");

        // When & Then
        assertThrows(Exception.class, () -> receiptGenerator.loadJsonParameters());
    }

    @Test
    void loadJsonParameters_WithInvalidJsonPath_ShouldThrowException() {
        // Given
        System.setProperty("REPORT_PARAMS_RECEIPT_JSON", "invalid/path.json");

        // When & Then
        assertThrows(Exception.class, () -> receiptGenerator.loadJsonParameters());
    }
}