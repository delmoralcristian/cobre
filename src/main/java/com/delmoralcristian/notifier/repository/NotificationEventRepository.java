package com.delmoralcristian.notifier.repository;

import com.delmoralcristian.notifier.model.NotificationEvent;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationEventRepository extends CrudRepository<NotificationEvent, Long> {

    Optional<NotificationEvent> findByEventId(String eventId);

    boolean existsByEventId(String eventId);

    Page<NotificationEvent> findByClientIdAndDeliveryStatusAndDeliveryDateBetween(
        Long clientId,
        String deliveryStatus,
        Date from,
        Date to,
        Pageable pageable
    );

}
