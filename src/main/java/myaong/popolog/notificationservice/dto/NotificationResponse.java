package myaong.popolog.notificationservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class NotificationResponse {
    private Long lastId;
    private List<NotificationDto> notifications;

    // 생성자
    public NotificationResponse(Long lastId, List<NotificationDto> notifications) {
        this.lastId = lastId;
        this.notifications = notifications;
    }
}
