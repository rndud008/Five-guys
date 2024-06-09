
# 특정 지역 ID만 선택한 경우: 게시글 출력
SELECT *
FROM traverl_post
WHERE areacode_id = [선택한 지역 ID] AND sigungucode_id IS NULL;

# 특정 지역 ID와 시군구 ID를 모두 선택한 경우: 게시글 출력
SELECT *
FROM traverl_post
WHERE areacode_id = [선택한 지역 ID] AND sigungucode_id = [선택한 시군구 ID];