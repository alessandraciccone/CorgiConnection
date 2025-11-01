package alessandraciccone.CorgiConnection.generetor;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretGenerator {
    public static String generateSecret() {
        byte[] key = new byte[64]; // 512 bit
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public static void main(String[] args) {
        System.out.println(generateSecret());
    }
}
