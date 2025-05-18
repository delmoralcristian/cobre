package com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.adapter;

import com.delmoralcristian.notifier.application.port.out.NotificationEventPersistencePort;
import com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.entity.NotificationEventEntity;
import com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.repository.JpaNotificationEventRepository;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventPersistenceAdapter implements NotificationEventPersistencePort {

    private final JpaNotificationEventRepository repository;


    @Override
    public Optional<NotificationEventEntity> findByEventId(String eventId) {
        return this.repository.findByEventId(eventId);
    }

    @Override
    public boolean existsByEventId(String eventId) {
        return this.repository.existsByEventId(eventId);
    }

    @Override
    public Page<NotificationEventEntity> findByClientIdAndDeliveryStatusAndDeliveryDateBetween(Long clientId,
        String deliveryStatus, Date from, Date to, Pageable pageable) {
        return this.repository.findByClientIdAndDeliveryStatusAndDeliveryDateBetween(clientId, deliveryStatus, from, to,
            pageable);
    }

    @Override
    public NotificationEventEntity save(NotificationEventEntity notificationEventEntity) {
        return this.repository.save(notificationEventEntity);
    }
}
