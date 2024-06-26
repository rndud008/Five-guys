package com.lec.spring.config.oauth;


import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.config.oauth.provider.GoogleUserInfo;
import com.lec.spring.config.oauth.provider.NaverUserInfo;
import com.lec.spring.config.oauth.provider.OAuth2UserInfo;
import com.lec.spring.domain.User;
import com.lec.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

// oauth 인증 후 동작
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserService userService;

    @Value("${app.oauth2.password}")
    private String oauth2Password;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("Oauth2 loadUser() 호출");

        OAuth2User oAuth2User = super.loadUser(userRequest);    // 사용자 프로필 정보 가져오기

        System.out.println("""
                ClientRegistration: %s
                RegistrationId: %s
                AccessToken: %s
                OAuth2User Attributes: $s
                """.formatted(
                        userRequest.getClientRegistration()
                        , userRequest.getClientRegistration().getRegistrationId()
                        , userRequest.getAccessToken().getTokenValue()
                        , oAuth2User.getAttributes()
        ));

        // 회원가입
        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = switch (provider.toLowerCase()) {
            case "naver" -> new NaverUserInfo(oAuth2User.getAttributes());
            case "google" -> new GoogleUserInfo(oAuth2User.getAttributes());
            // todo
            default -> null;
        };

        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = oauth2Password;
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();

        // 회원가입 전, 이미 회원인지 확인 후 진행
        User user = userService.findByUsername(username);
        if (user == null) {
           User newUser = User.builder()
                   .username(username)
                   .name(name)
                   .email(email)
                   .password(password)
                   .provide(provider)
                   .provideId(providerId)
                   .build();

           int cnt = userService.register(newUser);
           if (cnt > 0) {
               user = userService.findByUsername(username);
               System.out.println("[OAuth] 인증. 화원가입 완료.");
           } else {
               System.out.println("[OAuth] 회원가입 실패");
           }
        } else {
            System.out.println("[OAuth] 이미 가입된 회원");
        }

        PrincipalDetails principalDetails = new PrincipalDetails(user, oAuth2User.getAttributes());
        principalDetails.setUserService(userService);
        return principalDetails;
    }

}
