package HaruIt.domain.daily.service;

import HaruIt.domain.daily.dto.DailyResponse;
import HaruIt.domain.daily.dto.DailyUpdateRequest;
import HaruIt.domain.records.repository.CaffeineIntakeRepository;
import HaruIt.domain.records.repository.ConditionRepository;
import HaruIt.domain.records.repository.SleepRepository;
import HaruIt.domain.records.repository.StudyRepository;
import HaruIt.domain.records.service.ConditionService;
import HaruIt.domain.targets.entity.Target;
import HaruIt.domain.targets.repository.TargetCheckRepository;
import HaruIt.domain.targets.repository.TargetRepository;
import HaruIt.domain.targets.service.TargetService;
import HaruIt.global.exception.CustomException;
import HaruIt.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyService {
    private final SleepRepository sleepRepository;
    private final StudyRepository studyRepository;
    private final ConditionRepository conditionRepository;
    private final CaffeineIntakeRepository caffeineIntakeRepository;
    private final TargetRepository targetRepository;
    private final TargetCheckRepository targetCheckRepository;

    private final TargetService targetService;
    private final ConditionService conditionService;
    private final Clock clock;

    public DailyResponse get(Long userId, LocalDate date) {
        List<Target> targets = targetRepository.findAllByUserIdAndStartDateLessThanEqualOrderByIdAsc(userId, date);
        Set<Long> checkedIds = targets.isEmpty() ? Set.of()
                : targetCheckRepository.findCheckedTargetIds(targets.stream().map(Target::getId).toList(), date);

        return DailyResponse.of(
                date,
                sleepRepository.findAllByUserIdAndSleepDateBetweenOrderByBedTimeAsc(userId, date, date),
                studyRepository.findAllByUserIdAndStudyDateBetweenOrderByStudyDateAscIdAsc(userId, date, date),
                conditionRepository.findByUserIdAndRecordDate(userId, date).orElse(null),
                caffeineIntakeRepository.findAllByUserIdAndIntakeAtGreaterThanEqualAndIntakeAtLessThanOrderByIntakeAtAsc(
                        userId, date.atStartOfDay(), date.plusDays(1).atStartOfDay()),
                targets,
                checkedIds
        );
    }

    @Transactional
    public DailyResponse update(Long userId, LocalDate date, DailyUpdateRequest req) {
        if (date.isAfter(LocalDate.now(clock)))
            throw new CustomException(ErrorCode.FUTURE_RECORD);

        if (req.condition() != null)
            conditionService.upsert(userId, date, req.condition().score(), req.condition().memo());

        if (req.targets() != null)
            req.targets().forEach(t -> {
                if (t.checked()) targetService.check(userId, t.targetId(), date);
                else targetService.uncheck(userId, t.targetId(), date);
            });

        return get(userId, date);
    }
}
