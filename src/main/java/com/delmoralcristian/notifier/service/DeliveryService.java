package com.delmoralcristian.notifier.service;

import static com.delmoralcristian.notifier.enums.ENotificationStatus.COMPLETED;
import static com.delmoralcristian.notifier.enums.ENotificationStatus.FAILED;

import com.delmoralcristian.notifier.consumer.dto.EventDTO;
import com.delmoralcristian.notifier.exceptions.WebhookDeliveryException;
import com.delmoralcristian.notifier.mapper.NotificationEventMapper;
import com.delmoralcristian.notifier.model.Client;
import com.delmoralcristian.notifier.model.NotificationEvent;
import com.delmoralcristian.notifier.repository.ClientRepository;
import com.delmoralcristian.notifier.repository.NotificationEventRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final RestTemplate restTemplate;
    private final NotificationEventRepository notificationEventRepository;
    private final ClientRepository clientRepository;
    private final NotificationEventMapper notificationEventMapper;

    public void send(EventDTO eventDTO) {
        var eventId = eventDTO.getEventId();

        if (this.notificationEventRepository.existsByEventId(eventId)) {
            log.warn("Event with ID {} already exists. Skipping delivery.", eventId);
            return;
        }

        var client = getClient(Long.parseLong(eventDTO.getClientId()));
        var event = this.notificationEventMapper.transformToNotificationEvent(client, eventDTO);

        try {
            attemptDelivery(event);
            event.setDeliveryStatus(COMPLETED.name());
        } catch (WebhookDeliveryException ex) {
            log.warn("Initial delivery failed for event {}: {}", eventId, ex.getMessage());
            event.setDeliveryStatus(FAILED.name());
        }

        this.notificationEventRepository.save(event);
        log.info("Event {} delivered successfully", eventId);
    }

    @Retryable(
        maxAttemptsExpression = "#{@retryProperties.maxAttempts}",
        backoff = @Backoff(delayExpression = "#{@retryProperties.backoffDelay}")
    )
    public void reSend(NotificationEvent event) {
        this.attemptDelivery(event);
        event.setDeliveryStatus(COMPLETED.name());
        this.notificationEventRepository.save(event);
        log.info("Event {} re-delivered successfully.", event.getEventId());
    }

    @Recover
    public void recover(WebhookDeliveryException ex, NotificationEvent event) {
        log.error("Final delivery attempt failed for event {}. Marking as FAILED.", event.getEventId());
        event.setDeliveryStatus(FAILED.name());
        this.notificationEventRepository.save(event);
    }

    private void attemptDelivery(NotificationEvent event) {
        var client = event.getClient();
        var webhookUrl = client.getWebhookUrl();

        if (StringUtils.isEmpty(webhookUrl)) {
            log.warn("Empty or null webhook URL for client {} and event {}", client.getId(), event.getEventId());
            throw new WebhookDeliveryException("Invalid webhook URL for event " + event.getEventId());
        }

        try {
            log.info("Sending event {} to webhook URL: {}", event.getEventId(), webhookUrl);
            this.restTemplate.postForEntity(webhookUrl, event.getContent(), Void.class);
        } catch (RestClientException ex) {
            log.warn("Webhook failed for event {}: {}", event.getEventId(), ex.getMessage());
            throw new WebhookDeliveryException("Delivery failed for event " + event.getEventId(), ex);
        }
    }

    private Client getClient(Long clientId) {
        return this.clientRepository.findById(clientId)
            .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + clientId));
    }
}

