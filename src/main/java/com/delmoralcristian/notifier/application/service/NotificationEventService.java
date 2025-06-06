package com.delmoralcristian.notifier.application.service;

import com.delmoralcristian.notifier.advice.TrackProcessingTime;
import com.delmoralcristian.notifier.application.dto.NotificationEventDTO;
import com.delmoralcristian.notifier.application.port.in.NotificationEventUseCase;
import com.delmoralcristian.notifier.application.port.out.NotificationEventPersistencePort;
import com.delmoralcristian.notifier.enums.ENotificationStatus;
import com.delmoralcristian.notifier.infraestructure.adapter.out.mapper.NotificationEventMapper;
import com.delmoralcristian.notifier.utils.DateUtil;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationEventService implements NotificationEventUseCase {

    private static final int PAGE_SIZE = 100;

    private final DeliveryService deliveryService;
    private final NotificationEventPersistencePort notificationAdapter;
    private final NotificationEventMapper notificationEventMapper;

    @TrackProcessingTime
    @Override
    public List<NotificationEventDTO> findByFilters(
        String clientId,
        ENotificationStatus status,
        LocalDateTime from,
        LocalDateTime to) {

        if (to.isBefore(from) || to.isEqual(from)) {
            throw new IllegalArgumentException("'to' datetime must be after 'from' datetime");
        }

        log.info("Finding notification events by filters: clientId={}, status={}, from={}, to={}",
            clientId, status, from, to);

        var fromDate = DateUtil.toDate(from);
        var toDate = DateUtil.toDate(to);

        return this.fetchAllPages(clientId, status.name(), fromDate, toDate);
    }

    @TrackProcessingTime
    @Override
    public NotificationEventDTO getByEventId(String eventId) {
        log.info("Finding notification event by eventId: {}", eventId);
        var event = this.notificationAdapter.findByEventId(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Notification event not found for eventId: " + eventId));
        return this.notificationEventMapper.transformToDto(event);
    }

    @TrackProcessingTime
    @Transactional
    @Override
    public void replayNotification(String eventId) {
        log.info("Replaying notification event with eventId: {}", eventId);

        var event = this.notificationAdapter.findByEventId(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Notification event not found for eventId: " + eventId));

        this.deliveryService.reSend(event);

    }

    private List<NotificationEventDTO> fetchAllPages(
        String clientId, String status, Date fromDate, Date toDate) {

        List<NotificationEventDTO> results = new ArrayList<>();
        var pageSize = PAGE_SIZE;
        var pageNumber = 0;

        while (true) {
            var pageable = PageRequest.of(pageNumber, pageSize);
            var page = this.notificationAdapter
                .findByClientIdAndDeliveryStatusAndDeliveryDateBetween(
                    Long.valueOf(clientId), status, fromDate, toDate, pageable);

            if (page.hasContent()) {
                var dtos = page.getContent().stream()
                    .map(this.notificationEventMapper::transformToDto)
                    .toList();
                results.addAll(dtos);
            }

            if (!page.hasNext()) {
                break;
            }

            pageNumber++;
            pageSize += PAGE_SIZE;
        }

        return results;
    }
}
