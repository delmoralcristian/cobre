package com.delmoralcristian.notifier.infraestructure.adapter.out.mapper;

import static com.delmoralcristian.notifier.enums.ENotificationStatus.COMPLETED;

import com.delmoralcristian.notifier.application.dto.NotificationEventDTO;
import com.delmoralcristian.notifier.enums.EEventType;
import com.delmoralcristian.notifier.enums.ENotificationStatus;
import com.delmoralcristian.notifier.infraestructure.adapter.in.consumer.EventDTO;
import com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.entity.ClientEntity;
import com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.entity.NotificationEventEntity;
import java.util.Date;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationEventMapper {

    default NotificationEventDTO transformToDto(NotificationEventEntity event) {
        return new NotificationEventDTO(
            event.getEventId(),
            EEventType.fromString(event.getEventType()),
            event.getContent(),
            event.getDeliveryDate(),
            ENotificationStatus.valueOf(event.getDeliveryStatus())
        );
    }

    default NotificationEventEntity transformToNotificationEvent(ClientEntity client, EventDTO eventDTO) {
        return NotificationEventEntity.builder()
            .eventId(eventDTO.getEventId())
            .eventType(eventDTO.getEventType())
            .content(eventDTO.getContent())
            .client(client)
            .deliveryDate(new Date())
            .deliveryStatus(COMPLETED.name())
            .build();
    }

}
