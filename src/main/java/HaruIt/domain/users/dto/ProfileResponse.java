package HaruIt.domain.users.dto;

import HaruIt.domain.users.entity.User;

public record ProfileResponse(
        Long id,
        String email,
        String nickname
) {
    public static ProfileResponse from(User u) {
        return new ProfileResponse(u.getId(), u.getEmail(), u.getNickname());
    }
}