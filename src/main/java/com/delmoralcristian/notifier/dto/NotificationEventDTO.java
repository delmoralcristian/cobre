package com.delmoralcristian.notifier.dto;

import com.delmoralcristian.notifier.enums.NotificationStatus;
import java.util.Date;

public record NotificationEventDTO(
    String eventId,
    String eventType,
    String content,
    Date deliveryDate,
    NotificationStatus deliveryStatus
) {

}
