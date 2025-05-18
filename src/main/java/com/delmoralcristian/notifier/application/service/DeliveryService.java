package com.delmoralcristian.notifier.application.service;

import com.delmoralcristian.notifier.application.port.in.DeliveryServiceUseCase;
import com.delmoralcristian.notifier.application.port.out.ClientPersistencePort;
import com.delmoralcristian.notifier.application.port.out.NotificationEventPersistencePort;
import com.delmoralcristian.notifier.infraestructure.adapter.in.consumer.EventDTO;
import com.delmoralcristian.notifier.infraestructure.adapter.out.mapper.NotificationEventMapper;
import com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.entity.ClientEntity;
import com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.entity.NotificationEventEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService implements DeliveryServiceUseCase {

    private final NotificationEventPersistencePort notificationAdapter;
    private final ClientPersistencePort clientAdapter;
    private final NotificationEventMapper notificationEventMapper;
    private final DeliveryRetryHandler deliveryRetryHandler;

    @Override
    public void send(EventDTO eventDTO) {
        var eventId = eventDTO.getEventId();

        if (this.notificationAdapter.existsByEventId(eventId)) {
            log.warn("Event with ID {} already exists. Skipping delivery.", eventId);
            return;
        }

        var client = this.getClient(Long.parseLong(eventDTO.getClientId()));
        var event = this.notificationEventMapper.transformToNotificationEvent(client, eventDTO);

        this.deliveryRetryHandler.attemptDelivery(event);

        this.notificationAdapter.save(event);
        log.info("Event {} processed with delivery status {}", eventId, event.getDeliveryStatus());
    }

    @Override
    public void reSend(NotificationEventEntity event) {
        this.deliveryRetryHandler.attemptDelivery(event);
        log.info("Event {} reprocessed with delivery status {}", event.getEventId(), event.getDeliveryStatus());
        this.notificationAdapter.save(event);
    }

    private ClientEntity getClient(Long clientId) {
        return this.clientAdapter.findById(clientId)
            .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + clientId));
    }
}
