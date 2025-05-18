package com.delmoralcristian.notifier.mapper;

import com.delmoralcristian.notifier.dto.NotificationEventDTO;
import com.delmoralcristian.notifier.enums.NotificationStatus;
import com.delmoralcristian.notifier.model.NotificationEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationEventMapper {

    default NotificationEventDTO transformToDto(NotificationEvent event) {

        return new NotificationEventDTO(
            event.getEventId(),
            event.getEventType(),
            event.getContent(),
            event.getDeliveryDate(),
            NotificationStatus.valueOf(event.getDeliveryStatus())
        );

    }

}
