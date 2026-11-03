package HaruIt.domain.records.dto.response;

import HaruIt.domain.records.entity.CaffeineIntake;

import java.time.LocalDateTime;

public record CaffeineIntakeResponse(
        Long recordId,
        Long itemId,
        String itemName,
        int caffeineMg,
        LocalDateTime intakeAt
) {
    public static CaffeineIntakeResponse from(CaffeineIntake r) {
        return new CaffeineIntakeResponse(r.getId(), r.getItemId(), r.getItemName(),
                r.getCaffeineMg(), r.getIntakeAt());
    }
}
