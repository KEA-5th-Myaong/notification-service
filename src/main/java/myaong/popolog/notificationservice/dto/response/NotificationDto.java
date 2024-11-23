package myaong.popolog.notificationservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import myaong.popolog.notificationservice.constant.NotificationType;
import myaong.popolog.notificationservice.entity.Notification;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationDto {
    private final Long notificationId;
    private final String title;
    private final String content;
    private final String url;
    private final Boolean isRead;
    private final NotificationType type;
    private final LocalDateTime timestamp;

    // 정적 팩토리 메서드
    public static NotificationDto fromEntity(Notification notification) {
        return NotificationDto.builder()
                .notificationId(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .url(notification.getUrl())
                .isRead(notification.getIsRead())
                .type(notification.getType())
                .timestamp(notification.getCreatedAt())
                .build();
    }
}
