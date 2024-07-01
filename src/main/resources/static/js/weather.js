// iframe이 로드된 후 실행 -> map.svg 클릭가능
$('.map').on('load', function () {

    // iframe 내부 문서에 접근
    let iframeDocument = this.contentDocument || this.contentWindow.document;

    // jQuery를 사용하여 iframe 내부 요소에 이벤트 리스너 추가
    $(iframeDocument).find(".regionLink").on("click", function (event) {
        event.preventDefault();
        let areacode = $(this).data("areacode");
        let title = $(this).attr("title");

        console.log(areacode);
        console.log(title);

        document.querySelector('.main-right').classList.add('slide-in');

        // 'tableHeader' 내용을 'title' 값 + ' 주간 예보'로 변경
        $("#tableHeader").text(title + " 주간 예보");



        // 현재 날짜를 가져오기 (YYYY-MM-DD 형식)
        let today = new Date().toISOString().slice(0, 10);

        // 예보 기준날짜 가져오기
        let currentDate = new Date();
        let year = currentDate.getFullYear();
        let month = currentDate.getMonth() + 1; // 월은 0부터 시작하므로 +1 필요
        let day = currentDate.getDate();
        let formattedDate = year + "년 " + month + "월 " + day + "일 예보 기준";

        document.getElementById("current-date").innerText = formattedDate;
        // 관광지 살펴보기, 축제 살펴보기 버튼 보이기
        $("#link-container button").removeClass("d-none")

        // 관광지 살펴보기 버튼
        $("#tourismButton").on("click", function () {
            $(this).removeClass("d-none");
            $(this).find('a').attr('href', `/categoryTable/12?regionQuery=${areacode}`);
        });

        // 축제·공연·행사 버튼
        $("#festivalButton").on("click", function () {
            $(this).removeClass("d-none");
            $(this).find('a').attr('href', `/categoryTable/15?regionQuery=${areacode}`);
        });

        // AJAX 요청1: 단기예보 정보 업데이트
        $.ajax({
            url: "/weather/update1",
            type: "POST",
            data: {
                areacode: areacode
            },
            success: function () {
                // AJAX 요청2: 업데이트된 단기예보 정보 가져오기
                $.ajax({
                    url: "/weather/getWeather1",
                    type: "GET",
                    data: {
                        areacode: areacode
                    },
                    success: function (response) {
                        let tbody = $("#weatherTableBody");
                        let tbody2 = $("#weather2TableBody");

                        // 초기화
                        tbody.empty();
                        tbody2.empty();

                        // 날짜 및 시간 기준으로 오름차순 정렬
                        response.sort((a, b) => {
                            if (a.fcstDate !== b.fcstDate) {
                                return a.fcstDate.localeCompare(b.fcstDate);
                            } else {
                                return a.fcstTime.localeCompare(b.fcstTime);
                            }
                        });

                        let weatherData = {};
                        let midWeatherData = {};

                        response.forEach(function (short_weather) {
                            if (short_weather.fcstDate >= today) {
                                if (!weatherData[short_weather.fcstDate]) {
                                    weatherData[short_weather.fcstDate] = {
                                        morning: {
                                            sky: "",
                                            tmn: "",
                                            tmx: ""
                                        },
                                        afternoon: {
                                            sky: "",
                                            tmn: "",
                                            tmx: ""
                                        }
                                    };
                                }

                                if (short_weather.fcstTime === "0600") {
                                    weatherData[short_weather.fcstDate].morning = {
                                        sky: short_weather.sky,
                                        tmn: short_weather.tmn,
                                        tmx: short_weather.tmx,
                                        pop: short_weather.pop
                                    };
                                } else if (short_weather.fcstTime === "1500") {
                                    weatherData[short_weather.fcstDate].afternoon = {
                                        sky: short_weather.sky,
                                        tmn: short_weather.tmn,
                                        tmx: short_weather.tmx,
                                        pop: short_weather.pop
                                    };
                                }
                            }
                        });

                        // 날씨 예보 테이블에 데이터 추가
                        for (let date in weatherData) {
                            let morning = weatherData[date].morning;
                            let afternoon = weatherData[date].afternoon;
                            // 날짜 변환 함수 호출
                            let formattedDate = formatDateW(date);

                            // 소수점 제거
                            let morningTmn = parseInt(morning.tmn);
                            let afternoonTmx = parseInt(afternoon.tmx);


                            let row = "<tr>" +
                                "<td>" + formattedDate + "</td>" +
                                "<td><span class='rain-blue-left'>" + morning.pop + "%</span></td>" +
                                "<td><img src='" + shortWeatherGif(morning.sky) + "' alt='" + weatherText(morning.sky) + "'></td>" +
                                "<td><img src='" + shortWeatherGif(afternoon.sky) + "' alt='" + weatherText(afternoon.sky) + "'></td>" +
                                "<td><span class='rain-blue-right'>" + afternoon.pop + "%</span></td>" +
                                `<td><span class='blue'>${morningTmn}℃</span> <span class='gray'>/</span> <span class='red'>${afternoonTmx}℃</span></td>` +
                                "</tr>";

                            tbody.append(row);
                        }

                        // AJAX 요청3: 중기예보 정보(기온) 업데이트
                        $.ajax({
                            url: "/weather/update2",
                            type: "POST",
                            data: {
                                areacode: areacode
                            },
                            success: function () {
                                // AJAX 요청4: 업데이트된 중기예보 정보(기온) 가져오기
                                $.ajax({
                                    url: "/weather/getWeather2",
                                    type: "GET",
                                    data: {
                                        areacode: areacode
                                    },
                                    success: function (response) {
                                        // 초기화
                                        tbody2.empty();

                                        // 현재 날짜 객체 생성
                                        let currentDate = new Date();

                                        // response는 4일부터 7일까지의 중기 날씨 예보 데이터
                                        for (let i = 0; i < response.length; i++) {
                                            let mid_weather = response[i]; // 각 날짜의 예보 데이터를 가져옵니다

                                            // 날짜 초기화 - 현재 날짜로 설정
                                            currentDate.setDate(currentDate.getDate() + 2);

                                            // 4일부터 7일까지의 날짜 생성
                                            for (let j = 4; j < 8; j++) {
                                                // 날짜 설정 - j일 후로 설정
                                                currentDate.setDate(currentDate.getDate() + 1);

                                                // 현재 월의 마지막 날짜 가져오기
                                                let lastDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0).getDate();

                                                // 만약 현재 날짜가 월의 마지막 날짜를 초과하는 경우, 다음 달로 넘어감
                                                if (currentDate.getDate() > lastDayOfMonth) {
                                                    currentDate.setDate(1); // 다음 달로 넘어가기 위해 1일로 설정
                                                    currentDate.setMonth(currentDate.getMonth() + 1); // 다음 달로 설정
                                                }

                                                // 형식화된 날짜 생성 (예: "금 6.28" 형식)
                                                let formattedDate = formatDateMid(currentDate);

                                                // 행(ROW)의 HTML을 구성합니다
                                                let row = "<tr>" +
                                                    "<td>" + formattedDate + "</td>" +
                                                    "<td><span class='rain-blue-left'>" + mid_weather["rnSt" + j + "Am"] + "%</span></td>" +
                                                    "<td><img src='" + middleWeatherGif(mid_weather["wf" + j + "Am"]) + "' alt='" + mid_weather["wf" + j + "Am"] + "'></td>" +
                                                    "<td><img src='" + middleWeatherGif(mid_weather["wf" + j + "Pm"]) + "' alt='" + mid_weather["wf" + j + "Pm"] + "'></td>" +
                                                    "<td><span class='rain-blue-right'>" + mid_weather["rnSt" + j + "Pm"] + "%</span></td>" +
                                                    `<td><span class='blue'>${mid_weather["taMin" + j]}℃</span> <span class='gray'>/</span> <span class='red'>${mid_weather["taMax" + j]}℃</span></td>` +
                                                    "</tr>";


                                                // 행을 tbody에 추가합니다
                                                tbody2.append(row);
                                            }
                                        }
                                    },
                                    error: function () {
                                        // 에러 처리 - 업데이트된 중기예보 정보(기온) 가져오기 실패
                                        console.log("업데이트된 중기예보 정보(기온)를 가져오는 데 실패했습니다.");
                                    }
                                });
                            },
                            error: function () {
                                // 에러 처리 - 중기예보 정보(기온) 업데이트 실패
                                console.log("중기예보 정보(기온)를 업데이트하는 데 실패했습니다.");
                            }
                        });
                    },
                    error: function () {
                        // 에러 처리 - 업데이트된 중기예보 정보(기온) 가져오기 실패
                        console.log("업데이트된 중기예보 정보(기온)를 가져오는 데 실패했습니다.");
                    }
                });
            },
            error: function () {
                // 에러 처리 - 중기예보 정보(기온) 업데이트 실패
                console.log("중기예보 정보(기온)를 업데이트하는 데 실패했습니다.");
            }
        });
    });
});

function formatDateW(dateString) {
    // 날짜 문자열을 Date 객체로 변환
    let dateObj = new Date(dateString);

    // 요일 배열
    let daysOfWeek = ["일", "월", "화", "수", "목", "금", "토"];

    // 월, 일, 요일 추출
    let month = dateObj.getMonth() + 1; // 월은 0부터 시작하므로 1을 더함
    let day = dateObj.getDate();
    let dayOfWeek = daysOfWeek[dateObj.getDay()];

    return `<span class="dayOfWeek">${dayOfWeek}</span><br>
            <span class="date">${month}.${day}</span>`;
}

function formatDateMid(dateObj) {
    // 요일 배열
    let daysOfWeek = ["일", "월", "화", "수", "목", "금", "토"];

    // 월, 일, 요일 추출
    let month = dateObj.getMonth() + 1; // 월은 0부터 시작하므로 1을 더함
    let day = dateObj.getDate();
    let dayOfWeek = daysOfWeek[dateObj.getDay()];

    // 형식에 맞게 문자열 반환
    return `<span class="dayOfWeek">${dayOfWeek}</span><br>
            <span class="date">${month}.${day}</span>`;
}

function shortWeatherGif(sky) {
    const basePath = "/img/";
    switch (sky) {
        case "1":
            return basePath + "sun.gif";
        case "3":
            return basePath + "blur.gif";
        case "4":
            return basePath + "cloudy.gif";
        default:
            return "default.gif"; // 기본 이미지
    }
}

function middleWeatherGif(description) {
    const basePath = "/img/";
    switch (description) {
        case "맑음":
            return basePath + "sun.gif";
        case "흐림":
            return basePath + "blur.gif";
        case "구름많음":
            return basePath + "cloudy.gif";
        case "흐리고 비":
            return basePath + "rain.gif";
        case "구름많고 비":
            return basePath + "rain.gif";
        default:
            return basePath + "default.gif"; // 기본 이미지
    }
}

function weatherText(sky) {
    switch (sky) {
        case "1":
            return "맑음";
        case "3":
            return "구름 많음";
        case "4":
            return "흐림";
        case "5":
            return "비";
        case "6":
            return "눈/비";
        case "7":
            return "눈";
        default:
            return "정보 없음";
    }
}

const modal = document.getElementById('modalSignin');
const openModalBtn = document.querySelector('.login');

if (modal !== null) {
    openModalBtn.addEventListener('click', () => {
        $('#modalSignin').modal('show'); // jQuery를 사용하여 모달을 표시합니다.
        centerModal(); // 모달을 중앙 정렬하는 함수 호출
    });

    const closeModalBtn = document.querySelector('.modal .close');
    closeModalBtn.addEventListener('click', () => {
        $('#modalSignin').modal('hide'); // jQuery를 사용하여 모달을 숨깁니다.
    });

    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            $('#modalSignin').modal('hide'); // jQuery를 사용하여 모달을 숨깁니다.
        }
    });

    function centerModal() {
        const dialog = modal.querySelector('.modal-dialog');
        if (dialog.offsetHeight < window.innerHeight) {
            dialog.style.marginTop = (window.innerHeight - dialog.offsetHeight) / 2 + 'px';
        } else {
            dialog.style.marginTop = '';
        }
    }

    window.addEventListener('DOMContentLoaded', () => {
        modal.style.display = 'none'; // 페이지가 로드될 때 모달이 숨겨지도록 설정합니다.
    });
}
