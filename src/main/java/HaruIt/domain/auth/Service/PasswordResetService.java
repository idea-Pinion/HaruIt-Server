
package HaruIt.domain.auth.Service;

import HaruIt.domain.auth.Repository.AuthUserRepository;
import HaruIt.domain.auth.dto.PasswordResetRequestDto;
import HaruIt.domain.auth.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignupService signupService;

    // 비밀번호 재설정
    @Transactional
    public void resetPassword(PasswordResetRequestDto dto) {

        String email = dto.getEmail();
        String newPassword = dto.getNewPassword();

        // 1. 이메일 인증 여부 확인
        if (!signupService.isResetVerified(email)) {
            throw new IllegalArgumentException(
                    "이메일 인증이 필요하거나 인증 시간이 만료되었습니다."
            );
        }

        // 2. 회원 조회
        User user = authUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("회원을 찾을 수 없습니다.")
                );

        // 3. 새 비밀번호 암호화
        String encodedPassword =
                passwordEncoder.encode(newPassword);

        // 4. 비밀번호 변경
        user.updatePassword(encodedPassword);

        // 5. 인증 완료 정보 삭제
        signupService.clearResetVerification(email);
    }
}
