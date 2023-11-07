package com.shinsang.gameboard;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() where board_id=?;")
@Where(clause = "deleted_at is NULL")
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username; //email 아이디

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String userImage;

    @Column(nullable = false)
    private Long kakaoId;

    @Column(nullable = false)
    private LocalDateTime userLastLogin; // 마지막 접속 시간

    @Column(nullable = false)
    @CreatedDate
    private Timestamp registerAt;  // 가입 날짜

    @Column(name = "updated_at")
    private Timestamp updatedAt;  // 프로필 수정

    @Column(name = "deleted_at")
    private Timestamp deletedAt; // 회원 탈퇴

    public User(String username, String nickname, String userImage, Long kakaoId, LocalDateTime userLastLogin) {
        this.username = username;
        this.nickname = nickname;
        this.userImage = userImage;
        this.kakaoId = kakaoId;
        this.userLastLogin = userLastLogin;
    }
    @PrePersist
    void registeredAt(){
        this.registerAt = Timestamp.from(Instant.now());
    }
    @PreUpdate
    void updatedAt(){
        this.updatedAt = Timestamp.from(Instant.now());
    }
}
