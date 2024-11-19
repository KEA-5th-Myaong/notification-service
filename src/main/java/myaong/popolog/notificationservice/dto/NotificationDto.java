package myaong.popolog.notificationservice.dto;

import lombok.Data;
import myaong.popolog.notificationservice.entity.Notification;

import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private Long notificationId;
    private String title;
    private String content;
    private String url;
    private Boolean isRead;
    private LocalDateTime timestamp;

    // 생성자
    public NotificationDto(Long notificationId, String title, String content, String url, Boolean isRead, LocalDateTime timestamp) {
        this.notificationId = notificationId;
        this.title = title;
        this.content = content;
        this.url = url;
        this.isRead = isRead;
        this.timestamp = timestamp;
    }

    // 정적 팩토리 메서드
    public static NotificationDto fromEntity(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getUrl(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
}
