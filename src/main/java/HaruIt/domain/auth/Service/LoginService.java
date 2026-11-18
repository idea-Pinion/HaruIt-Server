package HaruIt.domain.auth.Service;

import HaruIt.domain.auth.Repository.AuthUserRepository;
import HaruIt.domain.auth.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder){
        this.authUserRepository = authUserRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public boolean login(String email,String password){
        Optional<User> userOptional= authUserRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            return false;
        }
        User user=userOptional.get();

        return passwordEncoder.matches(password, user.getPassword());
    }
    public void logout(){
        System.out.println("로그아웃 처리가 완료되었습니다.");
    }
}
