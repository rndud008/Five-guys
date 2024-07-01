
let regiontest1 = getUrlParameter('regionQuery');
let sigungutest2;
let queyrtest3;
let searchQuery;

let loading = false;

let offset = 0;
const limit = 20; // 한 번에 가져올 데이터의 개수

let currentUrl = window.location.href;

const $modal = $('#modalSign');

$(document).ready(function () {
    // 지역 카테고리 클릭 이벤트 처리

    if (regiontest1 !== '') {

        // test 값과 일치하는 지역을 찾습니다.
        let $element = $('.region-category-list').filter(function () {
            return $(this).data('subcategory') === parseInt(regiontest1);
        });

        console.log($element); // 선택된 요소를 콘솔에 출력합니다.

        let nameValue = $element.data('name'); // 선택된 요소의 지역 이름을 가져옵니다.
        console.log(nameValue); // 지역 이름을 콘솔에 출력합니다.


        // 선택한 지역을 1번 항목에 표시
        updateSelectedRegionDisplay(regiontest1, nameValue);

        // 다른 지역 카테고리 링크 숨기기
        hideOtherRegionCategories();

        // 선택한 지역의 시군구 카테고리 표시
        showSelectedRegionSubcategories(regiontest1);

        // 시군구 카테고리 클릭 이벤트 처리
        $('.region-subcategory-list').off('click').click(function (event) {
            event.preventDefault();
            const sigungu = $(this).data('subcategory');
            const sigunguName = $(this).data('name');

            const regionSubCategoryParts = sigungu.split('-');
            sigungutest2 = regionSubCategoryParts[1];

            offset = 0;

            // 선택한 시군구 지역을 1번 항목에 표시
            updateSelectedRegionDisplay(regiontest1, nameValue, sigungu, sigunguName);

            // 선택한 시군구 카테고리 강조
            highlightSelectedSubcategory(regiontest1, sigungu);

            resultCategpryAjex();
        });

    }

    $('#loading').show();
    resultCategpryAjex();

    if (currentUrl.split('?')[0] !== null) {
        let newUrl = currentUrl.split('?')[0];
        history.pushState(null, null, newUrl);
    }

    $(window).scroll(function () {
        if ($(window).scrollTop() + $(window).height() >= $(document).height() - 100) {
            let count = $('.first-item').eq(0).data('value')

            if (!loading && count > offset) {

                $('#loading').show();
                resultCategpryAjex();

            }

        }
    });

    $('.region-category-list').click(function (event) {
        event.preventDefault();
        const region = $(this).data('subcategory');
        const regionName = $(this).data('name');
        regiontest1 = region
        offset = 0;

        // 선택한 지역을 1번 항목에 표시
        updateSelectedRegionDisplay(region, regionName);

        // 다른 지역 카테고리 링크 숨기기
        hideOtherRegionCategories();

        // 선택한 지역의 시군구 카테고리 표시
        showSelectedRegionSubcategories(region);

        resultCategpryAjex();

        // 시군구 카테고리 클릭 이벤트 처리
        $('.region-subcategory-list').off('click').click(function (event) {
            event.preventDefault();
            const sigungu = $(this).data('subcategory');
            const sigunguName = $(this).data('name');

            const regionSubCategoryParts = sigungu.split('-');
            sigungutest2 = regionSubCategoryParts[1];

            offset = 0;

            // 선택한 시군구 지역을 1번 항목에 표시
            updateSelectedRegionDisplay(region, regionName, sigungu, sigunguName);

            // 선택한 시군구 카테고리 강조
            highlightSelectedSubcategory(region, sigungu);

            resultCategpryAjex();
        });
    });

    // 대분류 카테고리 클릭 이벤트 처리
    $('.category-list').click(function (event) {
        event.preventDefault();
        const big = $(this).data('subcategory');
        const bigName = $(this).data('name');

        queyrtest3 = big;

        offset = 0;

        // 선택한 대분류를 1번 항목에 표시
        updateSelectedCategoryDisplay(big, bigName);

        // 다른 대분류 카테고리 링크 숨기기
        hideOtherCategoryCategories();

        // 선택한 대분류의 중분류 카테고리 표시
        showSelectedCategorySubcategories(big);

        resultCategpryAjex();

        // 중분류 카테고리 클릭 이벤트 처리
        $('.middle-list').off('click').on('click', function (event) {
            event.preventDefault();
            const middle = $(this).data('subcategory');
            const middleName = $(this).data('name');

            const subcategoryParts = middle.split('-');

            queyrtest3 = subcategoryParts[1];

            offset = 0;

            // 선택한 중분류를 대분류 1번 항목에 표시
            updateSelectedCategoryDisplay(big, bigName, middle, middleName);

            // 다른 중분류 카테고리 링크 숨기기
            hideOtherCategoryCategories(middle);

            // 선택한 중분류의 소분류 카테고리 표시
            showSelectedCategorySubcategories(middle);

            resultCategpryAjex();

            // 소분류 카테고리 클릭 이벤트 처리
            $('.small-list').off('click').on('click', function (event) {
                event.preventDefault();
                const small = $(this).data('subcategory');
                const smallName = $(this).data('name');

                const subcategoryParts = small.split('-');
                queyrtest3 = subcategoryParts[2];

                offset = 0;

                // 선택한 소분류를 대분류 1번 항목에 표시
                updateSelectedCategoryDisplay(big, bigName, middle, middleName, small, smallName);

                // 선택한 소분류 카테고리 강조
                highlightSelectedCategorySubcategory(small);

                resultCategpryAjex();

            });
        });

    });

    $('#searchForm').on('submit', function (event) {
        event.preventDefault(); // Prevent the form from submitting via the browser

        searchQuery = $('#searchQuery').val();
        offset = 0
        updateSearchResultDisplay(searchQuery);

        resultCategpryAjex()

    });

    $('.search-result').on('click', 'li', function (event) {
        event.preventDefault(); // Prevent the default action of the list

        resetSearchResult()
        searchQuery = ''
        offset = 0

        resultCategpryAjex()

    })

    // 선택한 지역을 초기 상태로 돌려놓는 기능
    $('.region-selected').on('click', 'li', function (event) {
        event.preventDefault(); // Prevent the default action of the list
        const subcategory = $(this).data('subcategory');
        const subcategoryName = $(this).prevAll('[data-name]').first().data('name');

        const subcategoryStr = String(subcategory);

        if (subcategory === undefined || !subcategoryStr.includes('-')) {
            resetSelectedRegion();
            regiontest1 = '';
            sigungutest2 = '';

            offset = 0;

            resultCategpryAjex();
            return;
        }

        const subcategoryParts = subcategory.split('-');

        if (subcategoryParts.length === 2) {
            // '-' 문자가 1개 있는 경우

            const parentContainerId = subcategoryParts[0];
            const categoryValue = subcategoryParts[1];

            regiontest1 = parentContainerId;
            sigungutest2 = '';

            offset = 0;

            $('.region-subcategory-list').css('color', '');

            // 선택한 시군구 지역을 1번 항목에 표시
            updateSelectedRegionDisplay(parentContainerId, subcategoryName);

            // 선택한 시군구 카테고리 강조
            highlightSelectedSubcategory(parentContainerId, categoryValue);

            resultCategpryAjex();

        } else {
            // 지역 항목으로 복귀
            resetSelectedRegion();
            regiontest1 = '';
            sigungutest2 = '';

            offset = 0;

            resultCategpryAjex();
        }

    });

    // 선택한 카테고리 초기 상태로 돌려놓는 기능
    $('.category-selected').on('click', 'li', function (event) {
        event.preventDefault(); // Prevent the default action of the list
        const subcategory = $(this).data('subcategory');
        let firstName = $(this).prevAll('[data-name]').eq(0).data('name');
        let secondName = $(this).prevAll('[data-name]').eq(1).data('name');

        if (subcategory === undefined) {
            resetSelectedCategory();

            queyrtest3 = '';

            offset = 0;

            resultCategpryAjex();
            return;
        }

        const subcategoryParts = subcategory.split('-');

        if (subcategoryParts.length === 2) {
            // '-' 문자가 1개 있는 경우

            const parentContainerId = subcategoryParts[0];
            const categoryValue = subcategoryParts[1];

            queyrtest3 = parentContainerId;

            offset = 0;

            $('.middle-list').show();
            $('.middle-container').hide();
            $('.small-list').css('color', '');

            // 선택한 대분류를 1번 항목에 표시
            updateSelectedCategoryDisplay(parentContainerId, firstName);

            // 다른 대분류 카테고리 링크 숨기기
            hideOtherCategoryCategories();

            // 선택한 대분류의 중분류 카테고리 표시
            showSelectedCategorySubcategories(parentContainerId);

            resultCategpryAjex();
        } else if (subcategoryParts.length === 3) {
            // '-' 문자가 2개 있는 경우

            const parentContainerId = subcategoryParts[0];
            const middleCategoryValue = subcategoryParts[0] + '-' + subcategoryParts[1];
            const smallCategoryValue = subcategoryParts[2];

            queyrtest3 = subcategoryParts[1]

            offset = 0;

            // 선택한 중분류를 대분류 1번 항목에 표시
            updateSelectedCategoryDisplay(parentContainerId, secondName, middleCategoryValue, firstName);

            // 다른 중분류 카테고리 링크 숨기기
            hideOtherCategoryCategories(middleCategoryValue);

            // 선택한 중분류의 소분류 카테고리 표시
            showSelectedCategorySubcategories(middleCategoryValue);

            $('.small-list').css('color', '');

            resultCategpryAjex();

        } else {
            // 대분류 항목으로 복귀
            resetSelectedCategory();

            queyrtest3 = '';

            offset = 0;

            resultCategpryAjex();
        }
    });


    $(document).on('click', '.category-like-button', function (event) {

        event.preventDefault();
        event.stopPropagation();
        event.stopImmediatePropagation();
        console.log('라이크버튼 클릭')

        if (loggedUser === null) {
            alert('로그인이 필요합니다.');
            $modal.show()
        }

        const $button = $(this);
        const postId = $(this).data('post');
        console.log(postId)

        $.ajax({
            url: '/like/postTravelPostLike',
            type: 'POST',
            data: {
                travelPostId: postId
            },
            success: function (response) {
                if (response === ' ') {
                    $button.html(`<i class="fa-regular fa-thumbs-up"></i>`)
                } else {
                    $button.html(`<i class="fa-solid fa-thumbs-up"></i>`)
                }
                console.log('변경완료')
            },
            error: function (xhr, status, error) {
                console.error('Error fetching search results:', error);
            }
        });
    });

});

function getUrlParameter(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    let regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    let results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
}

function resultCategpryAjex() {

    loading = true;

    regiontest1 = regiontest1 || 99;
    sigungutest2 = sigungutest2 || 99;
    queyrtest3 = queyrtest3 || 99;
    searchQuery = searchQuery || 99;

    console.log(regiontest1)
    console.log(sigungutest2)
    console.log(queyrtest3)
    console.log(searchQuery)

    $.ajax({
        url: '/Search/' + categoryId,
        type: 'POST',
        data: {
            Query: queyrtest3,
            regionQuery: regiontest1,
            sigunguQuery: sigungutest2,
            searchQuery: searchQuery,
            offset: offset, // 서버에 전달할 offset 값 추가
            limit: limit   // 서버에 전달할 limit 값 추가
        },
        success: function (response) {
            console.log(this.url);

            if (offset === 0) {
                $('#categoryResults').html(response);
            } else {
                $('#categoryResults').append(response);
            }

            // 로딩 상태 업데이트
            loading = false;
            $('#loading').hide();

            offset += limit;
            loadLikeStatus();

        },
        error: function (xhr, status, error) {
            console.error('Error fetching search results:', error);
            loading = false;
            $('#loading').hide();
        }
    });
}

function loadLikeStatus() {
    console.log("loadLikeStatus 실행")

    $('.category-like-button').each(function () {

        const $button = $(this);

        const postId = $button.data('post');
        console.log(postId)

        $.ajax({
            url: '/like/getTravelPostLike',
            type: 'GET',
            data: {
                travelPostId: postId
            },
            success: function (response) {
                if (response === ' ') {
                    $button.html(`<i class="fa-regular fa-thumbs-up"></i>`)
                } else {
                    $button.html(`<i class="fa-solid fa-thumbs-up"></i>`)
                }

            }.bind(this),
            error: function (xhr, status, error) {
                console.error('Error fetching search results:', error);

            }
        });
    })
}

function updateSelectedRegionDisplay(region, regionName, sigungu = '', sigunguName = '') {
    // 선택한 지역 표시 업데이트
    const selectedRegionDisplay = $('.region-selected');

    const categoryPath = [];
    if (region) {
        categoryPath.push(`<li class="region-selected" data-subcategory="${region}" data-name="${regionName}">${regionName}</li>`);
    }

    if (sigungu) {
        categoryPath.push(`<li class="region-selected" data-subcategory="${sigungu}" data-name="${sigunguName}">${sigunguName}</li>`);
    }

    selectedRegionDisplay.html('<li class="region-selected"> 전체 </li>' + categoryPath.join(''));

}

function hideOtherRegionCategories() {
    // 다른 지역 카테고리 링크 숨기기
    $('.region-category-list').hide();
}

function showSelectedRegionSubcategories(sigungu) {
    // 선택한 지역의 시군구 카테고리 표시
    $('.region-subcategory-container').hide();
    $('#' + sigungu).show();
}

function highlightSelectedSubcategory(area, sigungu) {
    // 선택한 시군구 카테고리 강조
    $('#' + area + ' .region-subcategory-list').css('color', '');
    $('#' + area + ' .region-subcategory-list[data-subcategory="' + sigungu + '"]').css('color', 'red');
}

function resetSelectedRegion() {
    // 선택한 지역 표시 초기화
    $('.region-selected').html('<li class="region-selected"> 전체 </li>');

    // 모든 지역 카테고리 링크 표시
    $('.region-category-list').show();

    // 모든 시군구 카테고리 숨기기
    $('.region-subcategory-container').hide();

    // 시군구 카테고리 강조 초기화
    $('.region-subcategory-list').css('color', '');
}

function updateSearchResultDisplay(searchQueryName) {
    // 선택한 지역 표시 업데이트
    const searchResultDisplay = $('.search-result');

    searchResultDisplay.html(`<li class="search-result" >${searchQueryName}</li>`);

}

function resetSearchResult() {
    // 선택한 카테고리 표시 초기화
    const searchResultDisplay = $('.search-result');
    searchResultDisplay.html('');
    searchResultDisplay.removeAttr('data-subcategory');

}

function updateSelectedCategoryDisplay(big, bigName, middle = '', middleName = '', small = '', smallName = '') {
    // 선택한 대분류 표시 업데이트
    const selectedCategoryDisplay = $('.category-selected');
    const categoryPath = [];
    if (big) {
        categoryPath.push(`<li class="category-selected" data-subcategory="${big}" data-name="${bigName}">${bigName}</li>`);
    }

    if (middle) {
        categoryPath.push(`<li class="category-selected" data-subcategory="${middle}" data-name="${middleName}">${middleName}</li>`);
    }

    if (small) {
        categoryPath.push(`<li class="category-selected" data-subcategory="${small}" data-name="${smallName}">${smallName}</li>`);
    }

    selectedCategoryDisplay.html('<li class="category-selected"> 전체 </li>' + categoryPath.join(''));

}

function hideOtherCategoryCategories(middle = '', small = '') {
    // 다른 대분류 카테고리 링크 숨기기
    $('.category-list').hide();
    if (middle) {
        $('.middle-list').hide();
        $('[data-subcategory="' + middle + '"]').show();
    } else if (small) {
        $('.small-list').hide();
        $('[data-subcategory="' + small + '"]').show();
    } else {
        $('.middle-container, .small-container').hide();
    }
}

function showSelectedCategorySubcategories(big, middle = '') {
    // 선택한 대분류의 중분류 카테고리 표시
    if (middle) {
        $('.middle-container, .small-container').hide();
        $('#' + middle).show();
    } else if (big) {
        $('.middle-container, .small-container').hide();
        $('#' + big).show();
    }
}

function highlightSelectedCategorySubcategory(small) {
    // 선택한 소분류 카테고리 강조
    $('.small-list').css('color', '');
    $('.small-list[data-subcategory="' + small + '"]').css('color', 'red');
}

function resetSelectedCategory() {
    // 선택한 카테고리 표시 초기화
    const selectedCategoryDisplay = $('.category-selected');
    selectedCategoryDisplay.html('<li class="category-selected"> 전체 </li>');
    selectedCategoryDisplay.removeAttr('data-subcategory');

    // 모든 카테고리 링크 표시
    $('.category-list').show();

    // 모든 중분류, 소분류 카테고리 숨기기

    $('.middle-list').show();
    $('.small-list').show();

    $('.middle-container').hide();
    $('.small-container').hide();


    // 소분류 카테고리 강조 초기화
    $('.small-list').css('color', '');
}