package ru.glashiii.springauthms.dto;

import ru.glashiii.springauthms.entities.UserInfo;

public record RotatedRefreshToken(UserInfo user , String refreshToken) {
}
