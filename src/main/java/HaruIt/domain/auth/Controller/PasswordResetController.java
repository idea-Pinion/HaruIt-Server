
package HaruIt.domain.auth.Controller;

import HaruIt.domain.auth.Service.PasswordResetService;
import HaruIt.domain.auth.dto.PasswordResetRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    // 비밀번호 재설정
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(
            @RequestBody PasswordResetRequestDto dto) {

        passwordResetService.resetPassword(dto);

        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }
}
