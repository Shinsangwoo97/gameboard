package com.shinsang.gameboard.board.dto.responseDto;

import com.shinsang.gameboard.board.model.Attachment;
import com.shinsang.gameboard.board.model.Category;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
public class MainPageResponseDto {
    private Long boardId;
    private Category category;
    private List<Attachment> attachments;
    private String title;
    private String username;
    private int views;
    private Timestamp registerAt;
    private Timestamp updatedAt;

}
