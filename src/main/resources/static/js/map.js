var mapContainer = document.getElementById('map'); // 지도를 표시할 div
var markers = []; // 모든 마커를 담을 배열
var categoryMarkers = []; // 카테고리 검색 결과 마커를 담을 배열
var post = /*[[${travelPosts}]]*/ []; // Thymeleaf 템플릿 문법으로 데이터 출력

var centerCoords = new kakao.maps.LatLng(post.mapy, post.mapx); // 초기 중심 좌표

var mapOption = {
    center: centerCoords,
    level: 2 // 지도의 확대 레벨
};

var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

// 기존 마커 표시
post.forEach(function (post) {
    var markerPosition = new kakao.maps.LatLng(post.mapy, post.mapx);
    var marker = new kakao.maps.Marker({
        position: markerPosition,
    });
    marker.setMap(map); // 마커를 지도에 표시
    markers.push(marker); // markers 배열에 추가
});

// 각 카테고리에 클릭 이벤트를 등록합니다
var category = document.getElementById('category'),
    children = category.children;

for (var i = 0; i < children.length; i++) {
    children[i].onclick = onClickCategory;
}


// 클릭된 카테고리에만 클릭된 스타일을 적용하는 함수입니다
function changeCategoryClass(el) {
    for (var i = 0; i < children.length; i++) {
        children[i].className = '';
    }
    if (el) {
        el.className = 'on';
    }
}

// 카테고리를 클릭했을 때 호출되는 함수입니다
function onClickCategory() {
    var id = this.id,
        className = this.className;

    hidePlaceInfo();

    if (className === 'on') {
        currCategory = ''; // 현재 카테고리 초기화
        changeCategoryClass(); // 모든 카테고리 클래스 제거
        removeCategoryMarkers(); // 카테고리 검색 결과 마커 제거
    } else {
        currCategory = id; // 현재 카테고리 설정
        changeCategoryClass(this); // 해당 카테고리 클래스 설정
        searchPlaces(); // 장소 검색 실행
    }
}

// 카테고리 검색을 요청하는 함수입니다
function searchPlaces() {
    if (!currCategory) {
        return;
    }

    // 기존 카테고리 검색 결과 마커를 제거합니다
    removeCategoryMarkers();

    // 카테고리 검색 요청
    var ps = new kakao.maps.services.Places(map);
    ps.categorySearch(currCategory, placesSearchCB, {useMapBounds: true});
}

// 장소 검색이 완료된 후 호출되는 콜백 함수입니다
function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {
        // 검색 결과가 정상적으로 나왔을 때 마커 표시
        displayCategoryPlaces(data);
    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        // 검색 결과가 없을 때 처리
        console.log("검색 결과가 없습니다.");
    } else if (status === kakao.maps.services.Status.ERROR) {
        // 검색 중 오류 발생 시 처리
        console.error("검색 중 오류가 발생했습니다.");
    }
}

// 카테고리 검색 결과 마커를 모두 제거합니다
function removeCategoryMarkers() {
    for (var i = 0; i < categoryMarkers.length; i++) {
        categoryMarkers[i].setMap(null);
    }
    categoryMarkers = []; // 배열 초기화
}

// 카테고리 검색 결과를 지도에 표시하는 함수입니다
function displayCategoryPlaces(places) {
    for (var i = 0; i < places.length; i++) {
        var marker = addCategoryMarker(places[i]);
        categoryMarkers.push(marker); // categoryMarkers 배열에 추가
    }
}

// 카테고리 검색 결과 마커를 생성하고 지도에 표시하는 함수입니다
function addCategoryMarker(place) {
    var imageSize = new kakao.maps.Size(35, 35), // 마커 이미지의 크기
        imageSrc = getImageSrc(currCategory), // 카테고리에 따른 마커 이미지 URL
        imgOptions = {
            offset: new kakao.maps.Point(18, 28) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
        markerPosition = new kakao.maps.LatLng(place.y, place.x);

    var marker = new kakao.maps.Marker({
        position: markerPosition,
        image: markerImage
    });

    marker.setMap(map); // 지도에 마커 표시
    kakao.maps.event.addListener(marker, 'click', function () {
        displayPlaceInfo(place); // 마커 클릭 시 상세 정보 표시
    });

    return marker;
}

// 카테고리에 따른 마커 이미지 URL을 반환하는 함수입니다
function getImageSrc(category) {
    switch (category) {
        case 'CE7': // 카페
            return '/image/coffee.png';
        case 'FD6': // 음식점
            return '/image/food.png';
        case 'PK6': // 주차장
            return '/image/car.png';
        default:
            return '';
    }
}

// 클릭한 마커에 대한 장소 상세 정보를 커스텀 오버레이로 표시하는 함수입니다
var placeOverlay = new kakao.maps.CustomOverlay({
    zIndex: 1
});

var contentNode = document.createElement('div');
contentNode.className = 'placeinfo_wrap';

addEventHandle(contentNode, 'mousedown', kakao.maps.event.preventMap);
addEventHandle(contentNode, 'touchstart', kakao.maps.event.preventMap);

placeOverlay.setContent(contentNode);

function displayPlaceInfo(place) {
    var content = '<div class="placeinfo">' +
        '   <a class="title" href="' + place.place_url + '" target="_blank" title="' + place.place_name + '">' + place.place_name + '</a>';

    if (place.road_address_name) {
        content += '    <span title="' + place.road_address_name + '">' + place.road_address_name + '</span>' +
            '  <span class="jibun" title="' + place.address_name + '">(지번 : ' + place.address_name + ')</span>';
    } else {
        content += '    <span title="' + place.address_name + '">' + place.address_name + '</span>';
    }

    content += '    <span class="tel">' + place.phone + '</span>' +
        '</div>' +
        '<div class="after"></div>';

    contentNode.innerHTML = content;
    placeOverlay.setPosition(new kakao.maps.LatLng(place.y, place.x));
    placeOverlay.setMap(map);
}

function hidePlaceInfo() {
    placeOverlay.setMap(null);
}

// 엘리먼트에 이벤트 핸들러를 등록하는 함수입니다
function addEventHandle(target, type, callback) {
    if (target.addEventListener) {
        target.addEventListener(type, callback);
    } else {
        target.attachEvent('on' + type, callback);
    }
}

//<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=22d9b94181d1047695b69ce686213e6a&libraries=services"></script>
// <script th:inline="javascript">

// @GetMapping("/map1")
// public String showMap1(Model model) {
//     List<TravelPost> travelPosts = travelPostService.findAll();
//     model.addAttribute("travelPosts", travelPosts);
//     System.out.println(travelPosts); // 데이터 출력 확인
//     return "map1";
// }