package HaruIt.domain.auth.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void setMailSender(String toAddress, String subject,String body){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("s26016@gsm.hs.kr");

        mailSender.send(message);
    }
}
