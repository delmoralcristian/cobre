package com.delmoralcristian.notifier.infraestructure.adapter.in.consumer;

import com.delmoralcristian.notifier.application.service.DeliveryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.annotation.SqsListenerAcknowledgementMode;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(value = "aws.sqs.consumer.event-notifications.enabled", havingValue = "true")
@RequiredArgsConstructor
public class EventConsumer {

    private static final TypeReference<EventDTO> EVENT_TYPE_REFERENCE = new TypeReference<>() {
    };

    private final DeliveryService deliveryService;
    private final ObjectMapper objectMapper;


    @SqsListener(
        value = "${aws.sqs.consumer.event-notifications.queue-name}",
        maxConcurrentMessages = "${aws.sqs.consumer.event-notifications.maxConcurrentMessage:5}",
        maxMessagesPerPoll = "${aws.sqs.consumer.event-notifications.maxMessagesPerPoll:5}",
        pollTimeoutSeconds = "${aws.sqs.consumer.event-notifications.pollTimeoutSeconds:20}",
        acknowledgementMode = SqsListenerAcknowledgementMode.MANUAL
    )
    public void processMessage(@Payload String message, @Headers MessageHeaders headers,
        Acknowledgement ack) {
        try {
            log.info("Received message: {}", message);
            this.deliveryService.send(objectMapper.readValue(message, EVENT_TYPE_REFERENCE));
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }

}
