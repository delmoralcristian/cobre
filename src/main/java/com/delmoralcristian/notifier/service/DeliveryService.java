package com.delmoralcristian.notifier.service;

import static com.delmoralcristian.notifier.enums.ENotificationStatus.COMPLETED;
import static com.delmoralcristian.notifier.enums.ENotificationStatus.FAILED;

import com.delmoralcristian.notifier.exceptions.WebhookDeliveryException;
import com.delmoralcristian.notifier.model.NotificationEvent;
import com.delmoralcristian.notifier.repository.NotificationEventRepository;
import io.micrometer.common.util.StringUtils;
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

    @Retryable(
        maxAttemptsExpression = "#{@retryProperties.maxAttempts}",
        backoff = @Backoff(delayExpression = "#{@retryProperties.backoffDelay}")
    )
    public void reSend(NotificationEvent event) {
        try {
            var client = event.getClient();
            var webhookUrl = client.getWebhookUrl();
            if (StringUtils.isEmpty(webhookUrl)) {
                log.warn("Empty or null webhook URL for client {} and event {}", client.getId(), event.getEventId());
                throw new WebhookDeliveryException("Invalid webhook URL for event " + event.getEventId());
            }
            // Suppose the webhook URL is valid and the client is reachable
            this.restTemplate.postForEntity(client.getWebhookUrl(), event.getContent(), Void.class);
            event.setDeliveryStatus(COMPLETED.name());
            this.notificationEventRepository.save(event);
        } catch (RestClientException ex) {
            log.warn("Webhook failed for event {}: {}", event.getEventId(), ex.getMessage());
            throw new WebhookDeliveryException("Delivery failed for event " + event.getEventId(), ex);
        }
    }

    @Recover
    public void recover(WebhookDeliveryException ex, NotificationEvent event) {
        log.error("Final delivery attempt failed for event {}. Marking as FAILED.", event.getEventId());
        event.setDeliveryStatus(FAILED.name());
        this.notificationEventRepository.save(event);
    }
}

