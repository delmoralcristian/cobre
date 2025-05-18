package com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.repository;

import com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.entity.NotificationEventEntity;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaNotificationEventRepository extends CrudRepository<NotificationEventEntity, Long> {

    Optional<NotificationEventEntity> findByEventId(String eventId);

    boolean existsByEventId(String eventId);

    Page<NotificationEventEntity> findByClientIdAndDeliveryStatusAndDeliveryDateBetween(
        Long clientId,
        String deliveryStatus,
        Date from,
        Date to,
        Pageable pageable
    );

}
