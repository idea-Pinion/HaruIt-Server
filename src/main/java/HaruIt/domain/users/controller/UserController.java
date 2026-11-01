package HaruIt.domain.users.controller;

import HaruIt.domain.users.dto.DeviceRegisterRequest;
import HaruIt.domain.users.dto.NotificationSettingDto;
import HaruIt.domain.users.dto.PasswordChangeRequest;
import HaruIt.domain.users.dto.ProfileResponse;
import HaruIt.domain.users.dto.ProfileUpdateRequest;
import HaruIt.domain.users.dto.SleepSettingDto;
import HaruIt.domain.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ProfileResponse getProfile(@AuthenticationPrincipal Long userId) {
        return userService.getProfile(userId);
    }

    @PatchMapping
    public ProfileResponse updateProfile(@AuthenticationPrincipal Long userId,
                                         @RequestBody @Valid ProfileUpdateRequest req) {
        return userService.updateProfile(userId, req);
    }

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@AuthenticationPrincipal Long userId,
                               @RequestBody @Valid PasswordChangeRequest req) {
        userService.changePassword(userId, req);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdraw(@AuthenticationPrincipal Long userId) {
        userService.withdraw(userId);
    }

    @GetMapping("/settings/sleep")
    public SleepSettingDto getSleepSetting(@AuthenticationPrincipal Long userId) {
        return userService.getSleepSetting(userId);
    }

    @PatchMapping("/settings/sleep")
    public SleepSettingDto updateSleepSetting(@AuthenticationPrincipal Long userId,
                                              @RequestBody @Valid SleepSettingDto req) {
        return userService.updateSleepSetting(userId, req);
    }

    @GetMapping("/settings/notification")
    public NotificationSettingDto getNotificationSetting(@AuthenticationPrincipal Long userId) {
        return userService.getNotificationSetting(userId);
    }

    @PatchMapping("/settings/notification")
    public NotificationSettingDto updateNotificationSetting(@AuthenticationPrincipal Long userId,
                                                            @RequestBody @Valid NotificationSettingDto req) {
        return userService.updateNotificationSetting(userId, req);
    }

    @PostMapping("/devices")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registerDevice(@AuthenticationPrincipal Long userId,
                                         @RequestBody @Valid DeviceRegisterRequest req) {
        userService.registerDevice(userId, req);
    }
}
