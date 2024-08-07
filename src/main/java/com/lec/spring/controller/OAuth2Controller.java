package com.lec.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.domain.User;
import com.lec.spring.domain.oauth.KakaoOAuthToken;
import com.lec.spring.domain.oauth.KakaoProfile;
import com.lec.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import com.lec.spring.util.U;

@Controller
@RequestMapping("/oauth2")
public class OAuth2Controller {

    // Kakao 로그인
    @Value("${app.oauth2.kakao.client-id}")
    private String kakaoClientId;
    @Value("${app.oauth2.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    @Value("${app.oauth2.kakao.token-uri}")
    private String kakaoTokenUri;
    @Value("${app.oauth2.kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    @Value("${app.oauth2.password}")
    private String oauth2Password;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Kakao 가 보내준 code 값 받아옴
    @GetMapping("/kakao/callback")
    public String kakaoCallback(String code) {

        // 인가 code 확인 <- 인증완료
        System.out.println("\n<<카카오 인증 완료>>\ncode: " + code);

        // Access token 받아오기
        KakaoOAuthToken token = kakaoAccessToken(code);

        // 사용자 정보 요청 <- Access Token 사용
        KakaoProfile profile = kakaoUserInfo(token.getAccess_token());

        // 회원가입 진행<- KakaoProfile (사용자 정보) 사용
        User kakaoUser = registerKakaoUser(profile);

        // 사이트 로그인
        loginKakaoUser(kakaoUser);

        return "redirect:/travelkorea";

    }

    public KakaoOAuthToken kakaoAccessToken(String code) {

        RestTemplate rt = new RestTemplate();

        // header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        // body
        // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token-request-body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        // header, body 를 담은 HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // 요청
        // 카카오 API에 POST 요청을 보내고, 사용자 정보를 문자열 형태로 응답받을 수 있음.
        ResponseEntity<String> response = rt.exchange( // RestTemplate 의 exchange 메소드를 사용하여 HTTP 요청을 보냄
                kakaoTokenUri,  // Access Code 발급 URI
                HttpMethod.POST,    // POST 메소드를 사용하여 요청
                kakaoTokenRequest,    // 요청 엔티티, 요청의 헤더와 본문을 포함함.
                String.class
        );

        System.out.println("카카오 Access Token 응답: " + response.getBody());

        // JSON -> Java Object
        ObjectMapper mapper = new ObjectMapper();
        KakaoOAuthToken token = null;

        try {
            token = mapper.readValue(response.getBody(), KakaoOAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return token;
    }



    public KakaoProfile kakaoUserInfo(String accessToken) {
        RestTemplate rt = new RestTemplate();

        // header 준비
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        System.out.println(headers);

        // HttpEntity 요청
        HttpEntity<MultiValueMap> kakaoProfileRequest = new HttpEntity<>(headers);

        // 요청
        ResponseEntity<String> response = rt.exchange(
                kakaoUserInfoUri,
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        System.out.println("카카오 사용자 profile 요청 응답: " + response) ;
        System.out.println("카카오 사용자 profile 응답  body: " + response.getBody()) ;

        // 사용자 정보(JSON) -> Java 로 받아냄
        ObjectMapper mapper = new ObjectMapper();
        KakaoProfile profile = null;
        try {
            profile = mapper.readValue(response.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return profile;
    }

    // 카카오 계정으로 회원가입
    public User registerKakaoUser(KakaoProfile profile) {

        String provider = "KAKAO";
        String providerId = "" + profile.getId();
        String username = provider + "_" + providerId;
        String name = profile.getKakaoAccount().getProfile().getNickname();
        String password = oauth2Password;

        // 이미 가입된 회원인지 유부
        User user = userService.findByUsername(username);
        if(user == null) {
            User newUser = User.builder()
                    .username(username)
                    .name(name)
                    .password(password)
                    .provide(provider)
                    .provideId(providerId)
                    .build();

            int cnt = userService.register(newUser);    // 회원가입 -> 성공시 1 return
            if(cnt > 0) {
                user = userService.findByUsername(username);
                System.out.println("[카카오] 가입 성공");
            } else {
                System.out.println("[카카오] 가입 실패");
            }
        } else {
            System.out.println("[카카오] 이미 가입된 회원입니다");
        }


        return user;
    }



    // 카카오 아이디로 로그인
    public void loginKakaoUser(User kakaoUser) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( // 사용자 인증 정보를 담고 있는 객체
                kakaoUser.getUsername(),
                oauth2Password
        );

        System.out.println("Authentication token: " + authenticationToken);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);

        U.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
        System.out.println("카카오 로그인 완료");
    }




}
