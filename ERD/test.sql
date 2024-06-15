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

drop table test;

CREATE TABLE test
(
    var varchar(10) not null ,
    date timestamp        NOT NULL DEFAULT current_timestamp
# last api date type
) COMMENT '지역(areacode)';

insert into test
(var, date) values ('asdfa', current_date);
# 저장은 커렌트 데이트로 해야함.?

select *
from test;