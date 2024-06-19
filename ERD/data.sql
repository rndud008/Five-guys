# 지역코드
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

# Authority
INSERT INTO authority (id, name) VALUES
                                     (1, 'ADMIN'),
                                     (2, 'MEMBER');

select * from authority;