package com.adoptpet.server.commons.notification.domain;

import com.adoptpet.server.commons.support.BaseTimeEntity;

import com.adoptpet.server.user.domain.Member;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Entity
@Table(name = "NOTIFICATION")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @Column(name = "noti_No")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notiNo;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private NotifiTypeEnum type;

    @Column(name = "url")
    private String Url;

    @Column(name = "content")
    private String content;

    @Column(name = "delete_status")
    private boolean isDel;
    @Column(name = "read_status")
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    public void read() {
        this.isRead = true;
    }
}
