package com.study.Jpawebapp.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@EqualsAndHashCode(of ="id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    // email, nickname은 중복이 되지 않으므로 unique해야 함
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime joinedAt;

    private String bio;

    private String url;

    private String occupation;

    private String location;

//  varchar(255)보다 더 길어질 수 있을 때 @Lob -> text type에 매핑 시켜줌
//  profile image는 user을 로딩 할 떄 종종 같이 쓰일 것이기 때문에 fetch모드로 가져오는게 좋음
    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;


//  알림
    private boolean studyCreatedByEmail;
    private boolean studyCreatedByWeb;
    private boolean studyEnrollmentResultByEmail;
    private boolean studyEnrollmentResultByWeb;
    private boolean studyUpdatedByEmail;
    private boolean studyUpdatedByWeb;


    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }
}
