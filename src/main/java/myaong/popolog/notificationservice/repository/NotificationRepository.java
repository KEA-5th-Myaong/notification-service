package myaong.popolog.notificationservice.repository;

import myaong.popolog.notificationservice.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByMemberIdAndIdGreaterThanOrderByIdAsc(Long memberId, Long lastId, Pageable pageable);
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :notificationId")
    void markAsRead(Long notificationId);

    // 30일이 지난 알림 삭제
    void deleteByCreatedAtBefore(LocalDateTime threshold);
}
