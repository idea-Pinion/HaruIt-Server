package HaruIt.domain.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordChangeRequest(
        @NotBlank String currentPassword,
        @NotBlank
        @Size(min = 8, max = 64)
        @Pattern(regexp = "^[\\x21-\\x7E]+$", message = "영문, 숫자, 특수문자만 사용할 수 있습니다.")
        String newPassword
) {}