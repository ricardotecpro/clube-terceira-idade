import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PasswordEncoderGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordToHash = "password"; // A senha que você quer hashear
        String hashedPassword = encoder.encode(passwordToHash);

        // Gerar nome do arquivo com timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "hashed_password_" + timestamp + ".txt";

        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            out.println("Senha original: " + passwordToHash);
            out.println("Hash BCrypt: " + hashedPassword);
            System.out.println("Hash da senha salvo em: " + fileName);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }
}