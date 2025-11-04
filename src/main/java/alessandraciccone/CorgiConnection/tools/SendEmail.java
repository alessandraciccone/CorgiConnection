package alessandraciccone.CorgiConnection.tools;

import alessandraciccone.CorgiConnection.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class SendEmail {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Registrazione completata");
        message.setText("Benvenuto su Corgi Connection-Dove i Corgi si incontrano!\uD83D\uDC3E ");
        mailSender.send(message);
        System.out.println("email inviata");
    }
}
