package HaruIt.domain.records.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record StudyRequest(
        @NotNull LocalDate studyDate,
        @NotBlank @Size(max = 30) String subject,
        @NotNull @Min(1) @Max(1440) Integer minutes
) {}
