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
    private final MemberServiceFeignClient memberServiceFeignClient;

    public NotificationService(NotificationRepository notificationRepository, MemberServiceFeignClient memberServiceFeignClient) {
        this.notificationRepository = notificationRepository;
        this.memberServiceFeignClient = memberServiceFeignClient;
    }
    // TODO: 각 다른 유형의 알림을 전송받는 로직이 필요
    // 알림 생성
    @Transactional
    public void createNotification(NotificationRequest request, NotificationType type, Long senderMemberId) {
        // 알림 제목에 nickname을 받기 위한 로직
        String senderNickname = fetchSenderNicknameById(senderMemberId);
        String title = determineTitle(type, senderNickname);

        Notification notification = Notification.builder()
                .memberId(request.getMemberId())
                .title(title)
                .content(request.getContent())
                .url(request.getUrl())
                .type(type)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    // TODO: Member-service에 Nickname 조회하는 로직 구현 필요
    // TODO: Member-service에 feign 폴더를 만들어 feign/service, feign/controller를 만들어 그 곳에 로직을 구현할 예정
    //senderMemberId로 사용자 nickname 조회
    private String fetchSenderNicknameById(Long senderMemberId) {
        try {
            return memberServiceFeignClient.getNicknameById(senderMemberId);
        } catch (Exception e) {
            // 예외 처리: nickname 조회 실패 시 기본값 사용
            throw new ApiException(ApiCode.DB_ERROR);
        }
    }

    // 알림의 유형에 따른 제목 생성
    private String determineTitle(NotificationType type, String senderNickname) {
        switch (type) {
            case COMMENT:
                return String.format("%s님께서 회원님의 게시글에 새로운 댓글을 달았습니다.", senderNickname);
            case REPLY:
                return String.format("%s님께서 회원님의 댓글에 답글을 달았습니다.", senderNickname);
            case LIKE:
                return String.format("%s님께서 회원님의 게시글을 좋아합니다.", senderNickname);
            case FOLLOW:
                return String.format("%s님께서 회원님을 팔로우하였습니다.", senderNickname);
            case NOTICE:
                return "새 공지가 등록되었습니다.";
            case INQUIRY_REPLY:
                return "문의에 대한 답변이 도착했습니다.";
            default:
                return "새로운 알림이 있습니다.";
        }
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
