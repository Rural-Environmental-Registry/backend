package br.car.registration.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PropertyHashGeneratorTest {

    private PropertyHashGenerator hashGenerator;

    @BeforeEach
    void setUp() {
        hashGenerator = new PropertyHashGenerator();
        ReflectionTestUtils.setField(hashGenerator, "HASH_PREFIX", "RER");
    }

    @Test
    void generatePropertyHash_ShouldReturnValidFormat() {
        // Given
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        // When
        String hash = hashGenerator.generatePropertyHash(id, createdAt);

        // Then
        assertNotNull(hash);
        assertTrue(hash.startsWith("RER-"));
        assertTrue(hash.matches(
                "RER-[0-9A-F]{7}-[0-9A-F]{4}\\.[0-9A-F]{4}\\.[0-9A-F]{4}\\.[0-9A-F]{4}\\.[0-9A-F]{4}\\.[0-9A-F]{4}\\.[0-9A-F]{4}\\.[0-9A-F]{4}"));
    }

    @Test
    void generatePropertyHash_WithSameInputs_ShouldGenerateDifferentHashes() {
        // Given
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        // When
        String hash1 = hashGenerator.generatePropertyHash(id, createdAt);
        String hash2 = hashGenerator.generatePropertyHash(id, createdAt);

        // Then
        assertNotEquals(hash1, hash2);
    }

    @Test
    void generatePropertyHash_WithDifferentInputs_ShouldGenerateDifferentHashes() {
        // Given
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        // When
        String hash1 = hashGenerator.generatePropertyHash(id1, createdAt);
        String hash2 = hashGenerator.generatePropertyHash(id2, createdAt);

        // Then
        assertNotEquals(hash1, hash2);
    }

    @Test
    void generatePropertyHash_ShouldContainCorrectPrefix() {
        // Given
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        // When
        String hash = hashGenerator.generatePropertyHash(id, createdAt);

        // Then
        assertTrue(hash.startsWith("RER-"));
    }
}