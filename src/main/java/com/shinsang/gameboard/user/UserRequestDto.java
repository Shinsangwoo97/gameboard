package com.shinsang.gameboard.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDto {
    private Long userId;
    private String username;
    private String nickname;
    private String imageUrl;

    public static UserRequestDto fromUser(User user) {

        UserRequestDto requestDto = new UserRequestDto();

        requestDto.userId = user.getId();
        requestDto.username = user.getUsername();
        requestDto.nickname = user.getNickname();
        requestDto.imageUrl = user.getUserImage();

        return requestDto;
    }
}
