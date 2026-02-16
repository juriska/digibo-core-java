package com.digibo.core.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class RsaKeyProviderTest {

    private RsaKeyProvider rsaKeyProvider;

    @BeforeEach
    void setUp() {
        rsaKeyProvider = new RsaKeyProvider();
        rsaKeyProvider.init();
    }

    @Test
    void getPublicKeyBase64_returnsValidBase64() {
        String publicKeyBase64 = rsaKeyProvider.getPublicKeyBase64();

        assertNotNull(publicKeyBase64);
        assertFalse(publicKeyBase64.isEmpty());
        assertDoesNotThrow(() -> Base64.getDecoder().decode(publicKeyBase64));
    }

    @Test
    void decrypt_roundTrip_returnsOriginalText() throws Exception {
        String originalPassword = "mySecretPassword123!";

        // Simulate frontend: encrypt with public key using RSA-OAEP + SHA-256
        PublicKey publicKey = rsaKeyProvider.getPublicKey();
        OAEPParameterSpec oaepParams = new OAEPParameterSpec(
                "SHA-256", "MGF1", MGF1ParameterSpec.SHA256, OAEPParameterSpec.DEFAULT.getPSource());
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, oaepParams);
        byte[] encrypted = cipher.doFinal(originalPassword.getBytes());
        String encryptedBase64 = Base64.getEncoder().encodeToString(encrypted);

        // Decrypt with provider
        String decrypted = rsaKeyProvider.decrypt(encryptedBase64);

        assertEquals(originalPassword, decrypted);
    }

    @Test
    void decrypt_withUnicodePassword_returnsOriginalText() throws Exception {
        String originalPassword = "p@$$w0rd_\u00e9\u00e8\u00ea";

        PublicKey publicKey = rsaKeyProvider.getPublicKey();
        OAEPParameterSpec oaepParams = new OAEPParameterSpec(
                "SHA-256", "MGF1", MGF1ParameterSpec.SHA256, OAEPParameterSpec.DEFAULT.getPSource());
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, oaepParams);
        byte[] encrypted = cipher.doFinal(originalPassword.getBytes());
        String encryptedBase64 = Base64.getEncoder().encodeToString(encrypted);

        String decrypted = rsaKeyProvider.decrypt(encryptedBase64);

        assertEquals(originalPassword, decrypted);
    }

    @Test
    void decrypt_withInvalidBase64_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> rsaKeyProvider.decrypt("not-valid-base64!!!"));
    }

    @Test
    void decrypt_withCorruptedCiphertext_throwsIllegalArgumentException() {
        // Valid Base64 but not valid RSA ciphertext
        String corruptedData = Base64.getEncoder().encodeToString("corrupted data".getBytes());

        assertThrows(IllegalArgumentException.class,
                () -> rsaKeyProvider.decrypt(corruptedData));
    }
}
