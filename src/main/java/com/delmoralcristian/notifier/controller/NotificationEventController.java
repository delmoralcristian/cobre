package com.delmoralcristian.notifier.controller;

import com.delmoralcristian.notifier.dto.NotificationEventDTO;
import com.delmoralcristian.notifier.enums.NotificationStatus;
import com.delmoralcristian.notifier.service.NotificationEventService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/notification-events")
@RequiredArgsConstructor
public class NotificationEventController {

    private final NotificationEventService notificationEventService;

    @GetMapping
    public ResponseEntity<List<NotificationEventDTO>> getAll(
        @RequestParam String clientId,
        @RequestParam(required = false) NotificationStatus status,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        return ResponseEntity.ok(this.notificationEventService.findByFilters(clientId, status, from, to));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationEventDTO> getByEventId(@PathVariable String id) {
        return ResponseEntity.ok(this.notificationEventService.getByEventId(id));
    }

    @PostMapping("/{id}/replay")
    public ResponseEntity<Void> replay(@PathVariable String id) {
        this.notificationEventService.replayNotification(id);
        return ResponseEntity.accepted().build();
    }

}
