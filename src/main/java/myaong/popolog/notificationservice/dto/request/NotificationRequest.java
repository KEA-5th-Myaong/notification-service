package myaong.popolog.notificationservice.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationRequest {
    private Long memberId;
    private String title;
    private String content;
    private String url;
}
