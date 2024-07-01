package com.lec.spring.config;

import com.lec.spring.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //OAuth2 Client
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http

                .csrf(csrf -> csrf.disable())   // CSRF(Cross-site Request Forgery: 사이트 간 요청 위조)

                /**
                 * request URL 에 대한 접근 권한 세팅
                 */
                .authorizeHttpRequests(auth -> auth
                        // URL 과 접근권한 세팅(들)
                        // ↓ /board/detail/** URL로 들어오는 요청은 '인증'만 필요.
                        .requestMatchers("/board/detail/**").authenticated()
                        // ↓ "/board/write/**", "/board/update/**", "/board/delete/**" URL로 들어오는 요청은 '인증' 뿐 아니라 ROLE_MEMBER 나 ROLE_ADMIN 권한을 갖고 있어야 한다. ('인가')
                        .requestMatchers("/board/write/**", "/board/update/**", "/board/delete/**").hasAnyRole("MEMBER", "ADMIN")
                        // 그 외 request 는 모두 permit
                        .anyRequest().permitAll()
                )

                /**
                 *  로그인 설정
                 */
                .formLogin(form -> form
//                        .loginPage("/user/login")   // 로그인 필요한 상황 발생시 "/user/login" 으로 request 요청
                        .loginPage("/fragment/navbar")   // 로그인 필요한 상황 발생시 "/user/login" 으로 request 요청
//                        .loginProcessingUrl("/user/login")  // "/user/login" url 로 POST request 가 들어오면 Security 가 fetch 하여 처리. "인증" 과정
                        .loginProcessingUrl("/fragment/navbar")  // "/user/login" url 로 POST request 가 들어오면 Security 가 fetch 하여 처리. "인증" 과정
                        .defaultSuccessUrl("/") // 이전 페이지에서 로그인 요청하여 로그인 성공 시, 해당 페이지로 다시 이동
                        .successHandler(new CustomLoginSuccessHandler("/board/list"))
                        .failureHandler(new CustomLoginFailureHandler())
                )

                /**
                 * 로그아웃 설정
                 */
                // ※ 아래 설정 없이도 기본적으로 /logout 으로 로그아웃 된다
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                                .logoutUrl("/fragment/logout")  // 로그아웃 수행 url

                                //                              .logoutSuccessUrl("/home")  // 로그아웃 성공후 redirect url

                                .invalidateHttpSession(false)   // session invalidate 수행안함
                                // 이따가 CustomLogoutSuccessHandler 에서 꺼낼 정보가 있기 때문에
                                // false 로 세팅한다

                                // 로그아웃 성공후 수행할 코드
                                // .logoutSuccessHandler(LogoutSuccessHandler)
                                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                )
                /**
                 * 예외처리 설정
                 */
                // 로그인하지 않은 client 가 게시물 페이지 접근시 수행
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )


                /**
                 * OAuth2 로그인
                 */
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .loginPage("/fragment/navbar")
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(principalOauth2UserService)))

                /**
                 * - iframe 사용시
                 * Spring security 에선 해킹 등 을 방지하기위해 (Click Jacking) 막는 설정이 (deny) 되있는데,
                 * X-Frame-options 에서 동일 출처에서는 사용 가능하게끔 풀어주는 설정 코드 ↓
                 **/
                .headers(httpSecurityHeadersConfigurer -> {
                    httpSecurityHeadersConfigurer.frameOptions(frameOptionsConfig -> {
                        frameOptionsConfig.sameOrigin();
                    });
                })

                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
