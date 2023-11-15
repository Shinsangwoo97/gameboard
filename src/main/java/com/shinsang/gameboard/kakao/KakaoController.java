package com.shinsang.gameboard.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinsang.gameboard.exceptionHandler.CustomException;
import com.shinsang.gameboard.exceptionHandler.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoUserService kakaoUserService;

    ///oauth/kakao
    @GetMapping("/oauth/kakao")
    public ResponseEntity kakaoCallback(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        try {
            kakaoUserService.kakaoLogin(code, response);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD, "");
        }

        return  new ResponseEntity("카카오 사용자로 로그인 처리 완료", HttpStatus.OK);
    }
}
