package HaruIt.domain.users.service;

import HaruIt.domain.users.dto.DeviceRegisterRequest;
import HaruIt.domain.users.dto.NotificationSettingDto;
import HaruIt.domain.users.dto.PasswordChangeRequest;
import HaruIt.domain.users.dto.ProfileResponse;
import HaruIt.domain.users.dto.ProfileUpdateRequest;
import HaruIt.domain.users.dto.SleepSettingDto;
import HaruIt.domain.users.entity.Device;
import HaruIt.domain.users.entity.User;
import HaruIt.domain.users.repository.DeviceRepository;
import HaruIt.domain.users.repository.UserRepository;
import HaruIt.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(Long userId) {
        return ProfileResponse.from(getUser(userId));
    }

    public ProfileResponse updateProfile(Long userId, ProfileUpdateRequest req) {
        User user = getUser(userId);
        user.updateNickname(req.nickname());
        return ProfileResponse.from(user);
    }

    public void changePassword(Long userId, PasswordChangeRequest req) {
        User user = getUser(userId);
        if (!passwordEncoder.matches(req.currentPassword(), user.getPassword()))
            throw new CustomException(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다.");
        if (req.currentPassword().equals(req.newPassword()))
            throw new CustomException(HttpStatus.BAD_REQUEST, "기존 비밀번호와 같습니다.");
        user.changePassword(passwordEncoder.encode(req.newPassword()));
    }

    public void withdraw(Long userId) {
        User user = getUser(userId);
        deviceRepository.deleteAllByUserId(userId);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public SleepSettingDto getSleepSetting(Long userId) {
        return SleepSettingDto.from(getUser(userId).getSleepSetting());
    }

    public SleepSettingDto updateSleepSetting(Long userId, SleepSettingDto req) {
        var setting = getUser(userId).getSleepSetting();
        setting.update(req.targetBedTime(), req.targetWakeTime());
        return SleepSettingDto.from(setting);
    }

    @Transactional(readOnly = true)
    public NotificationSettingDto getNotificationSetting(Long userId) {
        return NotificationSettingDto.from(getUser(userId).getNotificationSetting());
    }

    public NotificationSettingDto updateNotificationSetting(Long userId, NotificationSettingDto req) {
        var setting = getUser(userId).getNotificationSetting();
        setting.update(req.bedtimeAlarm(), req.bedtimeAlarmMinutesBefore(), req.caffeineAlarm(), req.weeklyReportAlarm());
        return NotificationSettingDto.from(setting);
    }

    public void registerDevice(Long userId, DeviceRegisterRequest req) {
        User user = getUser(userId);
        LocalDateTime now = LocalDateTime.now(clock);
        deviceRepository.findByToken(req.token())
                .ifPresentOrElse(
                        device -> device.reassign(user, req.platform(), now),
                        () -> deviceRepository.save(new Device(user, req.token(), req.platform(), now))
                );
    }

    public void unregisterDevice(Long userId, String token) {
        deviceRepository.deleteByTokenAndUserId(token, userId);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
}
