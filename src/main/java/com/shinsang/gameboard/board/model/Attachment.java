package com.shinsang.gameboard.board.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // 파일 번호 (PK)
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board boardId;           // 게시글 번호 (FK)
    @Column(nullable = false)
    private String originalName;    // 원본 파일명
    @Column(nullable = false)
    private String saveName;        // 저장 파일명
    @Column(nullable = false)
    private long size;              // 파일 크기
}
