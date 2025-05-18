package com.delmoralcristian.notifier.mapper;

import static com.delmoralcristian.notifier.enums.ENotificationStatus.COMPLETED;

import com.delmoralcristian.notifier.consumer.dto.EventDTO;
import com.delmoralcristian.notifier.dto.NotificationEventDTO;
import com.delmoralcristian.notifier.enums.EEventType;
import com.delmoralcristian.notifier.enums.ENotificationStatus;
import com.delmoralcristian.notifier.model.Client;
import com.delmoralcristian.notifier.model.NotificationEvent;
import java.util.Date;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationEventMapper {

    default NotificationEventDTO transformToDto(NotificationEvent event) {
        return new NotificationEventDTO(
            event.getEventId(),
            EEventType.fromString(event.getEventType()),
            event.getContent(),
            event.getDeliveryDate(),
            ENotificationStatus.valueOf(event.getDeliveryStatus())
        );
    }

    default NotificationEvent transformToNotificationEvent(Client client, EventDTO eventDTO) {
        return NotificationEvent.builder()
            .eventId(eventDTO.getEventId())
            .eventType(eventDTO.getEventType())
            .content(eventDTO.getContent())
            .client(client)
            .deliveryDate(new Date())
            .deliveryStatus(COMPLETED.name())
            .build();
    }

}
