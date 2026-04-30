package com.project.aes.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AesService {

    /* ---------------- KEY GENERATION ---------------- */

    public SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // AES-128
        return keyGen.generateKey();
    }

    public String generateKeyString() throws Exception {
        SecretKey key = generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /* ---------------- PASSWORD STRENGTH VALIDATION ---------------- */

    private void validatePasswordStrength(String keyStr) {

        if (keyStr == null || keyStr.trim().isEmpty()) {
            throw new IllegalArgumentException("AES key cannot be empty.");
        }

        if (keyStr.length() < 16) {
            throw new IllegalArgumentException(
                "Invalid key. Key must be at least 16 characters long."
            );
        }

        boolean hasUpper = keyStr.matches(".*[A-Z].*");
        boolean hasLower = keyStr.matches(".*[a-z].*");
        boolean hasDigit = keyStr.matches(".*\\d.*");
        boolean hasSymbol = keyStr.matches(".*[^a-zA-Z0-9].*");

        if (!(hasUpper && hasLower && hasDigit && hasSymbol)) {
            throw new IllegalArgumentException(
                "Invalid key. Key must include uppercase, lowercase, number, and symbol."
            );
        }
    }

    /* ---------------- KEY VALIDATION ---------------- */

    private SecretKey getValidAesKey(String keyStr) {

        validatePasswordStrength(keyStr);

        byte[] keyBytes;

        // Try Base64 decode first (for generated keys)
        try {
            keyBytes = Base64.getDecoder().decode(keyStr.trim());
        } catch (IllegalArgumentException e) {
            // If not Base64, treat as raw string
            keyBytes = keyStr.trim().getBytes(StandardCharsets.UTF_8);
        }

        int len = keyBytes.length;

        if (len != 16 && len != 24 && len != 32) {
            throw new IllegalArgumentException(
                "Invalid AES key length. Key must be exactly 16, 24, or 32 bytes."
            );
        }

        return new SecretKeySpec(keyBytes, "AES");
    }

    /* ---------------- ENCRYPTION ---------------- */

    public byte[] encrypt(byte[] plainData, String keyStr) throws Exception {

        SecretKey key = getValidAesKey(keyStr);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = new byte[cipher.getBlockSize()];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] cipherText = cipher.doFinal(plainData);

        // Prepend IV to ciphertext
        byte[] output = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, output, 0, iv.length);
        System.arraycopy(cipherText, 0, output, iv.length, cipherText.length);

        return output;
    }

    /* ---------------- DECRYPTION ---------------- */

    public byte[] decrypt(byte[] ivAndCipherText, String keyStr) throws Exception {

        SecretKey key = getValidAesKey(keyStr);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        int blockSize = cipher.getBlockSize();

        byte[] iv = new byte[blockSize];
        byte[] cipherText = new byte[ivAndCipherText.length - blockSize];

        System.arraycopy(ivAndCipherText, 0, iv, 0, blockSize);
        System.arraycopy(ivAndCipherText, blockSize, cipherText, 0, cipherText.length);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        return cipher.doFinal(cipherText);
    }
}