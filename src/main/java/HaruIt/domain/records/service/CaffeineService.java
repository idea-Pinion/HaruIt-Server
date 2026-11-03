package HaruIt.domain.records.service;

import HaruIt.domain.records.dto.request.CaffeineIntakeRequest;
import HaruIt.domain.records.dto.request.CaffeineItemRequest;
import HaruIt.domain.records.dto.response.CaffeineIntakeResponse;
import HaruIt.domain.records.dto.response.CaffeineItemResponse;
import HaruIt.domain.records.entity.CaffeineIntake;
import HaruIt.domain.records.entity.CaffeineItem;
import HaruIt.domain.records.repository.CaffeineIntakeRepository;
import HaruIt.domain.records.repository.CaffeineItemRepository;
import HaruIt.global.exception.CustomException;
import HaruIt.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CaffeineService {
    private final CaffeineItemRepository itemRepository;
    private final CaffeineIntakeRepository intakeRepository;
    private final Clock clock;

    // 음료 항목

    @Transactional(readOnly = true)
    public List<CaffeineItemResponse> getItems(Long userId) {
        return itemRepository.findAllByUserIdIsNullOrUserIdOrderByIdAsc(userId)
                .stream().map(CaffeineItemResponse::from).toList();
    }

    public CaffeineItemResponse addItem(Long userId, CaffeineItemRequest req) {
        CaffeineItem item = itemRepository.save(CaffeineItem.custom(userId, req.name(), req.caffeineMg()));
        return CaffeineItemResponse.from(item);
    }

    public void deleteItem(Long userId, Long itemId) {
        CaffeineItem item = getItem(userId, itemId);
        if (item.isPreset())
            throw new CustomException(ErrorCode.PRESET_ITEM_NOT_DELETABLE);
        itemRepository.delete(item);
    }

    // 섭취 항목

    public CaffeineIntakeResponse create(Long userId, CaffeineIntakeRequest req) {
        CaffeineItem item = getItem(userId, req.itemId());
        checkNotFuture(req.intakeAt());
        return CaffeineIntakeResponse.from(intakeRepository.save(new CaffeineIntake(userId, item, req.intakeAt())));
    }

    @Transactional(readOnly = true)
    public List<CaffeineIntakeResponse> getWeek(Long userId, LocalDate weekStart) {
        return intakeRepository.findAllByUserIdAndIntakeAtGreaterThanEqualAndIntakeAtLessThanOrderByIntakeAtAsc(
                        userId, weekStart.atStartOfDay(), weekStart.plusDays(7).atStartOfDay())
                .stream().map(CaffeineIntakeResponse::from).toList();
    }

    public CaffeineIntakeResponse update(Long userId, Long recordId, CaffeineIntakeRequest req) {
        CaffeineIntake record = intakeRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));
        CaffeineItem item = getItem(userId, req.itemId());
        checkNotFuture(req.intakeAt());
        record.change(item, req.intakeAt());
        return CaffeineIntakeResponse.from(record);
    }

    public void delete(Long userId, Long recordId) {
        CaffeineIntake record = intakeRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));
        intakeRepository.delete(record);
    }

    // 기본 항목 또는 본인 항목만 사용 가능

    @Transactional(readOnly = true)
    public CaffeineItem getItem(Long userId, Long itemId) {
        return itemRepository.findUsable(itemId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CAFFEINE_ITEM_NOT_FOUND));
    }

    private void checkNotFuture(LocalDateTime at) {
        if (at.isAfter(LocalDateTime.now(clock)))
            throw new CustomException(ErrorCode.FUTURE_RECORD);
    }
}
