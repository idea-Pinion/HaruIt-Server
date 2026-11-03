package HaruIt.domain.records.config;

import HaruIt.domain.records.entity.CaffeineItem;
import HaruIt.domain.records.repository.CaffeineItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CaffeineItemInitializer implements ApplicationRunner {
    private final CaffeineItemRepository repository;

    @Override
    public void run(ApplicationArguments args) {
        //
        if (repository.existsByUserIdIsNull()) return;
        repository.saveAll(List.of(
                CaffeineItem.preset("아메리카노 (355ml)", 150),
                CaffeineItem.preset("카페라떼 (355ml)", 75),
                CaffeineItem.preset("믹스커피 (1봉)", 60),
                CaffeineItem.preset("핫식스 (250ml)", 60),
                CaffeineItem.preset("몬스터에너지 (355ml)", 100),
                CaffeineItem.preset("레드불 (250ml)", 63),
                CaffeineItem.preset("콜라 (355ml)", 34),
                CaffeineItem.preset("녹차 (티백 1개)", 20)
        ));
    }
}
