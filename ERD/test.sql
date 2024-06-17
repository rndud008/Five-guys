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

select tp.*
FROM travel_post tp
         join travel_class_detail tcd on tp.travel_class_detail_id = tcd.id
         join travel_type tt on tcd.travel_type_id = tt.id
where tp.contentid = 2865100
  AND tt.id = 12
