package HaruIt.domain.records.dto.response;

import HaruIt.domain.records.entity.CaffeineItem;

public record CaffeineItemResponse(
        Long itemId,
        String name,
        int caffeineMg,
        boolean custom
) {
    public static CaffeineItemResponse from(CaffeineItem i) {
        return new CaffeineItemResponse(i.getId(), i.getName(), i.getCaffeineMg(), !i.isPreset());
    }
}
