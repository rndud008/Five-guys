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
where travel_type_id = 15;

select *
FROM travel_post
where areacode is null;

select *
FROM travel_post;

select *
FROM travel_post
where title LIKE '%도드람%';

select *
FROM last_call_api_date;
