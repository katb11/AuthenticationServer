import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Hash {

    private static final String salt = System.getenv("SALT");

    public static String getHash(String value) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            byte[] saltArray = Base64.getDecoder().decode(salt);
            md.update(saltArray);

            byte[] byteArray = md.digest(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(byteArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* used for generating the salt saved as the
     environmental variable SALT for the system
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return Base64.getEncoder().encodeToString(salt);
    }
}