package HaruIt.domain.records.service;

import HaruIt.domain.records.dto.request.ConditionCreateRequest;
import HaruIt.domain.records.dto.request.ConditionUpdateRequest;
import HaruIt.domain.records.dto.response.ConditionResponse;
import HaruIt.domain.records.entity.Condition;
import HaruIt.domain.records.repository.ConditionRepository;
import HaruIt.global.exception.CustomException;
import HaruIt.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConditionService {
    private final ConditionRepository repository;
    private final Clock clock;

    public ConditionResponse create(Long userId, ConditionCreateRequest req) {
        checkNotFuture(req.recordDate());
        if (repository.existsByUserIdAndRecordDate(userId, req.recordDate()))
            throw new CustomException(ErrorCode.CONDITION_ALREADY_EXISTS);
        Condition record = new Condition(userId, req.recordDate(), req.score(), req.memo());
        return ConditionResponse.from(repository.save(record));
    }

    @Transactional(readOnly = true)
    public List<ConditionResponse> getWeek(Long userId, LocalDate weekStart) {
        return repository.findAllByUserIdAndRecordDateBetweenOrderByRecordDateAsc(userId, weekStart, weekStart.plusDays(6))
                .stream().map(ConditionResponse::from).toList();
    }

    public ConditionResponse update(Long userId, Long recordId, ConditionUpdateRequest req) {
        Condition record = get(userId, recordId);
        record.update(req.score(), req.memo());
        return ConditionResponse.from(record);
    }

    public void delete(Long userId, Long recordId) {
        repository.delete(get(userId, recordId));
    }

    private void checkNotFuture(LocalDate date) {
        if (date.isAfter(LocalDate.now(clock)))
            throw new CustomException(ErrorCode.FUTURE_RECORD);
    }

    private Condition get(Long userId, Long recordId) {
        return repository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));
    }

    public void upsert(Long userId, LocalDate date, int score, String memo) {
        checkNotFuture(date);
        repository.findByUserIdAndRecordDate(userId, date).ifPresentOrElse(
                c -> c.update(score, memo == null ? c.getMemo() : memo),
                () -> repository.save(new Condition(userId, date, score, memo))
        );
    }
}
