
CREATE TABLE 
(
  id             INT          NOT NULL AUTO_INCREMENT COMMENT '여행 공통정보 id',
  travel_post_id INT          NOT NULL COMMENT '여행 정보 목록 id',
  homepage       VARCHAR(200) NULL     COMMENT '홈페이지주소',
  overview       LONGTEXT     NULL     COMMENT '개요',
  PRIMARY KEY (id)
) COMMENT '여행 공통정보';

CREATE TABLE areacode
(
  id    INT         NOT NULL AUTO_INCREMENT COMMENT '지역id',
  name  VARCHAR(20) NOT NULL COMMENT '지역이름',
  code  VARCHAR(20) NOT NULL COMMENT '코드번호',
  regId VARCHAR(50) NOT NULL COMMENT '중기예보 구역코드',
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
  id               INT          NOT NULL AUTO_INCREMENT COMMENT '블로그 리뷰id',
  travel_post_id   INT          NULL     COMMENT '여행 정보 목록 id',
  last_call_api_id INT          NOT NULL COMMENT 'API호출id',
  title            TEXT         NULL     COMMENT '블로그 포스트의 제목',
  link             VARCHAR(255) NULL     COMMENT '블로그 포스트의 URL',
  description      TEXT         NULL     COMMENT '내용을 요약한 패시지 정보',
  postdate         VARCHAR(50)  NULL     COMMENT '블로그 포스트가 작성된 날짜',
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

CREATE TABLE festival_content
(
  id             INT         NOT NULL AUTO_INCREMENT COMMENT 'festival_content',
  travel_post_id INT         NOT NULL COMMENT '여행 정보 목록 id',
  infoname       VARCHAR(20) NULL     COMMENT '행사내용 만 가져옴 title 확인 ',
  infotext       LONGTEXT    NULL     COMMENT '행사내용',
  PRIMARY KEY (id)
) COMMENT '여행 축제 행사내용';

CREATE TABLE festivalinfo
(
  id              INT          NOT NULL AUTO_INCREMENT COMMENT 'festivalinfo id',
  travel_post_id  INT          NOT NULL COMMENT '여행 정보 목록 id',
  eventplace      VARCHAR(100) NULL     COMMENT '행사장소',
  playtime        VARCHAR(100) NULL     COMMENT '공연시간',
  usetimefestival VARCHAR(100) NULL     COMMENT '이용요금',
  PRIMARY KEY (id)
) COMMENT '여행 축제소개정보';

CREATE TABLE last_call_api_date
(
  id        INT          NOT NULL AUTO_INCREMENT COMMENT 'API호출id',
  apiname   VARCHAR(255) NULL     COMMENT '호출한 정보 타이틀?',
  regdate   DATETIME     NULL     DEFAULT now() COMMENT '호출 날짜',
  r_code    VARCHAR(10)  NULL     COMMENT 'response code',
  r_message VARCHAR(255) NULL     COMMENT 'response message',
  PRIMARY KEY (id)
) COMMENT 'api 중복호출 방지';

CREATE TABLE sigungucode
(
  id          INT         NOT NULL AUTO_INCREMENT COMMENT '시군구id',
  areacode_id INT         NOT NULL COMMENT '지역id',
  name        VARCHAR(20) NOT NULL COMMENT '시군구 이름',
  code        VARCHAR(20) NULL     COMMENT '코드번호',
  PRIMARY KEY (id)
) COMMENT '시군구(sigungucode)';

CREATE TABLE touristinfo
(
  id             INT          NOT NULL COMMENT 'touristinfo id',
  travel_post_id INT          NOT NULL COMMENT '여행 정보 목록 id',
  infocenter     VARCHAR(100) NULL     COMMENT '문의및안내',
  parking        VARCHAR(100) NULL     COMMENT '주차시설',
  restdate       VARCHAR(100) NULL     COMMENT '쉬는날',
  usetime        VARCHAR(100) NULL     COMMENT '이용시간',
  PRIMARY KEY (id)
) COMMENT '여행 관광소개정보';

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

CREATE TABLE travel_large_class
(
  id             INT         NOT NULL AUTO_INCREMENT COMMENT '여행 대분류 id',
  travel_type_id INT         NOT NULL COMMENT '여행 타입 id',
  name           VARCHAR(20) NULL     COMMENT '여행정보 대분류이름',
  code           VARCHAR(20) NULL     COMMENT '코드번호',
  PRIMARY KEY (id)
) COMMENT '여행정보 유형 대분류';

CREATE TABLE travel_middle_class
(
  id              INT         NOT NULL AUTO_INCREMENT COMMENT '여행 중분류 id',
  travel_large_id INT         NOT NULL COMMENT '여행 대분류 id',
  name            VARCHAR(20) NULL     COMMENT '여행정보 중분류이름',
  code            VARCHAR(20) NULL     COMMENT '코드번호',
  PRIMARY KEY (id)
) COMMENT '여행 정보 유형 중분류';

CREATE TABLE travel_post
(
  id                INT          NOT NULL AUTO_INCREMENT COMMENT '여행 정보 목록 id',
  areacode_id       INT          NOT NULL COMMENT '지역id',
  travel_type_id    INT          NOT NULL COMMENT '여행 타입 id',
  last_call_api_id  INT          NOT NULL COMMENT 'API호출id',
  title             VARCHAR(100) NULL     COMMENT '제목',
  addr1             VARCHAR(200) NULL     COMMENT '주소',
  addr2             VARCHAR(200) NULL     COMMENT '상세주소',
  contentid         VARCHAR(20)  NULL     COMMENT '콘텐츠ID',
  firstimage        VARCHAR(100) NULL     COMMENT '대표이미지(원본)',
  firstimage2       VARCHAR(100) NULL     COMMENT '대표이미지(썸네일)',
  cpyrhtDivCd       VARCHAR(100) NULL     COMMENT '저작권 유형',
  mapx              VARCHAR(100) NULL     COMMENT 'GPS X좌표',
  mapy              VARCHAR(100) NULL     COMMENT 'GPS Y좌표',
  modifiedtime      VARCHAR(100) NULL     COMMENT '수정일',
  tel               VARCHAR(100) NULL     COMMENT '전화번호',
  eventstartdate    VARCHAR(100) NULL     COMMENT '행사시작일',
  eventenddate      VARCHAR(100) NULL     COMMENT '행사종료일',
  travel_like_count INT          NULL     DEFAULT 0 COMMENT '여행 정보 좋아요',
  PRIMARY KEY (id)
) COMMENT '여행 정보 목록';

CREATE TABLE travel_post_user
(
  travel_post_id INT     NOT NULL COMMENT '여행 정보 목록 id',
  user_id        INT     NOT NULL COMMENT '회원번호',
  liked          BOOLEAN NULL     DEFAULT false,
  PRIMARY KEY (travel_post_id, user_id)
) COMMENT '여행 정보 좋아요';

CREATE TABLE travel_small_class
(
  id               INT         NOT NULL AUTO_INCREMENT COMMENT '여행 소분류 id',
  travel_middle_id INT         NOT NULL COMMENT '여행 중분류 id',
  name             VARCHAR(20) NULL     COMMENT '여행정보 소분류이름',
  code             VARCHAR(20) NULL     COMMENT '코드번호',
  PRIMARY KEY (id)
) COMMENT '여행 정보 유형 소분류';

CREATE TABLE travel_type
(
  id   INT         NOT NULL AUTO_INCREMENT COMMENT '여행 타입 id',
  name VARCHAR(20) NULL     COMMENT '타입 이름',
  code VARCHAR(20) NULL     COMMENT '코드번호',
  PRIMARY KEY (id)
) COMMENT '여행 타입';

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
  user_id    INT     NOT NULL COMMENT '회원번호',
  comment_id INT     NOT NULL COMMENT '댓글번호?',
  liked      BOOLEAN NULL     DEFAULT false,
  PRIMARY KEY (user_id, comment_id)
) COMMENT '댓글 좋아요';

CREATE TABLE user_post
(
  user_id              INT     NOT NULL COMMENT '회원번호',
  travel_diary_post_id INT     NOT NULL COMMENT '게시판번호',
  liked                BOOLEAN NULL     DEFAULT false,
  PRIMARY KEY (user_id, travel_diary_post_id)
) COMMENT '게시글 좋아요';

CREATE TABLE weather_1
(
  id               INT         NOT NULL AUTO_INCREMENT COMMENT '딘기날씨 id',
  last_call_api_id INT         NOT NULL COMMENT 'API호출id',
  areacode_id      INT         NOT NULL COMMENT '지역id',
  basedate         VARCHAR(20) NULL     COMMENT '발표일자',
  basetime         VARCHAR(20) NULL     COMMENT '발표시각',
  fcstdate         VARCHAR(20) NULL     COMMENT '예보일자',
  fcsttime         VARCHAR(20) NULL     COMMENT '예보시각',
  tmn              VARCHAR(20) NULL     COMMENT '최저 기온',
  tmx              VARCHAR(20) NULL     COMMENT '최고 기온',
  sky              VARCHAR(20) NULL     COMMENT '하늘상태',
  pop              VARCHAR(20) NULL     COMMENT '강수확률',
  pty              VARCHAR(20) NULL     COMMENT '강수형태',
  PRIMARY KEY (id)
) COMMENT '단기날씨 1~3일째 날씨';

CREATE TABLE weather_2
(
  id               INT         NOT NULL AUTO_INCREMENT COMMENT '중기날씨 중기육상예보 id',
  last_call_api_id INT         NOT NULL COMMENT 'API호출id',
  areacode_id      INT         NOT NULL COMMENT '지역id',
  rnSt4Pm          VARCHAR(20) NULL     COMMENT '4일 후 오후 강수 확률',
  rnSt5Pm          VARCHAR(20) NULL     COMMENT '5일 후 오후 강수 확률',
  rnSt6Pm          VARCHAR(20) NULL     COMMENT '6일 후 오후 강수 확률',
  rnSt7Pm          VARCHAR(20) NULL     COMMENT '7일 후 오후 강수 확률',
  wf4Pm            VARCHAR(20) NULL     COMMENT '4일 후 오후 하늘상태',
  wf5Pm            VARCHAR(20) NULL     COMMENT '5일 후 오후 하늘상태',
  wf6Pm            VARCHAR(20) NULL     COMMENT '6일 후 오후 하늘상태',
  wf7Pm            VARCHAR(20) NULL     COMMENT '7일 후 오후 하늘상태',
  tmFc             VARCHAR(50) NULL     COMMENT '발표시각 // 확인 type datetime ?',
  PRIMARY KEY (id)
) COMMENT '중기날씨 중기육상예보';

CREATE TABLE weather_3
(
  id               INT         NOT NULL AUTO_INCREMENT COMMENT '중기날씨 중기기온 id',
  areacode_id      INT         NOT NULL COMMENT '지역id',
  last_call_api_id INT         NOT NULL COMMENT 'API호출id',
  taMin4           VARCHAR(20) NULL     COMMENT '4일 후 예상최저기온(℃)',
  taMax4           VARCHAR(20) NULL     COMMENT '4일 후 예상최고기온(℃)',
  taMin5           VARCHAR(20) NULL     COMMENT '5일 후 예상최저기온(℃)',
  taMax5           VARCHAR(20) NULL     COMMENT '5일 후 예상최고기온(℃)',
  taMin6           VARCHAR(20) NULL     COMMENT '6일 후 예상최저기온(℃)',
  taMax6           VARCHAR(20) NULL     COMMENT '6일 후 예상최고기온(℃)',
  taMin7           VARCHAR(20) NULL     COMMENT '7일 후 예상최저기온(℃)',
  taMax7           VARCHAR(20) NULL     COMMENT '7일 후 예상최고기온(℃)',
  tmFc             VARCHAR(50) NULL     COMMENT '발표시각 // 확인 type datetime ?',
  PRIMARY KEY (id)
) COMMENT '중기날씨 중기기온';

ALTER TABLE attachment
  ADD CONSTRAINT FK_travel_diary_post_TO_attachment
    FOREIGN KEY (travel_diary_post_id)
    REFERENCES travel_diary_post (id);

ALTER TABLE blog_reivew
  ADD CONSTRAINT FK_travel_post_TO_blog_reivew
    FOREIGN KEY (travel_post_id)
    REFERENCES travel_post (id);

ALTER TABLE blog_reivew
  ADD CONSTRAINT FK_last_call_api_date_TO_blog_reivew
    FOREIGN KEY (last_call_api_id)
    REFERENCES last_call_api_date (id);

ALTER TABLE comment
  ADD CONSTRAINT FK_user_TO_comment
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE comment
  ADD CONSTRAINT FK_travel_diary_post_TO_comment
    FOREIGN KEY (travel_diary_post_id)
    REFERENCES travel_diary_post (id);

ALTER TABLE travel_middle_class
  ADD CONSTRAINT FK_travel_large_class_TO_travel_middle_class
    FOREIGN KEY (travel_large_id)
    REFERENCES travel_large_class (id);

ALTER TABLE travel_post
  ADD CONSTRAINT FK_areacode_TO_travel_post
    FOREIGN KEY (areacode_id)
    REFERENCES areacode (id);

ALTER TABLE travel_post
  ADD CONSTRAINT FK_last_call_api_date_TO_travel_post
    FOREIGN KEY (last_call_api_id)
    REFERENCES last_call_api_date (id);

ALTER TABLE travel_post_user
  ADD CONSTRAINT FK_travel_post_TO_travel_post_user
    FOREIGN KEY (travel_post_id)
    REFERENCES travel_post (id);

ALTER TABLE travel_post_user
  ADD CONSTRAINT FK_user_TO_travel_post_user
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE travel_small_class
  ADD CONSTRAINT FK_travel_middle_class_TO_travel_small_class
    FOREIGN KEY (travel_middle_id)
    REFERENCES travel_middle_class (id);

ALTER TABLE sigungucode
  ADD CONSTRAINT FK_areacode_TO_sigungucode
    FOREIGN KEY (areacode_id)
    REFERENCES areacode (id);

ALTER TABLE travel_diary_post
  ADD CONSTRAINT FK_user_TO_travel_diary_post
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE user_authorities
  ADD CONSTRAINT FK_user_TO_user_authorities
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE user_authorities
  ADD CONSTRAINT FK_authority_TO_user_authorities
    FOREIGN KEY (authority_id)
    REFERENCES authority (id);

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

ALTER TABLE weather_1
  ADD CONSTRAINT FK_last_call_api_date_TO_weather_1
    FOREIGN KEY (last_call_api_id)
    REFERENCES last_call_api_date (id);

ALTER TABLE weather_1
  ADD CONSTRAINT FK_areacode_TO_weather_1
    FOREIGN KEY (areacode_id)
    REFERENCES areacode (id);

ALTER TABLE weather_2
  ADD CONSTRAINT FK_last_call_api_date_TO_weather_2
    FOREIGN KEY (last_call_api_id)
    REFERENCES last_call_api_date (id);

ALTER TABLE weather_2
  ADD CONSTRAINT FK_areacode_TO_weather_2
    FOREIGN KEY (areacode_id)
    REFERENCES areacode (id);

ALTER TABLE travel_large_class
  ADD CONSTRAINT FK_travel_type_TO_travel_large_class
    FOREIGN KEY (travel_type_id)
    REFERENCES travel_type (id);

ALTER TABLE travel_post
  ADD CONSTRAINT FK_travel_type_TO_travel_post
    FOREIGN KEY (travel_type_id)
    REFERENCES travel_type (id);

ALTER TABLE 
  ADD CONSTRAINT FK_travel_post_TO_
    FOREIGN KEY (travel_post_id)
    REFERENCES travel_post (id);

ALTER TABLE touristinfo
  ADD CONSTRAINT FK_travel_post_TO_touristinfo
    FOREIGN KEY (travel_post_id)
    REFERENCES travel_post (id);

ALTER TABLE festivalinfo
  ADD CONSTRAINT FK_travel_post_TO_festivalinfo
    FOREIGN KEY (travel_post_id)
    REFERENCES travel_post (id);

ALTER TABLE weather_3
  ADD CONSTRAINT FK_last_call_api_date_TO_weather_3
    FOREIGN KEY (last_call_api_id)
    REFERENCES last_call_api_date (id);

ALTER TABLE weather_3
  ADD CONSTRAINT FK_areacode_TO_weather_3
    FOREIGN KEY (areacode_id)
    REFERENCES areacode (id);

ALTER TABLE festival_content
  ADD CONSTRAINT FK_travel_post_TO_festival_content
    FOREIGN KEY (travel_post_id)
    REFERENCES travel_post (id);
