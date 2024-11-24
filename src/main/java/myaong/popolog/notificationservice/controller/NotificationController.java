package myaong.popolog.notificationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.PositiveOrZero;
import myaong.popolog.notificationservice.common.exception.ApiResponse;
import myaong.popolog.notificationservice.constant.NotificationType;
import myaong.popolog.notificationservice.dto.request.NotificationRequest;
import myaong.popolog.notificationservice.service.NotificationService;
import myaong.popolog.notificationservice.dto.response.NotificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "API 명세서 v0.4 서비스간 API line 16", description = "알림 생성")
    @PostMapping("/{type}")
    public ResponseEntity<ApiResponse<Void>> createNotification(
            @PathVariable String type,
            @RequestBody NotificationRequest request,
            @RequestHeader("memberId") Long memberId) {
        NotificationType notificationType = NotificationType.fromLabel(type); // Enum 변환 및 검증
        notificationService.createNotification(request, notificationType, memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @Operation(summary = "API 명세서 v0.4 line 85", description = "알림 조회")
    @GetMapping("/{lastId}")
    public ResponseEntity<ApiResponse<NotificationResponse>> getNotifications(
            @PathVariable @PositiveOrZero(message = "lastId는 0 이상이어야 합니다.") Long lastId,
            @RequestHeader("memberId") Long memberId) {
        NotificationResponse response = notificationService.getNotifications(memberId, lastId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "API 명세서 v0.4 line 86", description = "알림 읽음 처리")
    @PutMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long notificationId,
            @RequestHeader("memberId") Long memberId) {
        notificationService.markNotificationAsRead(notificationId, memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @Operation(summary = "API 명세서 v0.4 line 87", description = "알림 삭제")
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable Long notificationId,
            @RequestHeader("memberId") Long memberId) {
        notificationService.deleteNotification(notificationId, memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
}
