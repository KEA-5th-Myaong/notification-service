package myaong.popolog.notificationservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`notification`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "noti_id")
	private Long id;

	// 대상 회원
	@Column(name = "member_id", nullable = false, updatable = false)
	private Long memberId;

	// 알림 요약
	@Column(name = "title", nullable = false, updatable = false)
	private String title;

	// 상세 내용
	@Column(name = "content", nullable = false, updatable = false)
	private String content;

	// 알림 클릭 시 연결 주소
	@Column(name = "url", nullable = false, updatable = false)
	private String url;

	// 읽음 여부
	@Column(name = "is_read", nullable = false)
	private Boolean isRead;

	@Builder
	public Notification(Long memberId, String title, String content, String url, Boolean isRead) {
		this.memberId = memberId;
		this.title = title;
		this.content = content;
		this.url = url;
		this.isRead = isRead;
	}
}
