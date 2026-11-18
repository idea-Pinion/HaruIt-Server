package HaruIt.domain.auth.Service;

import HaruIt.domain.auth.Repository.AuthUserRepository;
import HaruIt.domain.auth.dto.SignupRequestDto;
import HaruIt.domain.auth.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SignupService {

    private final AuthUserRepository authUserRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    private Map<String,String> authCodeMap=new HashMap<>();
    // 비밀번호 재설정 인증번호
    private Map<String, String> resetCodeMap = new HashMap<>();
    // 인증 완료 여부
    private Map<String, Long> resetVerifiedMap = new HashMap<>();


    public SignupService(AuthUserRepository authUserRepository, JavaMailSender mailSender, PasswordEncoder passwordEncoder){
        this.authUserRepository = authUserRepository;
        this.mailSender=mailSender;
        this.passwordEncoder=passwordEncoder;
    }
    // 중복 체크 공통 메서드
    public boolean isEmailDuplicate(String email){
        return authUserRepository.findByEmail(email).isPresent();
    }
    //인증 번호 전송 메서드
    public void sendAuthCode(String email){
        if (authUserRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String code = String.valueOf((int)(Math.random()*900)+1000);
        authCodeMap.put(email,code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이음(EUM) 이메일 인증번호");
        message.setText("인증번호는 [" + code + "] 입니다.");

        mailSender.send(message);
    }
    //인증번호 검증 메서드
    public boolean verifyCode(String email,String code){
        String savedCode=authCodeMap.get(email);
        if(savedCode!=null && savedCode.equals(code)){
            return true;
        }
        return false;
    }
    // 비밀번호 재설정 인증번호 전송
    public void sendResetAuthCode(String email) {

        if (!authUserRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("가입되지 않은 이메일입니다.");
        }

        String code = String.valueOf(
                (int)(Math.random() * 9000) + 1000
        );

        resetCodeMap.put(email, code);
        resetVerifiedMap.remove(email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이음(EUM) 비밀번호 재설정 인증번호");
        message.setText("인증번호는 [" + code + "] 입니다.");

        mailSender.send(message);
    }

    // 비밀번호 재설정 인증번호 확인
    public boolean verifyResetAuthCode(String email, String code) {

        String savedCode = resetCodeMap.get(email);

        if (savedCode != null && savedCode.equals(code)) {
            resetCodeMap.remove(email);
            resetVerifiedMap.put(email, System.currentTimeMillis());
            return true;
        }

        return false;
    }

    @Transactional
    public void register(SignupRequestDto dto){
        if(!dto.getPassword().equals(dto.getPassWordCheck())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String encodedPassword=passwordEncoder.encode(dto.getPassword());
        User user = new User(dto.getEmail(),encodedPassword,dto.getName());
        authUserRepository.save(user);
        authCodeMap.remove(dto.getEmail());}
    // 비밀번호 재설정 인증 여부 확인
    public boolean isResetVerified(String email) {

        Long verifiedTime = resetVerifiedMap.get(email);

        if (verifiedTime == null) {
            return false;
        }

        // 인증 후 10분이 지났는지 확인
        if (System.currentTimeMillis() - verifiedTime > 600000) {
            resetVerifiedMap.remove(email);
            return false;
        }

        return true;
    }

    // 비밀번호 재설정 인증 완료 정보 삭제
    public void clearResetVerification(String email) {
        resetVerifiedMap.remove(email);
    }// 비밀번호 재설정 인증번호 전송
    public void sendResetCode(String email) {
        sendResetAuthCode(email);
    }

    // 비밀번호 재설정 인증번호 확인
    public boolean verifyResetCode(String email, String code) {
        return verifyResetAuthCode(email, code);
    }}

