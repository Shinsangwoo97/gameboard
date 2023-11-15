package com.shinsang.gameboard.board.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE board SET deleted_at = NOW() where board_id=?;")
@Where(clause = "deleted_at is NULL")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, name = "category")
    private Category category;
    @Column(nullable = false, name = "user_name")
    private String username;
    @Column(nullable = false, name = "password")
    private String password;
    @Column(nullable = false, name = "title")
    private String title;
    @Column(nullable = false, name = "content")
    private String content;
    @OneToMany(mappedBy = "boardId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Attachment> attachments;
    @Column(name = "image")
    private String image;
    @Column(name = "views")
    private int views = 0;
    @Column(name = "register_at")
    private Timestamp registerAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    public Board(Category category, String username, String password, String title, String content) {
        this.category = category;
        this.username = username;
        this.password = password;
        this.title = title;
        this.content = content;
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
