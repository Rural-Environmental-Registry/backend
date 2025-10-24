package br.car.registration.util;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PropertyHashGeneratorAdvancedTest {

    private PropertyHashGenerator hashGenerator;

    @BeforeEach
    void setUp() {
        hashGenerator = new PropertyHashGenerator();
        ReflectionTestUtils.setField(hashGenerator, "HASH_PREFIX", "TEST");
    }

    @Test
    void generateHashPart_ShouldReturnConsistentLength() throws Exception {
        // Given
        Method method = PropertyHashGenerator.class.getDeclaredMethod("generateHashPart", String.class, Instant.class);
        method.setAccessible(true);
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        // When
        String result = (String) method.invoke(hashGenerator, id, createdAt);

        // Then
        assertEquals(32, result.length());
        assertTrue(result.matches("[0-9a-f]+"));
    }

    @Test
    void generatePropertyHash_WithEmptyPrefix_ShouldHandleGracefully() {
        // Given
        ReflectionTestUtils.setField(hashGenerator, "HASH_PREFIX", "");
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        // When
        String result = hashGenerator.generatePropertyHash(id, createdAt);

        // Then
        assertNotNull(result);
        assertTrue(result.startsWith("-"));
    }

    @Test
    void generatePropertyHash_WithNullPrefix_ShouldHandleGracefully() {
        // Given
        ReflectionTestUtils.setField(hashGenerator, "HASH_PREFIX", null);
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        // When & Then
        assertDoesNotThrow(() -> hashGenerator.generatePropertyHash(id, createdAt));
    }

    @Test
    void generatePropertyHash_WithVeryOldDate_ShouldWork() {
        // Given
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.parse("1970-01-01T00:00:00Z");

        // When
        String result = hashGenerator.generatePropertyHash(id, createdAt);

        // Then
        assertNotNull(result);
        assertTrue(result.contains("TEST"));
    }

    @Test
    void generatePropertyHash_WithFutureDate_ShouldWork() {
        // Given
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.parse("2050-12-31T23:59:59Z");

        // When
        String result = hashGenerator.generatePropertyHash(id, createdAt);

        // Then
        assertNotNull(result);
        assertTrue(result.contains("TEST"));
    }

    @Test
    void generatePropertyHash_MultipleCallsInSequence_ShouldGenerateUniqueHashes() {
        // Given
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        // When
        String hash1 = hashGenerator.generatePropertyHash(id, createdAt);
        String hash2 = hashGenerator.generatePropertyHash(id, createdAt);
        String hash3 = hashGenerator.generatePropertyHash(id, createdAt);

        // Then
        assertNotEquals(hash1, hash2);
        assertNotEquals(hash2, hash3);
        assertNotEquals(hash1, hash3);
    }

    @Test
    void generatePropertyHash_ShouldContainCorrectNumberOfDots() {
        // Given
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        // When
        String result = hashGenerator.generatePropertyHash(id, createdAt);

        // Then
        long dotCount = result.chars().filter(ch -> ch == '.').count();
        assertEquals(7, dotCount);
    }

    @Test
    void generatePropertyHash_ShouldHaveCorrectStructure() {
        // Given
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        // When
        String result = hashGenerator.generatePropertyHash(id, createdAt);

        // Then
        String[] parts = result.split("-");
        assertEquals(3, parts.length);
        assertEquals("TEST", parts[0]);
        assertEquals(7, parts[1].length()); // hex minutes
        
        String[] hashParts = parts[2].split("\\.");
        assertEquals(8, hashParts.length);
        for (String part : hashParts) {
            assertEquals(4, part.length());
        }
    }
}