package com.shinsang.gameboard.board.dto.requestDto;

import com.shinsang.gameboard.board.model.Category;
import lombok.Getter;

@Getter
public class CreateBoardRequestDto {
    private Category category;
    private String username;
    private String password;
    private String passwordCheck;
    private String title;
    private String content;
    private String image;
}

