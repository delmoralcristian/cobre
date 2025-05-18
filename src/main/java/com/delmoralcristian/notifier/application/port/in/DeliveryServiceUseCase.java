package com.delmoralcristian.notifier.application.port.in;

import com.delmoralcristian.notifier.infraestructure.adapter.in.consumer.EventDTO;
import com.delmoralcristian.notifier.infraestructure.adapter.out.persistence.entity.NotificationEventEntity;

public interface DeliveryServiceUseCase {

    void send(EventDTO eventDTO);

    void reSend(NotificationEventEntity eventDTO);

}
