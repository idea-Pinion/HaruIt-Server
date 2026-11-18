package HaruIt.domain.auth.Controller;

import HaruIt.domain.auth.Service.LoginService;
import HaruIt.domain.auth.Service.SignupService;
import HaruIt.domain.auth.dto.LoginRequestDto;
import HaruIt.domain.auth.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignupService signupService;
    private final LoginService loginService;

    //1. 이메일 인증번호 전송
    // 1. 이메일 인증번호 전송
    @PostMapping("/email/send-code")
    public ResponseEntity<String> sendEmailCode(
            @RequestParam("email") String email,
            @RequestParam(defaultValue = "signup") String purpose) {

        if (purpose.equals("reset")) {
            signupService.sendResetAuthCode(email);
        } else {
            signupService.sendAuthCode(email);
        }

        return ResponseEntity.ok("인증번호가 발송되었습니다.");
    }
    // 2. 이메일 인증번호 확인
    @PostMapping("/email/verify-code")
    public ResponseEntity<String> verifyEmailCode(
            @RequestParam("email") String email,
            @RequestParam("code") String code,
            @RequestParam(defaultValue = "signup") String purpose) {

        boolean isVerified;

        if (purpose.equals("reset")) {
            isVerified = signupService.verifyResetAuthCode(email, code);
        } else {
            isVerified = signupService.verifyCode(email, code);
        }

        if (isVerified) {
            return ResponseEntity.ok("인증이 완료되었습니다.");
        }

        return ResponseEntity.badRequest()
                .body("인증번호가 일치하지 않습니다.");
    }
    //3. 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto){
        signupService.register(signupRequestDto);

        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }

    // 5. 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto){

        boolean loginSuccess= loginService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());

        if(loginSuccess) {
            return ResponseEntity.ok("로그인에 성공했습니다.");
        } else {
            return ResponseEntity.ok("이메일 또는 비밀번호가 틀렸습니다.");
        }
    }
    //6. 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("로그아웃되었습니다.");
    }
}
