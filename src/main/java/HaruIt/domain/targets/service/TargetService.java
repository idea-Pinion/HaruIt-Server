package HaruIt.domain.targets.service;

import HaruIt.domain.targets.dto.AchievementResponse;
import HaruIt.domain.targets.dto.TargetRequest;
import HaruIt.domain.targets.dto.TargetResponse;
import HaruIt.domain.targets.entity.Target;
import HaruIt.domain.targets.repository.TargetCheckRepository;
import HaruIt.domain.targets.repository.TargetRepository;
import HaruIt.global.exception.CustomException;
import HaruIt.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class TargetService {
    private final TargetRepository targetRepository;
    private final TargetCheckRepository checkRepository;
    private final Clock clock;

    public TargetResponse create(Long userId, TargetRequest req) {
        Target target = targetRepository.save(new Target(userId, req.title(), today()));
        return TargetResponse.from(target, false);
    }

    @Transactional(readOnly = true)
    public List<TargetResponse> getAll(Long userId) {
        List<Target> targets = targetRepository.findAllByUserIdOrderByIdDesc(userId);
        if (targets.isEmpty()) return List.of();

        List<Long> ids = targets.stream().map(Target::getId).toList();
        Set<Long> checkedIds = checkRepository.findCheckedTargetIds(ids, today());

        return targets.stream()
                .map(t -> TargetResponse.from(t, checkedIds.contains(t.getId())))
                .toList();
    }

    public TargetResponse update(Long userId, Long targetId, TargetRequest req) {
        Target target = get(userId, targetId);
        target.rename(req.title());
        boolean checkedToday = checkRepository.existsByTargetIdAndCheckDate(targetId, today());
        return TargetResponse.from(target, checkedToday);
    }

    public void delete(Long userId, Long targetId) {
        Target target = get(userId, targetId);
        checkRepository.deleteAllByTargetId(targetId);
        targetRepository.delete(target);
    }

    @Transactional(readOnly = true)
    public AchievementResponse achievement(Long userId, Long targetId, LocalDate from, LocalDate to) {
        Target target = get(userId, targetId);
        LocalDate today = today();

        LocalDate start = (from == null || from.isBefore(target.getStartDate())) ? target.getStartDate() : from;
        LocalDate end = (to == null || to.isAfter(today)) ? today : to;
        if (start.isAfter(end))
            throw new CustomException(ErrorCode.INVALID_PERIOD);

        long total = ChronoUnit.DAYS.between(start, end) + 1;
        long checked = checkRepository.countByTargetIdAndCheckDateBetween(targetId, start, end);
        double rate = Math.round(checked * 1000.0 / total) / 10.0;

        return new AchievementResponse(targetId, start, end, total, checked, rate);
    }

    public void check(Long userId, Long targetId, LocalDate date) {
        Target target = get(userId, targetId);
        target.validateCheckable(date, today());
        checkRepository.insertIfAbsent(targetId, date);
    }

    public void uncheck(Long userId, Long targetId, LocalDate date) {
        get(userId, targetId);
        checkRepository.deleteCheck(targetId, date);
    }

    private Target get(Long userId, Long targetId) {
        return targetRepository.findByIdAndUserId(targetId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.TARGET_NOT_FOUND));
    }

    private LocalDate today() {
        return LocalDate.now(clock);
    }
}
