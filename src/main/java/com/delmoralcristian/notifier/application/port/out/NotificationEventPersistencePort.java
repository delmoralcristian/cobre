package com.delmoralcristian.notifier.application.port.out;

import com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.entity.NotificationEventEntity;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationEventPersistencePort {

    Optional<NotificationEventEntity> findByEventId(String eventId);

    boolean existsByEventId(String eventId);

    Page<NotificationEventEntity> findByClientIdAndDeliveryStatusAndDeliveryDateBetween(
        Long clientId,
        String deliveryStatus,
        Date from,
        Date to,
        Pageable pageable
    );

    NotificationEventEntity save(NotificationEventEntity notificationEventEntity);
}
