package com.delmoralcristian.notifier.service;

import com.delmoralcristian.notifier.config.TrackProcessingTime;
import com.delmoralcristian.notifier.dto.NotificationEventDTO;
import com.delmoralcristian.notifier.enums.NotificationStatus;
import com.delmoralcristian.notifier.mapper.NotificationEventMapper;
import com.delmoralcristian.notifier.repository.NotificationEventRepository;
import com.delmoralcristian.notifier.utils.DateUtil;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationEventService {

    private static final int PAGE_SIZE = 100;

    private final NotificationEventRepository notificationEventRepository;

    private final NotificationEventMapper notificationEventMapper;

    @TrackProcessingTime
    public List<NotificationEventDTO> findByFilters(
        String clientId,
        NotificationStatus status,
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

    private List<NotificationEventDTO> fetchAllPages(
        String clientId, String status, Date fromDate, Date toDate) {

        List<NotificationEventDTO> results = new ArrayList<>();
        var pageSize = PAGE_SIZE;
        var pageNumber = 0;

        while (true) {
            var pageable = PageRequest.of(pageNumber, pageSize);
            var page = this.notificationEventRepository
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


    @TrackProcessingTime
    public NotificationEventDTO getByEventId(String eventId) {
        log.info("Finding notification event by eventId: {}", eventId);
        var event = this.notificationEventRepository.findByEventId(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Notification event not found for eventId: " + eventId));
        return this.notificationEventMapper.transformToDto(event);
    }

    @Retryable(retryFor = Exception.class, maxAttempts = 4, backoff = @Backoff(delay = 100))
    @TrackProcessingTime
    public void replayNotification(String id) {
        log.info("Replaying notification event with id: {}", id);
        // Logic to replay the notification
    }
}
