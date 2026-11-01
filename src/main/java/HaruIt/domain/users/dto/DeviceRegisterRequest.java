package HaruIt.domain.users.dto;

import HaruIt.domain.users.entity.Platform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DeviceRegisterRequest(
        @NotBlank @Size(max = 512) String token,
        @NotNull Platform platform
) {}