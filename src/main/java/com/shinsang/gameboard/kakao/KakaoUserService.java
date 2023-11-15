package com.shinsang.gameboard.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinsang.gameboard.configuration.JwtTokenUtils;
import com.shinsang.gameboard.user.User;
import com.shinsang.gameboard.user.UserDetailsImpl;
import com.shinsang.gameboard.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
@Slf4j
@Service
public class KakaoUserService {

    @Value("${REST_API_KEY}")
    private String REST_API_KEY;
    @Value("${REDIRECT_URL}")
    private String REDIRECT_URL;

    @Autowired
    private UserRepository userRepository;

    public SocialLoginInfoDto kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. 인가코드로 엑세스토큰 가져오기
        String accessToken = getAccessToken(code);
        // 2. 엑세스토큰으로 유저정보 가져오기
        SocialLoginInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
        // 3. 유저확인 & 회원가입
        User foundUser = kakaoUserCheckAndRegister(kakaoUserInfo);
        // 4. 시큐리티 강제 로그인
        Authentication authentication = securityLogin(foundUser);
        // 5. jwt 토큰 발급
        jwtToken(response, authentication);
        return kakaoUserInfo;
    }

    // #1 - 인가코드로 엑세스토큰 가져오기
    private String getAccessToken(String code) {

        // 헤더에 Content-type 지정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 바디에 필요한 정보 담기
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", REST_API_KEY);
        params.add("redirect_uri", REDIRECT_URL);
        params.add("code", code);
//        params.add("client_secret", "authorization_code"); 필수는 아니지만 보안강화 할떄 필요함
        // POST 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenReg = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenReg,
                String.class
        );
        System.out.println("리스폰스111"+response);

        // response에서 엑세스토큰 가져오기
        String tokenJson = response.getBody();
        System.out.println("토큰 제이슨"+tokenJson);
        JSONObject jsonObject = new JSONObject(tokenJson);
        String accessToken = jsonObject.getString("access_token");
        System.out.println("토큰" + accessToken);
        return accessToken;
    }

    // #2. 엑세스토큰으로 유저정보 가져오기
    private SocialLoginInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        System.out.println("포스트 보낸후"+response);
        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
//        System.out.println("jsonNode: " + jsonNode); // 무슨 값이 들어오나 체크

        // jsonNode 체크후 필요한 정보 가져오기 (추가 가능)
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String userimage = jsonNode.get("properties")
                .get("profile_image").asText();
        String email = (jsonNode.get("kakao_account")
                .get("email") != null) ? jsonNode.get("kakao_account")
                .get("email").asText() : null;
        System.out.println("id: " + id);
        System.out.println("nickname: " + nickname);
        System.out.println("userimage: " + userimage);
        System.out.println("email: " + email);
        return new SocialLoginInfoDto(id, nickname, email, userimage);
    }
    // #3.  가입된 유저확인 & 회원가입
    private User kakaoUserCheckAndRegister(SocialLoginInfoDto kakaoLoginInfoDto) {

        String username = kakaoLoginInfoDto.getEmail();
        String nickname = kakaoLoginInfoDto.getNickname();
        String userImage = kakaoLoginInfoDto.getProfileImgUrl();
        System.out.println(userImage);
        Long kakaoId = kakaoLoginInfoDto.getId();
        LocalDateTime now = LocalDateTime.now();

        User kakaoUser = userRepository.findByUsername(username)
                .orElse(null);
        if(kakaoUser == null) {
            kakaoUser = new User(username, nickname, userImage, kakaoId, now);
            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }
    // 4. 시큐리티 강제 로그인
    private Authentication securityLogin(User findUser) {

        // userDetails 생성
        UserDetailsImpl userDetails = new UserDetailsImpl(findUser);
        log.warn("카카오 로그인 완료 : " + userDetails.getUser().getUsername());
        // UsernamePasswordAuthenticationToken 발급
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        // 강제로 시큐리티 세션에 접근하여 authentication 객체를 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
    // 5. jwt 토큰 발급
    private void jwtToken(HttpServletResponse response, Authentication authentication) {

        UserDetailsImpl userDetailsImpl = ((UserDetailsImpl) authentication.getPrincipal());
        String token = JwtTokenUtils.generateJwtToken(userDetailsImpl);
        response.addHeader("Authorization", token);
//        response.addHeader("Authorization",token);
    }
}
