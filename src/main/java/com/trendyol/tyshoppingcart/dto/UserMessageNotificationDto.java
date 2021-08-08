package com.trendyol.tyshoppingcart.dto;

import com.trendyol.tyshoppingcart.domain.UserNotificationType;
import lombok.Data;

import java.util.List;

@Data
public class UserMessageNotificationDto {
    private List<UserDto> userList;

    private UserNotificationType userNotificationType;
}
