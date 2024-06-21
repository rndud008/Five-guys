package com.lec.spring.domain.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class KakaoProfile {

    public Long id;
    @JsonProperty("connected_at")
    public String connectedAt;
    public Properties properties;
    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;

    @Data
    public static class KakaoAccount {
        @JsonProperty("profile_nickname_needs_agreement")
        public Boolean profileNicknameNeedsAgreement;
        @JsonProperty("profile_image_needs_agreement")
        public Boolean profileImageNeedsAgreement;
        public Profile profile;

        @Data
        public static class Profile {

            public String nickname;
            @JsonProperty("is_default_nickname")
            public Boolean isDefaultNickname;
        }
    }

    @Data
    public static class Properties {

        public String nickname;
    }


}
