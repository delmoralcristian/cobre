package com.delmoralcristian.notifier.application.port.in;

import com.delmoralcristian.notifier.application.dto.NotificationEventDTO;
import com.delmoralcristian.notifier.enums.ENotificationStatus;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationEventUseCase {

    List<NotificationEventDTO> findByFilters(String clientId, ENotificationStatus status, LocalDateTime from,
        LocalDateTime to);

    NotificationEventDTO getByEventId(String eventId);

    void replayNotification(String eventId);
}
