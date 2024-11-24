package myaong.popolog.notificationservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import myaong.popolog.notificationservice.constant.NotificationType;

@Getter
@Builder
public class NotificationRequest {
    private Long memberId;
    private String title;
    private String content;
    private String url;
    private NotificationType type;
}
