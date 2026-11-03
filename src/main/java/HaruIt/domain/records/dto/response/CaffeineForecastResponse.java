package HaruIt.domain.records.dto.response;

import java.time.LocalDateTime;

public record CaffeineForecastResponse(
        Long itemId,
        LocalDateTime at,
        LocalDateTime bedTime,
        int residualAtBedtimeMg,
        int residualAtBedtimeIfDrinkMg,
        boolean sleepSafe
) {}
