package HaruIt.domain.users.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProfileUpdateRequest(
        @Size(min = 2, max = 20)
        @Pattern(regexp = "\\S.*\\S|\\S", message = "닉네임 앞뒤에 공백을 넣을 수 없습니다.")
        String nickname
) {}