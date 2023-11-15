package com.shinsang.gameboard.board.dto.responseDto;

import com.shinsang.gameboard.board.model.Board;
import com.shinsang.gameboard.board.model.Category;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class BoardListResponseDto {
    private Long id;
    private Category category;
    private String title;
    private String username;
    private int views;
    private String content;
    private String image;
    private Timestamp registerAt;
    private Timestamp updatedAt;

    public BoardListResponseDto(Board board){
        this.title = board.getTitle();
        this.content = board.getContent();
        this.image = board.getImage();
    }
}
