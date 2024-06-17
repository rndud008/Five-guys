

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
WHERE areacode = 1;


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

SELECT *
FROM sigungucode
where areacode = 2 and sigungucode = 3;

INSERT INTO travel_diary_post (user_id, areacode_id, subject, content) VALUES
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

SELECT  * FROM travel_diary_post;
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
