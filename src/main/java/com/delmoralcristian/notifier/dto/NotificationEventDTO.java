package com.delmoralcristian.notifier.dto;

import com.delmoralcristian.notifier.enums.EEventType;
import com.delmoralcristian.notifier.enums.ENotificationStatus;
import java.util.Date;

public record NotificationEventDTO(
    String eventId,
    EEventType eventType,
    String content,
    Date deliveryDate,
    ENotificationStatus deliveryStatus
) {

}
