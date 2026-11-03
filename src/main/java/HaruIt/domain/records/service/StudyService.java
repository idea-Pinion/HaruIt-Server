package HaruIt.domain.records.service;

import HaruIt.domain.records.dto.request.StudyRequest;
import HaruIt.domain.records.dto.response.StudyResponse;
import HaruIt.domain.records.entity.Study;
import HaruIt.domain.records.repository.StudyRepository;
import HaruIt.global.exception.CustomException;
import HaruIt.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {
    private final StudyRepository repository;
    private final Clock clock;

    public StudyResponse create(Long userId, StudyRequest req) {
        checkNotFuture(req.studyDate());
        Study record = new Study(userId, req.studyDate(), req.subject(), req.minutes());
        return StudyResponse.from(repository.save(record));
    }

    @Transactional(readOnly = true)
    public List<StudyResponse> getWeek(Long userId, LocalDate weekStart) {
        return repository.findAllByUserIdAndStudyDateBetweenOrderByStudyDateAscIdAsc(userId, weekStart, weekStart.plusDays(6))
                .stream().map(StudyResponse::from).toList();
    }

    public StudyResponse update(Long userId, Long recordId, StudyRequest req) {
        Study record = get(userId, recordId);
        checkNotFuture(req.studyDate());
        record.update(req.studyDate(), req.subject(), req.minutes());
        return StudyResponse.from(record);
    }

    public void delete(Long userId, Long recordId) {
        repository.delete(get(userId, recordId));
    }

    private void checkNotFuture(LocalDate date) {
        if (date.isAfter(LocalDate.now(clock)))
            throw new CustomException(ErrorCode.FUTURE_RECORD);
    }

    private Study get(Long userId, Long recordId) {
        return repository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));
    }
}
