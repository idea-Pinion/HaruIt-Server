package HaruIt.domain.users.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Platform {
    ANDROID("안드로이드"),
    IOS("아이폰"),
    WEB("웹사이트");

    private final String label;
}
