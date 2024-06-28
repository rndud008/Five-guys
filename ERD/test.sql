
-- areacode 테이블 데이터 확인
SELECT *
FROM areacode;

select *
from user_travel_post;

select *
from user;

delete from user_travel_post
where user_id =2 and travel_post_id =2;

SELECT s.*
FROM sigungucode s;

-- 서울의 areacode와 이름 조회
SELECT a.name, s.name
FROM areacode a,
     sigungucode s
WHERE a.areacode = 1
  and s.areacode = 1;


-- short_weather 테이블 데이터 조회
SELECT *
FROM short_weather;

-- middle_weather 테이블 데이터 조회
select *
from areacode;

SELECT name
FROM areacode
WHERE areacode = 1;

SELECT *
FROM sigungucode
where areacode = 2
  and sigungucode = 3;

SELECT *
FROM sigungucode
where areacode = 37;

select *
FROM travel_type;

select *
FROM last_call_api_date;

select travel_post_id
from user_travel_post
where travel_post_id =1 and user_id =2;

select *
from user_travel_post;


select *
FROM travel_class_detail;

select *
FROM travel_class_detail
where decode = '';

select infotext
FROM travel_post
where travel_class_detail_id = 15;

select *
FROM travel_post
where contentid = 127377;

select *
FROM blog_review;

select *
FROM travel_post
where contentid = 294390;

select *
FROM travel_post;

select *
FROM travel_post
where title LIKE '%도드람%';

select *
FROM last_call_api_date;

select tp.*
FROM travel_post tp
         join travel_class_detail tcd on tp.travel_class_detail_id = tcd.id
         join travel_type tt on tcd.travel_type_id = tt.id
where tt.id = 15
  and infotext is null;

select tp.*
FROM travel_post tp
         join travel_class_detail tcd on tp.travel_class_detail_id = tcd.id
         join travel_type tt on tcd.travel_type_id = tt.id
where title like '%계곡%'
  and travel_type_id = 12;


SELECT *
FROM last_call_api_date;

INSERT INTO last_call_api_date (id, url, regdate)
VALUES (1, 'test', '2024-06-12');

select *
FROM travel_class_detail tcd
         join travel_type tt on tcd.travel_type_id = tt.id
where travel_type_id = 12;

DROP TABLE IF EXISTS weather_forecast;



select *
from weather_forecast;

SELECT *
FROM weather_forecast
WHERE forecast_date = '2024-06-16';

SELECT *
FROM weather_forecast
WHERE forecast_date = '2024-06-16'
  AND category = 'tmp';


CREATE TABLE last_call_api_date
(
    id      INT  NOT NULL AUTO_INCREMENT COMMENT 'API 호출 id',
    url     TEXT NULL COMMENT 'apiurl',
    regdate DATE NULL DEFAULT (CURRENT_DATE()) COMMENT '호출 날짜',
    PRIMARY KEY (id)
) COMMENT 'api 중복호출 방지';

CREATE TABLE weather_forecast
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    category       VARCHAR(50),
    forecast_date  DATE,
    forecast_time  VARCHAR(10),
    forecast_value VARCHAR(50),
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    url            TEXT

);

#     api_call_id INT,
#     CONSTRAINT fk_api_call_id FOREIGN KEY (api_call_id) REFERENCES last_call_api_date(id)

select *
from short_weather;

select tp.id                     as tp_id,
       tp.travel_class_detail_id AS tp_travel_class_detail_id,
       tp.last_call_api_id       AS tp_last_call_api_id,
       tp.sigungucode_id         AS tp_sigungucode_id,
       tp.title                  AS tp_title,
       tp.addr1                  AS tp_addr1,
       tp.addr2                  AS tp_addr2,
       tp.contentid              AS tp_contentid,
       tp.firstimage             AS tp_firstimage,
       tp.firstimage2            AS tp_firstimage2,
       tp.cpyrhtDivCd            AS tp_cpyrhtDivCd,
       tp.mapx                   AS tp_mapx,
       tp.mapy                   AS tp_mapy,
       tp.modifiedtime           AS tp_modifiedtime,
       tp.tel                    AS tp_tel,
       tp.eventstartdate         AS tp_eventstartdate,
       tp.eventenddate           AS tp_eventenddate,
       tp.infocenter             AS tp_infocenter,
       tp.parking                AS tp_parking,
       tp.restdate               AS tp_restdate,
       tp.usetime                AS tp_usetime,
       tp.homepage               AS tp_homepage,
       tp.overview               AS tp_overview,
       tp.eventplace             AS tp_eventplace,
       tp.playtime               AS tp_playtime,
       tp.usetimefestival        AS tp_usetimefestival,
       tp.infoname               AS tp_infoname,
       tp.infotext               AS tp_infotext,
       tcd.id                   AS tcd_id,
       tcd.travel_type_id       AS tcd_travel_type_id,
       tcd.name                 AS tcd_name,
       tcd.code                 AS tcd_code,
       tcd.decode               AS tcd_decode,
       tt.id                     AS tt_id,
       tt.name                   AS tt_name,
       s.id                      AS s_id,
       s.sigungucode             AS s_sigungucode,
       s.name                    AS s_name,
       s.areacode                AS s_areacode,
       a.areacode                AS a_areacode,
       a.name                    AS a_name,
       a.regId                   AS a_regId,
       lcad.id                   AS lcad_id,
       lcad.url                  AS lcad_url,
       lcad.regdate              AS lcad_regdate
FROM travel_post tp
         JOIN travel_class_detail tcd on tcd.id = tp.travel_class_detail_id
         JOIN travel_class_detail tcd2 ON tcd.decode = tcd2.code
         JOIN travel_type tt ON tcd.travel_type_id = tt.id
         JOIN sigungucode s ON tp.sigungucode_id = s.id
         JOIN areacode a ON s.areacode = a.areacode
         JOIN last_call_api_date lcad ON tp.last_call_api_id = lcad.id
where
    tt.id= 12
# and
#     tcd2.decode = 'a02'
#     a.areacode =1
# and
#     s.sigungucode = 1
# and
#     STR_TO_DATE(tp.eventstartdate, '%Y%m%d') >= CURDATE()
# order by tp.eventstartdate asc
  and
    tp.overview is null
#     tp.contentid = 2702596
;


SELECT distinct
    tcd.*
FROM
    travel_class_detail tcd
        LEFT JOIN
    travel_class_detail tcd2 ON tcd.decode = tcd2.code
        join travel_type tt on tcd.travel_type_id = tt.id
# where
# tcd2.code = 'a0101'
#   and tcd2.decode = 'a01'
#   and
#     tcd2.code = 'a0101'
#   and tcd3.code = 'a01011300'
#   and
#   tcd3.travel_type_id = 12
;




select * from user;
select * from short_weather;
select * from middle_weather;
select * from last_call_api_date;
select * from areacode;
select * from sigungucode;
select * from blog_review;
select * from travel_post;
select * from travel_diary_post;
select * from travel_type;

select *
from sigungucode;
