import java.security.MessageDigest;
import java.security.SecureRandom; // these libraries are for secure passwords.
import java.util.Base64;          // passwords cannot be stored as plain text.

public class AccountData {
    private String username;
    private String passwordHash;  // Store hashed password instead
    private byte[] salt;          // Salt for the hash
    private double balance;
    public static int TotalUsers = 0;

    public AccountData(String username, String password, double balance) throws Exception {
        this.username = username;
        this.salt = generateSalt();
        this.passwordHash = hashPassword(password, salt);
        this.balance = balance;
        TotalUsers++;
    }

    private byte[] generateSalt() throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private String hashPassword(String password, byte[] salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    public boolean verifyPassword(String password) throws Exception {
        String hashedPassword = hashPassword(password, this.salt);
        return hashedPassword.equals(this.passwordHash);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setPassword(String newPassword) throws Exception {
        this.salt = generateSalt();
        this.passwordHash = hashPassword(newPassword, this.salt);
    }
}
