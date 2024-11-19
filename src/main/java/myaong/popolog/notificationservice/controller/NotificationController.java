package myaong.popolog.notificationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import myaong.popolog.notificationservice.common.exception.ApiResponse;
import myaong.popolog.notificationservice.service.NotificationService;
import myaong.popolog.notificationservice.dto.NotificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "API 명세서 v0.4 line 85", description = "알림 조회")
    @GetMapping("/{lastId}")
    public ResponseEntity<ApiResponse<NotificationResponse>> getNotifications(
            @PathVariable Long lastId,
            @RequestHeader(name = "memberId", required = false) Long memberId) {
        NotificationResponse response = notificationService.getNotifications(memberId, lastId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));  // 성공 응답
    }

    @Operation(summary = "API 명세서 v0.4 line 86", description = "알림 읽음 처리")
    @PutMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long notificationId,
            @RequestHeader(name = "memberId", required = false) Long memberId) {
        notificationService.markNotificationAsRead(notificationId, memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @Operation(summary = "API 명세서 v0.4 line 87", description = "알림 삭제")
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable Long notificationId,
            @RequestHeader(name = "memberId", required = false) Long memberId) {
        notificationService.deleteNotification(notificationId, memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
}
