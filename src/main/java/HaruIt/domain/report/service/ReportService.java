package HaruIt.domain.report.service;

import HaruIt.domain.records.entity.CaffeineIntake;
import HaruIt.domain.records.entity.Condition;
import HaruIt.domain.records.entity.Sleep;
import HaruIt.domain.records.entity.Study;
import HaruIt.domain.records.repository.CaffeineIntakeRepository;
import HaruIt.domain.records.repository.ConditionRepository;
import HaruIt.domain.records.repository.SleepRepository;
import HaruIt.domain.records.repository.StudyRepository;
import HaruIt.domain.report.dto.Day;
import HaruIt.domain.report.dto.Summary;
import HaruIt.domain.report.dto.WeeklyReportResponse;
import HaruIt.global.exception.CustomException;
import HaruIt.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final Clock clock;
    private final SleepRepository sleepRepository;
    private final StudyRepository studyRepository;
    private final ConditionRepository conditionRepository;
    private final CaffeineIntakeRepository caffeineIntakeRepository;

    public WeeklyReportResponse getWeekly(Long userId, LocalDate weekStart) {
        if (weekStart == null)
            weekStart = LocalDate.now(clock).with(DayOfWeek.MONDAY);
        if (weekStart.getDayOfWeek() != DayOfWeek.MONDAY)
            throw new CustomException(ErrorCode.INVALID_INPUT, "weekStart는 월요일이어야 합니다.");
        LocalDate weekEnd = weekStart.plusDays(6);

        Map<LocalDate, Integer> sleep = sleepRepository
                .findAllByUserIdAndSleepDateBetweenOrderByBedTimeAsc(userId, weekStart, weekEnd).stream()
                .collect(groupingBy(Sleep::getSleepDate, summingInt(s -> (int) s.getSleepMinutes())));

        Map<LocalDate, Integer> study = studyRepository
                .findAllByUserIdAndStudyDateBetweenOrderByStudyDateAscIdAsc(userId, weekStart, weekEnd).stream()
                .collect(groupingBy(Study::getStudyDate, summingInt(Study::getMinutes)));

        Map<LocalDate, Integer> condition = conditionRepository
                .findAllByUserIdAndRecordDateBetweenOrderByRecordDateAsc(userId, weekStart, weekEnd).stream()
                .collect(toMap(Condition::getRecordDate, Condition::getScore));

        Map<LocalDate, Integer> caffeine = caffeineIntakeRepository
                .findAllByUserIdAndIntakeAtGreaterThanEqualAndIntakeAtLessThanOrderByIntakeAtAsc(
                        userId, weekStart.atStartOfDay(), weekEnd.plusDays(1).atStartOfDay()).stream()
                .collect(groupingBy(i -> i.getIntakeAt().toLocalDate(), summingInt(CaffeineIntake::getCaffeineMg)));

        List<Day> days = weekStart.datesUntil(weekEnd.plusDays(1))
                .map(d -> new Day(
                        d,
                        sleep.get(d),
                        study.getOrDefault(d, 0),
                        condition.get(d),
                        caffeine.getOrDefault(d, 0)))
                .toList();

        Summary summary = new Summary(
                avg(sleep.values()),
                study.values().stream().mapToInt(Integer::intValue).sum(),
                avg(condition.values()),
                caffeine.values().stream().mapToInt(Integer::intValue).sum());

        return new WeeklyReportResponse(weekStart, weekEnd, summary, days);
    }

    private static Double avg(Collection<? extends Number> values) {
        if (values.isEmpty()) return null;
        double avg = values.stream().mapToDouble(Number::doubleValue).average().orElse(0);
        return Math.round(avg * 10) / 10.0;
    }
}
