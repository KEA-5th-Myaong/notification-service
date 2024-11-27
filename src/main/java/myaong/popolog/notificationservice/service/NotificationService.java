package myaong.popolog.notificationservice.service;

import myaong.popolog.notificationservice.common.exception.ApiCode;
import myaong.popolog.notificationservice.common.exception.ApiException;
import myaong.popolog.notificationservice.constant.NotificationType;
import myaong.popolog.notificationservice.dto.request.NotificationRequest;
import myaong.popolog.notificationservice.dto.response.NotificationDto;
import myaong.popolog.notificationservice.dto.response.NotificationResponse;
import myaong.popolog.notificationservice.entity.Notification;
import myaong.popolog.notificationservice.feign.client.MemberServiceFeignClient;
import myaong.popolog.notificationservice.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
    // TODO: 각 다른 유형의 알림을 전송받는 로직이 필요
    // 알림 생성
    @Transactional
    public void createNotification(NotificationRequest request, NotificationType type, Long memberId) {
        Notification notification = Notification.builder()
                .memberId(request.getMemberId())
                .title(request.getTitle())
                .content(request.getContent())
                .url(request.getUrl())
                .type(type)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }


    // 알림 조회
    @Transactional(readOnly = true)
    public NotificationResponse getNotifications(Long memberId, Long lastId) {

        // 10개씩 페이징 처리
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notification> notificationPage = notificationRepository.findByMemberIdAndIdGreaterThanOrderByIdAsc(memberId, lastId, pageable);

        Long nextLastId = notificationPage.hasContent()
                ? notificationPage.getContent().get(notificationPage.getContent().size() - 1).getId()
                : -1;

        // NotificationResponse 빌더를 활용하여 반환
        return NotificationResponse.of(
                nextLastId,
                notificationPage.stream()
                        .map(NotificationDto::fromEntity)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void markNotificationAsRead(Long notificationId, Long memberId) {
        // 알림 조회
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ApiException(ApiCode.INVALID_DATA));
        // memberId 검증
        if (!notification.getMemberId().equals(memberId)) {
            throw new ApiException(ApiCode.METHOD_NOT_ALLOWED);
        }
        notificationRepository.markAsRead(notificationId);
    }

    @Transactional
    public void deleteNotification(Long notificationId, Long memberId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ApiException(ApiCode.INVALID_DATA));
        if (!notification.getMemberId().equals(memberId)) {
            throw new ApiException(ApiCode.METHOD_NOT_ALLOWED);
        }
        notificationRepository.deleteById(notificationId);
    }

    // 30일 지난 알림 삭제
    @Transactional
    public void deleteOldNotifications() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        notificationRepository.deleteByCreatedAtBefore(threshold);
    }
}
