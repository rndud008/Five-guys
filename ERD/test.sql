INSERT INTO areacode (areacode, name, regId) VALUES
 (1, '서울', '11B10101'),
 (2, '인천', '11B20201'),
 (3, '대전', '11C20401'),
 (4, '대구', '11H10701'),
 (5, '광주', '11F20501'),
 (6, '부산', '11H20201'),
 (7, '울산', '11H20101'),
 (8, '세종', '11C20404'),
 (31, '경기', '11B10101'),  -- 서울의 regId를 경기에 포함
 (32, '강원', '11D10101'),  -- 철원의 regId를 강원에 포함
 (33, '충북', '11C10101'),  -- 충주의 regId를 충북에 포함
 (34, '충남', '11C20101'),  -- 서산의 regId를 충남에 포함
 (35, '경북', '11H10101'),  -- 울진의 regId를 경북에 포함
 (36, '경남', '11H20301'),  -- 창원의 regId를 경남에 포함
 (37, '전북', '11F10201'),  -- 전주의 regId를 전북에 포함
 (38, '전남', '11F20101'),  -- 함평의 regId를 전남에 포함
 (39, '제주', '11G00201');

select * from areacode;

SELECT name
FROM areacode
WHERE areacode = 9;

SELECT *
FROM sigungucode;

INSERT INTO travel_diary_post (user_id, areacode, subject, content) VALUES
(1, 1, '제목1', '내용1'),
(1, 2, '제목2', '내용2'),
(2, 1, '제목3', '내용3'),
(2, 2, '제목4', '내용4'),
(3, 1, '제목5', '내용5'),
(3, 2, '제목6', '내용6'),
(1, 2, '제목7', '내용7'),
(2, 1, '제목8', '내용8')
;
-- 페이징 실습용 다량의 데이터
INSERT INTO travel_diary_post(user_id, areacode_id, subject, content)
SELECT user_id, areacode_id, subject, content FROM travel_diary_post;

SELECT * FROM travel_diary_post ORDER BY 1 asc ;
SELECT  count(*) FROM travel_diary_post;

DELETE FROM travel_diary_post;
ALTER TABLE travel_diary_post Auto_Increment = 1;

INSERT INTO user (username, password, name, email) VALUES
('USER1', '1234', '회원1', 'user1@mail.com'),
('USER2', '1234', '회원2', 'user2@mail.com'),
('ADMIN1', '1234', '관리자1', 'admin1@mail.com')
;

SELECT  * FROM user;

SELECT *
FROM user
WHERE id = 3;


SELECT * FROM travel_diary_post;

INSERT INTO travel_diary_post (user_id, areacode_id, subject, content)
VALUES (3, 2, '제목', '내용');

UPDATE travel_diary_post
SET content = 'test', subject = 'test'
WHERE id = 1;

DELETE FROM travel_diary_post
WHERE id = 1;

SELECT *
FROM travel_diary_post p, areacode a
WHERE p.areacode_id = a.areacode AND p.areacode_id = 1;

SELECT name
FROM areacode
WHERE areacode = 1;

SELECT * FROM comment;
SELECT * FROM travel_diary_post;

INSERT INTO comment (user_id, travel_diary_post_id, content) VALUES
(1, 1, '1번유저 1번글의 댓글'),
(1, 2, '1번유저 2번글의 댓글'),
(2, 1, '2번유저 1번글의 댓글'),
(2, 2, '2번유저 2번글의 댓글'),
(3, 1, '3번유저 1번글의 댓글'),
(3, 2, '3번유저 2번글의 댓글');

DELETE FROM comment;
ALTER TABLE comment Auto_Increment = 1;

DELETE FROM travel_diary_post;
ALTER TABLE travel_diary_post Auto_Increment = 1;

SELECT c.id, c.user_id, c.travel_diary_post_id, c.content, c.regdate
FROM comment c, travel_diary_post p
WHERE c.user_id = p.id AND c.travel_diary_post_id = 2;

SELECT
    c.id "c_id",
    c.content "c_content",
    c.regdate "c_regdate",
    c.travel_diary_post_id "c_post_id",
    u.id "u_id",
    u.username "u_username",
    u.password "u_password",
    u.name "u_name",
    u.email "u_email",
    u.regdate "u_regdate"
FROM comment c, user u
WHERE c.user_id = u.id AND c.travel_diary_post_id = 1
ORDER BY c.id DESC;

SELECT * FROM comment;

SELECT
    p.id "p_id",
    p.subject "p_subject",
    p.content "p_content",
    p.viewcnt "p_viewcnt",
    p.regdate "p_regdate",
    u.id "u_id",
    u.username "u_username",
    u.name "u_name",
    u.email "u_email",
    u.regdate "u_regdate"
FROM travel_diary_post p, user u
WHERE p.user_id = u.id AND p.areacode_id = 1
ORDER BY p.id DESC
    LIMIT 5, 5;

SELECT
    c.id "c_id",
    c.content "c_content",
    c.regdate "c_regdate",
    c.travel_diary_post_id "c_post_id",
    u.id "u_id",
    u.username "u_username",
    u.password "u_password",
    u.name "u_name",
    u.email "u_email",
    u.regdate "u_regdate"
FROM comment c, user u
WHERE c.user_id = u.id AND c.travel_diary_post_id = 1
ORDER BY c.id DESC;

INSERT INTO attachment(travel_diary_post_id, sourcename, filename) VALUES
(1, 'face01.png', 'face01.png'),
(1, 'face02.png', 'face02.png'),
(2, 'face03.png', 'face03.png'),
(2, 'face04.png', 'face04.png'),
(3, 'face05.png', 'face05.png'),
(3, 'face06.png', 'face06.png'),
(4, 'face07.png', 'face07.png'),
(4, 'face08.png', 'face08.png')
;
DELETE FROM attachment;
ALTER TABLE attachment Auto_Increment = 1;

SELECT * FROM attachment;

SELECT * FROM user_travel_diary_post;
SELECT * FROM user_travel_diary_post WHERE travel_diary_post_id = 2;

INSERT INTO user_travel_diary_post
    (user_id, travel_diary_post_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 1),
    (3, 1)
    ;

SELECT * FROM user_authorities;

DELETE FROM user_travel_diary_post;
ALTER TABLE user_travel_diary_post AUTO_INCREMENT = 1;
# WHERE user_id = 1 AND travel_diary_post_id = 2;

SELECT *
FROM user_travel_diary_post
WHERE user_id = 1 AND travel_diary_post_id = 1;

SELECT count(*)
FROM user_travel_diary_post
WHERE travel_diary_post_id = 1;

INSERT INTO user ( username, password, name, email) VALUES
    ('USER4', '$2a$10$8HVLMZgJLqQTHcFgxgQiSu2FwpUUAHALnW7Iq0PFXkWAXe.Pv6Qqe', '관리자4', 'junho@naver.com');

SELECT * FROM user;

SELECT id, name
FROM authority
WHERE id=1;

SELECT * FROM authority;

INSERT INTO authority (id, name)
VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_MEMBER');


DELETE FROM authority;

insert into user_authorities(user_id, authority_id) VALUES
    (5, 1);

select *
from user_authorities;

delete
from user_authorities
;

select *
from user;

delete
from user;

desc user_authorities;

select * from information_schema.table_constraints where table_name = 'user';
select * from information_schema.table_constraints where constraint_schema = 'travel' ;
