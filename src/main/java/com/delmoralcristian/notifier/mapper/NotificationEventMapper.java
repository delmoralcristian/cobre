package com.delmoralcristian.notifier.mapper;

import com.delmoralcristian.notifier.dto.NotificationEventDTO;
import com.delmoralcristian.notifier.enums.EEventType;
import com.delmoralcristian.notifier.enums.ENotificationStatus;
import com.delmoralcristian.notifier.model.NotificationEvent;
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

}
