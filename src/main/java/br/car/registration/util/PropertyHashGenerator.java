package br.car.registration.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyHashGenerator {

  @Value("${HASH_PREFIX}")
  private String HASH_PREFIX;

  private final String HASH_ALGORITHM = "SHA-256";
  private final Random random = new Random();

  public String generatePropertyHash(String id, Instant createdAt) {
    try {
      String hashPart = generateHashPart(id, createdAt);

      long epochMinutes = Instant.now().getEpochSecond() / 60;
      String hexMinutes = String.format("%07x", epochMinutes);

      return String.format("%s-%s-%s.%s.%s.%s.%s.%s.%s.%s",
          HASH_PREFIX,
          hexMinutes,
          hashPart.substring(0, 4),
          hashPart.substring(4, 8),
          hashPart.substring(8, 12),
          hashPart.substring(12, 16),
          hashPart.substring(16, 20),
          hashPart.substring(20, 24),
          hashPart.substring(24, 28),
          hashPart.substring(28, 32)).toUpperCase();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Failed to generate property hash", e);
    }
  }

  private String generateHashPart(String id, Instant createdAt) throws NoSuchAlgorithmException {
    String input = id + createdAt.toString() + random.nextDouble();

    MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
    byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

    String hexHash = HexFormat.of().formatHex(hashBytes);
    return hexHash.substring(0, 32);
  }

}
