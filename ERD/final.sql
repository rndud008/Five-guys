SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS areacode;
DROP TABLE IF EXISTS attachment;
DROP TABLE IF EXISTS authority;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS last_call_api_date;
DROP TABLE IF EXISTS middle_weather;
DROP TABLE IF EXISTS short_weather;
DROP TABLE IF EXISTS sigungucode;
DROP TABLE IF EXISTS travel_class_detail;
DROP TABLE IF EXISTS travel_diary_post;
DROP TABLE IF EXISTS blog_review;
DROP TABLE IF EXISTS travel_post;
DROP TABLE IF EXISTS travel_type;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS user_authorities;
DROP TABLE IF EXISTS user_comment;
DROP TABLE IF EXISTS user_travel_diary_post;
DROP TABLE IF EXISTS user_travel_post;
CREATE TABLE areacode
(
  areacode INT         NOT NULL COMMENT '지역코드번호',
  nx       INT         NOT NULL COMMENT 'x좌표',
  ny       INT         NOT NULL COMMENT 'y좌표',
  name     VARCHAR(20) NOT NULL COMMENT '지역이름',
  regId    VARCHAR(10) NOT NULL COMMENT '중기예보 기온',
  regId2   VARCHAR(10) NOT NULL COMMENT '중기예보 날씨예보',
  PRIMARY KEY (areacode)
) COMMENT '지역(areacode)';

CREATE TABLE attachment
(
  id                   INT          NOT NULL AUTO_INCREMENT COMMENT '파일번호',
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

CREATE TABLE blog_review
(
  id               INT          NOT NULL AUTO_INCREMENT COMMENT '블로그 리뷰id',
  travel_post_id   INT          NULL     COMMENT '여행 정보 목록 id',
  last_call_api_id INT          NOT NULL COMMENT 'API 호출 id',
  title            VARCHAR(200) NULL     COMMENT '블로그 포스트의 제목',
  link             text NULL     COMMENT '블로그 포스트의 URL',
  description      VARCHAR(500) NULL     COMMENT '내용을 요약한 패시지 정보',
  postdate         VARCHAR(20)  NULL     COMMENT '블로그 포스트가 작성된 날짜',
  PRIMARY KEY (id)
) COMMENT '블로그 리뷰 리스트';

CREATE TABLE comment
(
  id                   INT      NOT NULL AUTO_INCREMENT COMMENT '댓글번호?',
  user_id              INT      NOT NULL COMMENT '회원번호',
  travel_diary_post_id INT      NOT NULL COMMENT '게시판번호',
  content              TEXT     NOT NULL COMMENT '댓글내용',
  regdate              DATETIME NULL     DEFAULT now() COMMENT '작성일',
  PRIMARY KEY (id)
) COMMENT '댓글';

CREATE TABLE last_call_api_date
(
  id INT  NOT NULL AUTO_INCREMENT COMMENT 'API호출id',
  url         TEXT NULL     COMMENT 'api url',
  regdate     DATE NULL     DEFAULT (CURRENT_DATE()) COMMENT '호출 날짜',
  PRIMARY KEY (id)
) COMMENT 'api 중복호출 방지';

CREATE TABLE middle_weather
(
  id               INT         NOT NULL AUTO_INCREMENT COMMENT '중기날씨 id',
  last_call_api_id INT         NOT NULL COMMENT 'API 호출 id',
  areacode         INT         NOT NULL COMMENT '지역코드번호',
  tmFc             VARCHAR(20) NULL     COMMENT '예보일자',
  taMin4           VARCHAR(10) NULL     COMMENT '4일 후 예상최저기온(℃)',
  taMax4           VARCHAR(10) NULL     COMMENT '4일 후 예상최고기온(℃)',
  wf4Am            VARCHAR(10) NULL     COMMENT '4일 후 오전 날씨예보',
  wf4Pm            VARCHAR(10) NULL     COMMENT '4일 후 오후 날씨예보',
  rnSt4Am          VARCHAR(10) NULL     COMMENT '4일 후 오전 강수량',
  rnSt4Pm          VARCHAR(10) NULL     COMMENT '4일 후 오후 강수량',
  taMin5           VARCHAR(10) NULL     COMMENT '5일 후 예상최저기온(℃)',
  taMax5           VARCHAR(10) NULL     COMMENT '5일 후 예상최고기온(℃)',
  wf5Am            VARCHAR(10) NULL     COMMENT '5일 후 오전 날씨예보',
  wf5Pm            VARCHAR(10) NULL     COMMENT '5일 후 오후 날씨예보',
  rnSt5Am          VARCHAR(10) NULL     COMMENT '5일 후 오전 강수량',
  rnSt5Pm          VARCHAR(10) NULL     COMMENT '5일 후 오후 강수량',
  taMin6           VARCHAR(10) NULL     COMMENT '6일 후 예상최저기온(℃)',
  taMax6           VARCHAR(10) NULL     COMMENT '6일 후 예상최고기온(℃)',
  wf6Am            VARCHAR(10) NULL     COMMENT '6일 후 오전 날씨예보',
  wf6Pm            VARCHAR(10) NULL     COMMENT '6일 후 오후 날씨예보',
  rnSt6Am          VARCHAR(10) NULL     COMMENT '6일 후 오전 강수량',
  rnSt6Pm          VARCHAR(10) NULL     COMMENT '6일 후 오후 강수량',
  taMin7           VARCHAR(10) NULL     COMMENT '7일 후 예상최저기온(℃)',
  taMax7           VARCHAR(10) NULL     COMMENT '7일 후 예상최고기온(℃)',
  wf7Am            VARCHAR(10) NULL     COMMENT '7일 후 오전 날씨예보',
  wf7Pm            VARCHAR(10) NULL     COMMENT '7일 후 오후 날씨예보',
  rnSt7Am          VARCHAR(10) NULL     COMMENT '7일 후 오전 강수량',
  rnSt7Pm          VARCHAR(10) NULL     COMMENT '7일 후 오후 강수량',
  PRIMARY KEY (id)
) COMMENT '중기예보(4~7일)';

CREATE TABLE short_weather
(
  id          INT         NOT NULL AUTO_INCREMENT COMMENT '단기날씨 id',
  areacode    INT         NOT NULL COMMENT '지역코드번호',
  last_api_id INT         NOT NULL COMMENT 'API호출id',
  fcstDate    DATE        NULL     COMMENT '예보일자',
  fcstTime    VARCHAR(10) NULL     COMMENT '예보시각',
  tmn         VARCHAR(10) NULL     COMMENT '최저기온',
  tmx         VARCHAR(10) NULL     COMMENT '최고기온',
  sky         VARCHAR(10) NULL     COMMENT '하늘상태',
  pop         VARCHAR(10) NULL     COMMENT '강수량',
  pty         VARCHAR(10) NULL     COMMENT '강수형태',
  PRIMARY KEY (id)
) COMMENT '단기예보(1~3일)';

CREATE TABLE sigungucode
(
    id          int         NOT NULL AUTO_INCREMENT COMMENT '시군구 id',
    areacode    INT         NOT NULL COMMENT '지역코드번호',
    sigungucode INT         NOT NULL COMMENT '코드번호',
    name        VARCHAR(20) NOT NULL COMMENT '시군구 이름',
    PRIMARY KEY (id),
    UNIQUE KEY UQ_areacode_sigungucode (areacode, sigungucode)
) COMMENT '시군구(sigungucode)';


CREATE TABLE travel_class_detail
(
    id             INT         NOT NULL AUTO_INCREMENT COMMENT '여행 분류 id',
    travel_type_id INT         NOT NULL COMMENT '여행 타입 id',
    name           VARCHAR(20) NULL     COMMENT '여행 분류이름',
    code           VARCHAR(10) NULL     COMMENT '코드번호',
    decode         VARCHAR(10) NULL     COMMENT '상위 코드번호',
    PRIMARY KEY (id),
    UNIQUE KEY UQ_code_travel_type_id (code, travel_type_id)
) COMMENT '여행정보 유형분류';


CREATE TABLE travel_diary_post
(
  id       INT          NOT NULL AUTO_INCREMENT COMMENT '게시판번호',
  user_id  INT          NOT NULL COMMENT '회원번호',
  areacode INT          NOT NULL COMMENT '지역코드번호',
  subject  VARCHAR(200) NOT NULL COMMENT '제목',
  content  LONGTEXT     NULL     COMMENT '내용',
  viewcnt  int          NULL     DEFAULT 0 COMMENT '조회수',
  regdate  DATETIME     NULL     DEFAULT now() COMMENT '작성일',
  PRIMARY KEY (id)
) COMMENT '게시판';

CREATE TABLE travel_post
(
  id                     INT          NOT NULL AUTO_INCREMENT COMMENT '여행 정보 목록 id',
  travel_class_detail_id INT          NOT NULL COMMENT '여행 분류 id',
  sigungucode_id         int          NOT NULL COMMENT '시군구 id',
  last_call_api_id       INT          NOT NULL COMMENT 'API 호출 id',
  title                  VARCHAR(200) NULL     COMMENT '제목',
  addr1                  VARCHAR(255) NULL     COMMENT '주소',
  addr2                  VARCHAR(255) NULL     COMMENT '상세주소',
  contentid              VARCHAR(20)  NULL     COMMENT '콘텐츠ID',
  firstimage             VARCHAR(255) NULL     COMMENT '대표이미지(원본)',
  firstimage2            VARCHAR(255) NULL     COMMENT '대표이미지(썸네일)',
  cpyrhtDivCd            VARCHAR(10)  NULL     COMMENT '저작권 유형',
  mapx                   DOUBLE       NULL     COMMENT 'GPS X좌표',
  mapy                   DOUBLE       NULL     COMMENT 'GPS Y좌표',
  modifiedtime           VARCHAR(20)  NULL     COMMENT '수정일',
  tel                    VARCHAR(255) NULL     COMMENT '전화번호',
  eventstartdate         VARCHAR(20)  NULL     COMMENT '행사시작일',
  eventenddate           VARCHAR(20)  NULL     COMMENT '행사종료일',
  infocenter             TEXT         NULL     COMMENT '문의및안내 / 관광소개정보',
  parking                VARCHAR(255) NULL     COMMENT '주차시설 / 관광소개정보',
  restdate               VARCHAR(255) NULL     COMMENT '쉬는날 / 관광소개정보',
  usetime                TEXT         NULL     COMMENT '이용시간 / 관광소개정보',
  homepage               TEXT         NULL     COMMENT '홈페이지주소 / 공통정보',
  overview               TEXT         NULL     COMMENT '개요 / 공통정보',
  eventplace             TEXT         NULL     COMMENT '행사장소 / 축제소개정보',
  playtime               TEXT         NULL     COMMENT '공연시간 / 축제소개정보',
  usetimefestival        TEXT         NULL     COMMENT '이용요금 / 축제소개정보',
  infoname               VARCHAR(10)  NULL     COMMENT '행사내용 만 가져옴 title 확인  / 축제반복내용',
  infotext               TEXT         NULL     COMMENT '행사내용 / 축제반복내용',
  PRIMARY KEY (id)
) COMMENT '여행 정보 목록';

alter table travel_post
modify infocenter text;

CREATE TABLE travel_type
(
  id   INT         NOT NULL COMMENT '여행 타입 id',
  name VARCHAR(20) NULL     COMMENT '이름',
  PRIMARY KEY (id)
) COMMENT '여행타입';

CREATE TABLE user
(
  id        INT          NOT NULL AUTO_INCREMENT COMMENT '회원번호',
  username  VARCHAR(100) NOT NULL COMMENT '아이디',
  password  VARCHAR(100) NOT NULL COMMENT '비밀번호',
  name      VARCHAR(80)  NOT NULL COMMENT '별명',
  email     VARCHAR(80)  NULL     COMMENT '이메일',
  regdate   DATETIME     NULL     DEFAULT now() COMMENT '가입일',
  provide   VARCHAR(30)  NULL     COMMENT 'OAuth 가입처',
  provideId VARCHAR(200) NULL     COMMENT 'OAuth id(PK)',
  PRIMARY KEY (id)
) COMMENT '회원가입';

ALTER TABLE user
  ADD CONSTRAINT UQ_username UNIQUE (username);

# ALTER TABLE user
#   ADD CONSTRAINT UQ_name UNIQUE (name);

CREATE TABLE user_authorities
(
  user_id      INT NOT NULL COMMENT '회원번호',
  authority_id INT NOT NULL COMMENT '권한번호',
  PRIMARY KEY (user_id, authority_id)
);

CREATE TABLE user_comment
(
  user_id    INT NOT NULL COMMENT '회원번호',
  comment_id INT NOT NULL COMMENT '댓글번호',
  PRIMARY KEY (user_id, comment_id)
) COMMENT '댓글 좋아요';

CREATE TABLE user_travel_diary_post
(
  user_id              INT NOT NULL COMMENT '회원번호',
  travel_diary_post_id INT NOT NULL COMMENT '게시판번호',
  PRIMARY KEY (user_id, travel_diary_post_id)
) COMMENT '게시글 좋아요';

CREATE TABLE user_travel_post
(
  user_id        INT NOT NULL COMMENT '회원번호',
  travel_post_id INT NOT NULL COMMENT '여행 정보 목록 id',
  PRIMARY KEY (user_id, travel_post_id)
) COMMENT '여행 정보 좋아요';

ALTER TABLE attachment
  ADD CONSTRAINT FK_travel_diary_post_TO_attachment
    FOREIGN KEY (travel_diary_post_id)
    REFERENCES travel_diary_post (id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE;

ALTER TABLE blog_review
  ADD CONSTRAINT FK_travel_post_TO_blog_review
    FOREIGN KEY (travel_post_id)
    REFERENCES travel_post (id);

ALTER TABLE blog_review
  ADD CONSTRAINT FK_last_call_api_date_TO_blog_review
    FOREIGN KEY (last_call_api_id)
    REFERENCES last_call_api_date (id);

ALTER TABLE comment
  ADD CONSTRAINT FK_user_TO_comment
    FOREIGN KEY (user_id)
    REFERENCES user (id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE;

ALTER TABLE comment
  ADD CONSTRAINT FK_travel_diary_post_TO_comment
    FOREIGN KEY (travel_diary_post_id)
    REFERENCES travel_diary_post (id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE;

ALTER TABLE middle_weather
  ADD CONSTRAINT FK_last_call_api_date_TO_middle_weather
    FOREIGN KEY (last_call_api_id)
    REFERENCES last_call_api_date (id);

ALTER TABLE travel_class_detail
  ADD CONSTRAINT FK_travel_type_TO_travel_class_detail
    FOREIGN KEY (travel_type_id)
    REFERENCES travel_type (id);

ALTER TABLE travel_diary_post
  ADD CONSTRAINT FK_user_TO_travel_diary_post
    FOREIGN KEY (user_id)
    REFERENCES user (id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE;

ALTER TABLE travel_post
  ADD CONSTRAINT FK_travel_class_detail_TO_travel_post
    FOREIGN KEY (travel_class_detail_id)
    REFERENCES travel_class_detail (id);

ALTER TABLE travel_post
  ADD CONSTRAINT FK_sigungucode_TO_travel_post
    FOREIGN KEY (sigungucode_id)
    REFERENCES sigungucode (id);

ALTER TABLE travel_post
  ADD CONSTRAINT FK_last_call_api_date_TO_travel_post
    FOREIGN KEY (last_call_api_id)
    REFERENCES last_call_api_date (id);

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
    REFERENCES user (id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE;

ALTER TABLE user_comment
  ADD CONSTRAINT FK_comment_TO_user_comment
    FOREIGN KEY (comment_id)
    REFERENCES comment (id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE;

ALTER TABLE user_travel_diary_post
  ADD CONSTRAINT FK_user_TO_user_travel_diary_post
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE user_travel_diary_post
  ADD CONSTRAINT FK_travel_diary_post_TO_user_travel_diary_post
    FOREIGN KEY (travel_diary_post_id)
    REFERENCES travel_diary_post (id);

ALTER TABLE user_travel_post
  ADD CONSTRAINT FK_travel_post_TO_user_travel_post
    FOREIGN KEY (travel_post_id)
    REFERENCES travel_post (id);

ALTER TABLE user_travel_post
  ADD CONSTRAINT FK_user_TO_user_travel_post
    FOREIGN KEY (user_id)
    REFERENCES user (id);

ALTER TABLE middle_weather
  ADD CONSTRAINT FK_areacode_TO_middle_weather
    FOREIGN KEY (areacode)
    REFERENCES areacode (areacode);

ALTER TABLE sigungucode
  ADD CONSTRAINT FK_areacode_TO_sigungucode
    FOREIGN KEY (areacode)
    REFERENCES areacode (areacode);

ALTER TABLE travel_diary_post
  ADD CONSTRAINT FK_areacode_TO_travel_diary_post
    FOREIGN KEY (areacode)
    REFERENCES areacode (areacode);

ALTER TABLE short_weather
  ADD CONSTRAINT FK_last_call_api_date_TO_short_weather
    FOREIGN KEY (last_api_id)
    REFERENCES last_call_api_date (id);

ALTER TABLE short_weather
  ADD CONSTRAINT FK_areacode_TO_short_weather
    FOREIGN KEY (areacode)
    REFERENCES areacode (areacode);

ALTER TABLE user
    DROP CONSTRAINT UQ_name;
