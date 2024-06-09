
CREATE TABLE areacode
(
  id   INT         NOT NULL AUTO_INCREMENT COMMENT '지역id',
  name VARCHAR(20) NOT NULL COMMENT '지역이름',
  PRIMARY KEY (id)
) COMMENT '지역(areacode)';

CREATE TABLE attachment
(
  id                   INT          NOT NULL AUTO_INCREMENT,
  travel_diary_post_id INT          NOT NULL COMMENT '게시판번호',
  sourcename           VARCHAR(100) NOT NULL COMMENT '원본파일이름',
  filename             VARCHAR(100) NOT NULL COMMENT '파일이름',
  PRIMARY KEY (id)
) COMMENT '파일업로드';

CREATE TABLE authority
(
  id   INT         NOT NULL AUTO_INCREMENT COMMENT '권한번호',
  name VARCHAR(40) NOT NULL COMMENT '권한이름',
  PRIMARY KEY (id)
) COMMENT '권한';

ALTER TABLE authority
  ADD CONSTRAINT UQ_name UNIQUE (name);

CREATE TABLE blog_reivew
(
  id               INT                         NOT NULL AUTO_INCREMENT COMMENT '블로그 리뷰id',
  tourist_post_id  INT                         NULL     COMMENT '여행 정보 목록 id',
  festival_post_id INT                         NULL     COMMENT '여행 축제 정보 목록 id',
  last_call_api_id INT                         NOT NULL COMMENT 'API호출id',
  title            VARCHAR(50)                 NOT NULL COMMENT '블로그 제목',
  overview         VARCHAR(100)                NOT NULL COMMENT '블로그상세내용',
  writedate        VARCHAR(30)                 NULL     COMMENT '블로그 작성일',
  url              VARCHAR(255)                NULL     COMMENT '블로그URL',
  review_type      ENUM('tourist', 'festival') NOT NULL COMMENT '리뷰 타입 에 따라 외래키로 지정한 festival, tourist 를 not null 설정',
  PRIMARY KEY (id)
) COMMENT '블로그 리뷰 리스트?';

CREATE TABLE comment
(
  id                   INT      NOT NULL AUTO_INCREMENT COMMENT '댓글번호?',
  user_id              INT      NOT NULL COMMENT '회원번호',
  travel_diary_post_id INT      NOT NULL COMMENT '게시판번호',
  content              TEXT     NOT NULL COMMENT '댓글내용',
  regdate              DATETIME NULL     DEFAULT now() COMMENT '작성일',
  comment_like_count   INT      NULL     DEFAULT 0 COMMENT '댓글 좋아요',
  PRIMARY KEY (id)
) COMMENT '댓글';

CREATE TABLE festival_large_class
(
  id   INT         NOT NULL AUTO_INCREMENT COMMENT '축제 대분류 id',
  name VARCHAR(20) NULL     COMMENT '축제정보 대분류이름',
  PRIMARY KEY (id)
) COMMENT '축제 정보 유형 대분류';

CREATE TABLE festival_middle_class
(
  id                INT         NOT NULL AUTO_INCREMENT COMMENT '축제 중분류 id',
  festival_large_id INT         NOT NULL COMMENT '축제 대분류 id',
  name              VARCHAR(20) NULL     COMMENT '축제정보 중분류이름',
  PRIMARY KEY (id)
) COMMENT '축제 정보 유형 중분류';

CREATE TABLE festival_post
(
  id                  INT          NOT NULL AUTO_INCREMENT COMMENT '여행 축제 정보 목록 id',
  user_id             INT          NOT NULL COMMENT '회원번호',
  areacode_id         INT          NOT NULL COMMENT '지역id',
  sigungucode_id      INT          NULL     COMMENT '시군구id',
  festival_large_id   INT          NOT NULL COMMENT '축제 대분류 id',
  festival_middle_id  INT          NULL     COMMENT '축제 중분류 id',
  festival_small_id   INT          NULL     COMMENT '축제 소분류 id',
  last_call_api_id    INT          NOT NULL COMMENT 'API호출id',
  title               VARCHAR(200) NULL     COMMENT '제목',
  address             VARCHAR(200) NULL     COMMENT '주소',
  image               VARCHAR(200) NULL     COMMENT '이미지',
  home_url            VARCHAR(100) NULL     COMMENT '홈페이지url',
  overview            LONGTEXT     NULL     COMMENT '세부내용',
  a                   VARCHAR(100) NULL     COMMENT '문의 및 안내',
  holiday             VARCHAR(20)  NULL     COMMENT '휴일',
  b                   VARCHAR(100) NULL     COMMENT '이용시간',
  c                   VARCHAR(100) NULL     COMMENT '주차장',
  festival_like_count INT          NULL     COMMENT '축제 정보 좋아요',
  PRIMARY KEY (id)
) COMMENT '여행 축제 정보 목록';

CREATE TABLE festival_post_user
(
  festival_post_id INT NOT NULL COMMENT '여행 축제 정보 목록 id',
  user_id          INT NOT NULL COMMENT '회원번호',
  PRIMARY KEY (festival_post_id, user_id)
) COMMENT '여행 축제 정보 좋아요';

CREATE TABLE festival_small_class
(
  id                 INT         NOT NULL AUTO_INCREMENT COMMENT '축제 소분류 id',
  festival_middle_id INT         NOT NULL COMMENT '축제 중분류 id',
  name               VARCHAR(20) NULL     COMMENT '축제정보 소분류이름',
  PRIMARY KEY (id)
) COMMENT '축제 정보 유형 소분류';

CREATE TABLE last_call_api_date
(
  id        INT          NOT NULL AUTO_INCREMENT COMMENT 'API호출id',
  apiname   VARCHAR(255) NULL     COMMENT '호출한 정보 타이틀?',
  regdate   DATE         NULL     DEFAULT now() COMMENT '호출 날짜',
  r_code    VARCHAR(10)  NULL     COMMENT 'response code',
  r_message VARCHAR(255) NULL     COMMENT 'response message',
  PRIMARY KEY (id)
) COMMENT 'api 중복호출 방지';

CREATE TABLE sigungucode
(
  id          INT         NOT NULL AUTO_INCREMENT COMMENT '시군구id',
  areacode_id INT         NOT NULL COMMENT '지역id',
  name        VARCHAR(20) NOT NULL COMMENT '시군구 이름',
  PRIMARY KEY (id)
) COMMENT '시군구(sigungucode)';

CREATE TABLE tourist_large_class
(
  id   INT         NOT NULL AUTO_INCREMENT COMMENT '관광정보 대분류 id',
  name VARCHAR(20) NULL     COMMENT '관광정보 대분류 이름',
  PRIMARY KEY (id)
) COMMENT '관광정보 유형 대분류';

CREATE TABLE tourist_middle_class
(
  id               INT         NOT NULL AUTO_INCREMENT COMMENT '관광정보 중분류 id',
  tourist_large_id INT         NOT NULL COMMENT '관광정보 대분류 id',
  name             VARCHAR(20) NULL     COMMENT '관광정보 중분류 이름',
  PRIMARY KEY (id)
) COMMENT '관광정보 유형 중분류';

CREATE TABLE tourist_post
(
  id                 INT          NOT NULL AUTO_INCREMENT COMMENT '여행 관광지 정보 목록 id',
  user_id            INT          NOT NULL COMMENT '회원번호',
  areacode_id        INT          NOT NULL COMMENT '지역id',
  sigungucode_id     INT          NULL     COMMENT '시군구id',
  tourist_large_id   INT          NOT NULL COMMENT '관광정보 대분류 id',
  tourist_middle_id  INT          NULL     COMMENT '관광정보 중분류 id',
  tourist_small_id   INT          NULL     COMMENT '관광정보 소분류 id',
  last_call_api_id   INT          NOT NULL COMMENT 'API호출id',
  title              VARCHAR(200) NOT NULL COMMENT '제목',
  address            VARCHAR(200) NULL     COMMENT '주소',
  image              VARCHAR(200) NULL     COMMENT '이미지',
  home_url           VARCHAR(100) NULL     COMMENT '홈페이지url',
  overview           LONGTEXT     NULL     COMMENT '세부내용',
  a                  VARCHAR(100) NULL     COMMENT '문의 및 안내',
  holiday            VARCHAR(20)  NULL     COMMENT '휴일',
  b                  VARCHAR(100) NULL     COMMENT '이용시간',
  c                  VARCHAR(100) NOT NULL COMMENT '주차장',
  tourist_like_count INT          NULL     COMMENT '관광지 정보 좋아요',
  PRIMARY KEY (id)
) COMMENT '여행 관광지 정보 목록';

CREATE TABLE tourist_post_user
(
  tourist_post_id INT NOT NULL COMMENT '여행 관광지 정보 목록 id',
  user_id         INT NOT NULL COMMENT '회원번호',
  PRIMARY KEY (tourist_post_id, user_id)
) COMMENT '여행 관광지 정보 좋아요';

CREATE TABLE tourist_small_class
(
  id                INT         NOT NULL AUTO_INCREMENT COMMENT '관광정보 소분류 id',
  tourist_middle_id INT         NOT NULL COMMENT '관광정보 중분류 id',
  name              VARCHAR(20) NULL     COMMENT '관광정보 소분류 이름',
  PRIMARY KEY (id)
) COMMENT '관광정보 유형 소분류';

CREATE TABLE travel_diary_post
(
  id              INT          NOT NULL AUTO_INCREMENT COMMENT '게시판번호',
  user_id         INT          NOT NULL COMMENT '회원번호',
  subject         VARCHAR(200) NOT NULL COMMENT '제목',
  content         LONGTEXT     NULL     COMMENT '내용',
  viewcnt         int          NULL     DEFAULT 0 COMMENT '조회수',
  regdate         DATETIME     NULL     DEFAULT now() COMMENT '작성일',
  post_like_count INT          NULL     DEFAULT 0 COMMENT '게시글 좋아요',
  PRIMARY KEY (id)
) COMMENT '게시판';

CREATE TABLE user
(
  id        INT          NOT NULL AUTO_INCREMENT COMMENT '회원번호',
  username  VARCHAR(100) NOT NULL COMMENT '아이디',
  password  VARCHAR(100) NOT NULL COMMENT '비밀번호',
  name      VARCHAR(80)  NOT NULL COMMENT '이름',
  email     VARCHAR(80)  NULL     COMMENT '이메일',
  regdate   DATETIME     NULL     DEFAULT now() COMMENT '가입일',
  provide   VARCHAR(30)  NULL     COMMENT 'OAuth 가입처',
  provideId VARCHAR(200) NULL     COMMENT 'OAuth id(PK)',
  PRIMARY KEY (id)
) COMMENT '회원가입';

ALTER TABLE user
  ADD CONSTRAINT UQ_username UNIQUE (username);

CREATE TABLE user_authorities
(
  user_id      INT NOT NULL COMMENT '회원번호',
  authority_id INT NOT NULL COMMENT '권한번호',
  PRIMARY KEY (user_id, authority_id)
);

CREATE TABLE user_comment
(
  user_id    INT NOT NULL COMMENT '회원번호',
  comment_id INT NOT NULL COMMENT '댓글번호?',
  PRIMARY KEY (user_id, comment_id)
) COMMENT '댓글 좋아요';

CREATE TABLE user_post
(
  user_id              INT NOT NULL COMMENT '회원번호',
  travel_diary_post_id INT NOT NULL COMMENT '게시판번호',
  PRIMARY KEY (user_id, travel_diary_post_id)
) COMMENT '게시글 좋아요';

CREATE TABLE weather_1
(
  id               INT         NOT NULL AUTO_INCREMENT COMMENT '초단기날씨 id',
  last_call_api_id INT         NOT NULL COMMENT 'API호출id',
  areacode_id      INT         NOT NULL COMMENT '지역id',
  date             DATE        NULL     COMMENT '날짜',
  a                VARCHAR(20) NULL     COMMENT '최저기온',
  b                VARCHAR(20) NULL     COMMENT '최고기온',
  c                VARCHAR(20) NULL     COMMENT '강수확률',
  d                VARCHAR(20) NULL     COMMENT '맑음 or  흐림?',
  PRIMARY KEY (id)
) COMMENT '초단기날씨  당일 1시간 단위?';

CREATE TABLE weather_2
(
  id               INT         NOT NULL AUTO_INCREMENT COMMENT '딘기날씨 id',
  last_call_api_id INT         NOT NULL COMMENT 'API호출id',
  areacode_id      INT         NOT NULL COMMENT '지역id',
  date             DATE        NULL     COMMENT '날짜',
  a                VARCHAR(20) NULL     COMMENT '최저 기온',
  b                VARCHAR(20) NULL     COMMENT '최고 기온',
  c                VARCHAR(20) NULL     COMMENT '강수확률?',
  d                VARCHAR(20) NULL     COMMENT '맑음or 흐림?',
  PRIMARY KEY (id)
) COMMENT '단기날씨 2~3일째 날씨';

CREATE TABLE weather_3
(
  id               INT         NOT NULL AUTO_INCREMENT COMMENT '중기날씨 id',
  last_call_api_id INT         NOT NULL COMMENT 'API호출id',
  areacode_id      INT         NOT NULL COMMENT '지역id',
  date             DATE        NULL     COMMENT '날짜',
  a                VARCHAR(20) NULL     COMMENT '최저기온',
  b                VARCHAR(20) NULL     COMMENT '최고기온',
  c                VARCHAR(20) NULL     COMMENT '강수확률',
  d                VARCHAR(20) NULL     COMMENT '맑음or  흘림?',
  PRIMARY KEY (id)
) COMMENT '중기날씨 4일~7일째 날씨';

ALTER TABLE user_authorities
  ADD CONSTRAINT FK_user_TO_user_authorities
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE user_authorities
  ADD CONSTRAINT FK_authority_TO_user_authorities
    FOREIGN KEY (authority_id)
    REFERENCES authority (id);

ALTER TABLE travel_diary_post
  ADD CONSTRAINT FK_user_TO_travel_diary_post
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE comment
  ADD CONSTRAINT FK_user_TO_comment
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE comment
  ADD CONSTRAINT FK_travel_diary_post_TO_comment
    FOREIGN KEY (travel_diary_post_id)
    REFERENCES travel_diary_post (id);

ALTER TABLE attachment
  ADD CONSTRAINT FK_travel_diary_post_TO_attachment
    FOREIGN KEY (travel_diary_post_id)
    REFERENCES travel_diary_post (id);

ALTER TABLE user_comment
  ADD CONSTRAINT FK_user_TO_user_comment
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE user_comment
  ADD CONSTRAINT FK_comment_TO_user_comment
    FOREIGN KEY (comment_id)
    REFERENCES comment (id);

ALTER TABLE user_post
  ADD CONSTRAINT FK_user_TO_user_post
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE user_post
  ADD CONSTRAINT FK_travel_diary_post_TO_user_post
    FOREIGN KEY (travel_diary_post_id)
    REFERENCES travel_diary_post (id);

ALTER TABLE tourist_post
  ADD CONSTRAINT FK_user_TO_tourist_post
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE sigungucode
  ADD CONSTRAINT FK_areacode_TO_sigungucode
    FOREIGN KEY (areacode_id)
    REFERENCES areacode (id);

ALTER TABLE blog_reivew
  ADD CONSTRAINT FK_tourist_post_TO_blog_reivew
    FOREIGN KEY (tourist_post_id)
    REFERENCES tourist_post (id);

ALTER TABLE tourist_post_user
  ADD CONSTRAINT FK_tourist_post_TO_tourist_post_user
    FOREIGN KEY (tourist_post_id)
    REFERENCES tourist_post (id);

ALTER TABLE tourist_post_user
  ADD CONSTRAINT FK_user_TO_tourist_post_user
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE festival_post
  ADD CONSTRAINT FK_user_TO_festival_post
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE festival_post_user
  ADD CONSTRAINT FK_festival_post_TO_festival_post_user
    FOREIGN KEY (festival_post_id)
    REFERENCES festival_post (id);

ALTER TABLE festival_post_user
  ADD CONSTRAINT FK_user_TO_festival_post_user
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE tourist_post
  ADD CONSTRAINT FK_areacode_TO_tourist_post
    FOREIGN KEY (areacode_id)
    REFERENCES areacode (id);

ALTER TABLE tourist_post
  ADD CONSTRAINT FK_sigungucode_TO_tourist_post
    FOREIGN KEY (sigungucode_id)
    REFERENCES sigungucode (id);

ALTER TABLE festival_post
  ADD CONSTRAINT FK_areacode_TO_festival_post
    FOREIGN KEY (areacode_id)
    REFERENCES areacode (id);

ALTER TABLE festival_post
  ADD CONSTRAINT FK_sigungucode_TO_festival_post
    FOREIGN KEY (sigungucode_id)
    REFERENCES sigungucode (id);

ALTER TABLE festival_middle_class
  ADD CONSTRAINT FK_festival_large_class_TO_festival_middle_class
    FOREIGN KEY (festival_large_id)
    REFERENCES festival_large_class (id);

ALTER TABLE festival_small_class
  ADD CONSTRAINT FK_festival_middle_class_TO_festival_small_class
    FOREIGN KEY (festival_middle_id)
    REFERENCES festival_middle_class (id);

ALTER TABLE festival_post
  ADD CONSTRAINT FK_festival_large_class_TO_festival_post
    FOREIGN KEY (festival_large_id)
    REFERENCES festival_large_class (id);

ALTER TABLE festival_post
  ADD CONSTRAINT FK_festival_middle_class_TO_festival_post
    FOREIGN KEY (festival_middle_id)
    REFERENCES festival_middle_class (id);

ALTER TABLE festival_post
  ADD CONSTRAINT FK_festival_small_class_TO_festival_post
    FOREIGN KEY (festival_small_id)
    REFERENCES festival_small_class (id);

ALTER TABLE festival_post
  ADD CONSTRAINT FK_last_call_api_date_TO_festival_post
    FOREIGN KEY (last_call_api_id)
    REFERENCES last_call_api_date (id);

ALTER TABLE tourist_middle_class
  ADD CONSTRAINT FK_tourist_large_class_TO_tourist_middle_class
    FOREIGN KEY (tourist_large_id)
    REFERENCES tourist_large_class (id);

ALTER TABLE tourist_small_class
  ADD CONSTRAINT FK_tourist_middle_class_TO_tourist_small_class
    FOREIGN KEY (tourist_middle_id)
    REFERENCES tourist_middle_class (id);

ALTER TABLE tourist_post
  ADD CONSTRAINT FK_tourist_large_class_TO_tourist_post
    FOREIGN KEY (tourist_large_id)
    REFERENCES tourist_large_class (id);

ALTER TABLE tourist_post
  ADD CONSTRAINT FK_tourist_middle_class_TO_tourist_post
    FOREIGN KEY (tourist_middle_id)
    REFERENCES tourist_middle_class (id);

ALTER TABLE tourist_post
  ADD CONSTRAINT FK_tourist_small_class_TO_tourist_post
    FOREIGN KEY (tourist_small_id)
    REFERENCES tourist_small_class (id);

ALTER TABLE blog_reivew
  ADD CONSTRAINT FK_festival_post_TO_blog_reivew
    FOREIGN KEY (festival_post_id)
    REFERENCES festival_post (id);

ALTER TABLE tourist_post
  ADD CONSTRAINT FK_last_call_api_date_TO_tourist_post
    FOREIGN KEY (last_call_api_id)
    REFERENCES last_call_api_date (id);

ALTER TABLE blog_reivew
  ADD CONSTRAINT FK_last_call_api_date_TO_blog_reivew
    FOREIGN KEY (last_call_api_id)
    REFERENCES last_call_api_date (id);

ALTER TABLE weather_3
  ADD CONSTRAINT FK_last_call_api_date_TO_weather_3
    FOREIGN KEY (last_call_api_id)
    REFERENCES last_call_api_date (id);

ALTER TABLE weather_2
  ADD CONSTRAINT FK_last_call_api_date_TO_weather_2
    FOREIGN KEY (last_call_api_id)
    REFERENCES last_call_api_date (id);

ALTER TABLE weather_1
  ADD CONSTRAINT FK_last_call_api_date_TO_weather_1
    FOREIGN KEY (last_call_api_id)
    REFERENCES last_call_api_date (id);

ALTER TABLE weather_1
  ADD CONSTRAINT FK_areacode_TO_weather_1
    FOREIGN KEY (areacode_id)
    REFERENCES areacode (id);

ALTER TABLE weather_2
  ADD CONSTRAINT FK_areacode_TO_weather_2
    FOREIGN KEY (areacode_id)
    REFERENCES areacode (id);

ALTER TABLE weather_3
  ADD CONSTRAINT FK_areacode_TO_weather_3
    FOREIGN KEY (areacode_id)
    REFERENCES areacode (id);
