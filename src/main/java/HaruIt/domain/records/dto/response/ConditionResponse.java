package HaruIt.domain.records.dto.response;

import HaruIt.domain.records.entity.Condition;

import java.time.LocalDate;

public record ConditionResponse(
        Long recordId,
        LocalDate recordDate,
        int score,
        String memo
) {
    public static ConditionResponse from(Condition r) {
        return new ConditionResponse(r.getId(), r.getRecordDate(), r.getScore(), r.getMemo());
    }
}
