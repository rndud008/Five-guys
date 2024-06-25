INSERT INTO areacode (areacode, name, regId, regId2) VALUES
(1, '서울', '11B10101'), -- 서울의 regId를 서울에 포함
(2, '부산', '11H20201'), -- 부산의 regId를 부산에 포함
(3, '대전', '11C20401'), -- 대전의 regId를 대전에 포함
(4, '전북', '11F10201'), -- 전주시의 regId를 전북에 포함
(5, '강원', '11D10301'), -- 춘천시의 regId를 강원에 포함
(6, '경남', '11H20301'), -- 창원시의 regId를 경남에 포함
(7, '광주', '11F20501'), -- 전라도의 광주의 regId를 광주에 포함
(8, '전남', '21F20804'), -- 무안군의 regId를 전남에 포함
(9, '경기', '11B20202'), -- 시흥의 regId를 경기에 포함
(10, '세종', '11C20404'), -- 세종의 regId를 세종에 포함
(11, '충북', '11C10301'), -- 청주의 regId를 충북에 포함
(12, '충남', '11C20104'), -- 홍성의 regId를 충남에 포함
(13, '대구', '11H10701'), -- 대구의 regId를 대구에 포함
(14, '울산', '11H20101'), -- 울산의 regId를 울산에 포함
(15, '경북', '11H10501'), -- 안동의 regId를 경북에 포함
(16, '인천', '11B20201'), -- 인천의 regId를 인천에 포함
(17, '제주', '11G00201'); -- 제주의 regId를 제주에 포함
select * from areacode;


INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (1, '서울', '11B10101', '11B00000', 60, 127);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (2, '부산', '11H20201', '11H20000', 98, 76);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (3, '대전', '11C20401', '11C20000', 67, 100);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (4, '전북', '11F10201', '11F10000', 63, 89);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (5, '강원', '11D10301', '11D20000', 73, 134);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (6, '경남', '11H20301', '11H20000', 90, 77);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (7, '광주', '11F20501', '11F20000', 60, 74);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (8, '전남', '11F20601', '11F20000', 52, 71);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (9, '경기', '11B20301', '11B00000', 57, 123);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (10, '세종', '11C20201', '11C20000', 66, 103);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (11, '충북', '11C20402', '11C10000', 69, 106);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (12, '충남', '11C20403', '11C20000', 55, 106);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (13, '대구', '11H10701', '11H10000', 99, 75);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (14, '울산', '11H20101', '11H20000', 102, 84);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (15, '경북', '11H10501', '11H20000', 91, 106);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (16, '인천', '11B20201', '11B00000', 54, 125);
INSERT INTO areacode (areacode, name, regId, regId2, nx, ny) VALUES (17, '제주', '11G00201', '11G00000', 53, 38);

DELETE FROM areacode;

INSERT INTO areacode (areacode, name, regId, regId2, nx, ny)
VALUES
    (1, '서울', '11B10101', '11B00000', 60, 127),
    (2, '부산', '11H20201', '11H20000', 98, 76),
    (3, '대전', '11C20401', '11C20000', 67, 100),
    (4, '전북', '11F10201', '11F10000', 63, 89),
    (5, '강원', '11D10301', '11D20000', 73, 134),
    (6, '경남', '11H20301', '11H20000', 90, 77),
    (7, '광주', '11F20501', '11F20000', 60, 74),
    (8, '전남', '11F20601', '11F20000', 52, 71),
    (31, '경기', '11B20301', '11B00000', 57, 123),
    (32, '세종', '11C20201', '11C20000', 66, 103),
    (33, '충북', '11C20402', '11C10000', 69, 106),
    (34, '충남', '11C20403', '11C20000', 55, 106),
    (35, '대구', '11H10701', '11H10000', 99, 75),
    (36, '울산', '11H20101', '11H20000', 102, 84),
    (37, '경북', '11H10501', '11H20000', 91, 106),
    (38, '인천', '11B20201', '11B00000', 54, 125),
    (39, '제주', '11G00201', '11G00000', 53, 38);







INSERT INTO sigungucode (areacode, sigungucode, name) VALUES
(1, 1, '강남구'),
(1, 2, '강동구'),
(1, 3, '강북구'),
(1, 4, '강서구'),
(1, 5, '관악구'),
(1, 6, '광진구'),
(1, 7, '구로구'),
(1, 8, '금천구'),
(1, 9, '노원구'),
(1, 10, '도봉구'),
(1, 11, '동대문구'),
(1, 12, '동작구'),
(1, 13, '마포구'),
(1, 14, '서대문구'),
(1, 15, '서초구'),
(1, 16, '성동구'),
(1, 17, '성북구'),
(1, 18, '송파구'),
(1, 19, '양천구'),
(1, 20, '영등포구'),
(1, 21, '용산구'),
(1, 22, '은평구'),
(1, 23, '종로구'),
(1, 24, '중구'),
(1, 25, '중랑구'),
(2, 1, '강화군'),
(2, 2, '계양구'),
(2, 3, '미추홀구'),
(2, 4, '남동구'),
(2, 5, '동구'),
(2, 6, '부평구'),
(2, 7, '서구'),
(2, 8, '연수구'),
(2, 9, '옹진군'),
(2, 10, '중구');

-- areacode 테이블 데이터 확인
SELECT * FROM areacode;
SELECT * FROM sigungucode;

-- 서울의 areacode와 이름 조회
SELECT name
FROM areacode
WHERE areacode = 1;

-- short_weather 테이블 데이터 조회
SELECT *
FROM short_weather;

-- middle_weather 테이블 데이터 조회
select * from areacode;

SELECT name
FROM areacode
WHERE areacode = 1;

SELECT *
FROM sigungucode
where areacode = 2 and sigungucode = 3;

SELECT *
FROM sigungucode;

select *
FROM travel_type;

select *
FROM travel_class_detail;

select infotext
FROM travel_post
where travel_class_detail_id = 15;

select *
FROM travel_post
where sigungucode_id is null;

select *
FROM travel_post;

select *
FROM travel_post
where title LIKE '%도드람%';

select *
FROM last_call_api_date;



SELECT *
FROM last_call_api_date;

INSERT INTO last_call_api_date (id, url, regdate) VALUES
                                                      (1,'test','2024-06-12');



DROP TABLE IF EXISTS weather_forecast;




select * from weather_forecast;

SELECT * FROM weather_forecast
WHERE forecast_date = '2024-06-16';

SELECT * FROM weather_forecast
WHERE forecast_date = '2024-06-16'
  AND category = 'tmp';


CREATE TABLE last_call_api_date (
                                    id INT NOT NULL AUTO_INCREMENT COMMENT 'API 호출 id',
                                    url TEXT NULL COMMENT 'apiurl',
                                    regdate DATE NULL DEFAULT (CURRENT_DATE()) COMMENT '호출 날짜',
                                    PRIMARY KEY (id)
) COMMENT 'api 중복호출 방지';

CREATE TABLE weather_forecast (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(50),
    forecast_date DATE,
    forecast_time VARCHAR(10),
    forecast_value VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    url TEXT

);


#     api_call_id INT,
#     CONSTRAINT fk_api_call_id FOREIGN KEY (api_call_id) REFERENCES last_call_api_date(id)


select * from sigungucode;
select * from areacode;




DROP TABLE IF EXISTS last_call_api_date;
DROP TABLE IF EXISTS blog_review;
DELETE FROM areacode;

CREATE TABLE last_call_api_date
(
    last_api_id INT  NOT NULL AUTO_INCREMENT COMMENT 'API호출id',
    url         TEXT NULL     COMMENT 'api url',
    regdate     DATE NULL     DEFAULT (CURRENT_DATE()) COMMENT '호출 날짜',
    PRIMARY KEY (last_api_id)
) COMMENT 'api 중복호출 방지';

-- 유니크 인덱스 생성
CREATE UNIQUE INDEX idx_unique_url ON last_call_api_date (url);

-- 데이터 삽입 또는 업데이트 쿼리
INSERT INTO last_call_api_date (url, regdate)
VALUES ('새로운_URL_값', CURRENT_DATE())
ON DUPLICATE KEY UPDATE url = VALUES(url), regdate = VALUES(regdate);


select *
from last_call_api_date;



DROP TABLE IF EXISTS short_weather;
DELETE FROM short_weather;
DELETE FROM middle_weather;
DELETE FROM last_call_api_date;

CREATE TABLE short_weather
(
    id          INT         NOT NULL AUTO_INCREMENT COMMENT '단기날씨 id',
    last_api_id INT         NOT NULL COMMENT 'API호출id',
    areacode    INT         NOT NULL COMMENT '지역코드번호',
    fcstDate    DATE        NULL     COMMENT '예보일자',
    fcstTime    VARCHAR(10) NULL     COMMENT '예보시각',
    TMN         VARCHAR(10)  NULL     COMMENT '최저기온',
    TMX         VARCHAR(10)  NULL     COMMENT '최고기온',
    SKY         VARCHAR(10)  NULL     COMMENT '하늘상태',
    POP         VARCHAR(10)  NULL     COMMENT '강수량',
    PTY         VARCHAR(10)  NULL     COMMENT '강수형태',
    PRIMARY KEY (id)
) COMMENT '단기예보(1~3일)';

select *
from short_weather;

SELECT *
FROM short_weather
ORDER BY areacode ASC, fcstDate ASC, fcstTime ASC;



SELECT * FROM short_weather WHERE areacode = areacode;

SELECT * FROM short_weather WHERE areacode = 1;

DELETE FROM short_weather;
DELETE FROM middle_weather;
DELETE FROM last_call_api_date;

select *
from areacode;

select *
from short_weather;

select *
from middle_weather;

select *
from last_call_api_date;

