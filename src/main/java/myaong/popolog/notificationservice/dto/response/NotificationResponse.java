package myaong.popolog.notificationservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NotificationResponse {
    private final Long lastId;
    private final List<NotificationDto> notifications;

    // 정적 팩토리 메서드
    public static NotificationResponse of(Long lastId, List<NotificationDto> notifications) {
        return NotificationResponse.builder()
                .lastId(lastId)
                .notifications(notifications)
                .build();
    }
}
