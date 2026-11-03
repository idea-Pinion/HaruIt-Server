package HaruIt.domain.records.service;

import HaruIt.domain.records.dto.request.SleepRequest;
import HaruIt.domain.records.dto.response.SleepResponse;
import HaruIt.domain.records.entity.Sleep;
import HaruIt.domain.records.repository.SleepRepository;
import HaruIt.global.exception.CustomException;
import HaruIt.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SleepService {
    private final SleepRepository repository;
    private final Clock clock;

    public SleepResponse create(Long userId, SleepRequest req) {
        validate(userId, req, null);
        return SleepResponse.from(repository.save(new Sleep(userId, req.bedTime(), req.wakeTime())));
    }

    @Transactional(readOnly = true)
    public List<SleepResponse> getWeek(Long userId, LocalDate weekStart) {
        return repository.findAllByUserIdAndSleepDateBetweenOrderByBedTimeAsc(userId, weekStart, weekStart.plusDays(6))
                .stream().map(SleepResponse::from).toList();
    }

    public SleepResponse update(Long userId, Long recordId, SleepRequest req) {
        Sleep record = get(userId, recordId);
        validate(userId, req, recordId);
        record.update(req.bedTime(), req.wakeTime());
        return SleepResponse.from(record);
    }

    public void delete(Long userId, Long recordId) {
        repository.delete(get(userId, recordId));
    }

    private void validate(Long userId, SleepRequest req, Long excludeId) {
        if (req.wakeTime().isAfter(LocalDateTime.now(clock)))
            throw new CustomException(ErrorCode.FUTURE_RECORD);
        if (repository.existsOverlap(userId, req.bedTime(), req.wakeTime(), excludeId))
            throw new CustomException(ErrorCode.SLEEP_RECORD_OVERLAP);
    }

    private Sleep get(Long userId, Long recordId) {
        return repository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));
    }
}
