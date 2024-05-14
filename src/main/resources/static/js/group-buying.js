// 현재 페이지의 URL에서 itemId 가져오기
const pathSegments = window.location.pathname.split('/');
const itemId = pathSegments[pathSegments.length - 1];


document.addEventListener('DOMContentLoaded', function () {
    const modal = document.querySelector('.modal');
    const modalCloseButton = document.querySelector('.modal-close');
    const listButton = document.querySelector('.list-button');

    if (listButton) {
        listButton.addEventListener('click', function () {
            modal.style.display = 'block';
        });
    }

    if (modalCloseButton) {
        modalCloseButton.addEventListener('click', function () {
            modal.style.display = 'none';
        });
    }

    window.addEventListener('click', function (event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
});


// ---- 메인 리뷰 평균
document.addEventListener('DOMContentLoaded', () => {
    const reviewElements = document.querySelectorAll('.review'); // 모든 리뷰 요소 선택
    let totalStarPoint = 0;
    let reviewCount = 0;

    // 각 리뷰의 starPoint 값을 누적하여 총합과 개수를 구함
    reviewElements.forEach(reviewElement => {
        const starPointElement = reviewElement.querySelector('.point');
        if (starPointElement) {
            const starPoint = parseFloat(starPointElement.textContent);
            totalStarPoint += starPoint;
            reviewCount++;
        }
    });

    // 리뷰가 존재할 경우, 평균 starPoint 계산
    let averageStarPoint = reviewCount > 0 ? totalStarPoint / reviewCount : 0;

    // 평균 starPoint 값을 페이지에 출력
    const averageStarPointElement = document.querySelector('.star-review .point');
    if (averageStarPointElement) {
        averageStarPointElement.textContent = averageStarPoint.toFixed(1); // 소수점 한 자리까지 표시
    }
});


// --- 메인 리뷰 별 개수 설정
document.addEventListener('DOMContentLoaded', function () {
    const reviewPoint = document.querySelector('.point').textContent;

    const starReviewContainer = document.querySelector('.star-review');

    function generateStarRating(point) {
        const roundedPoint = Math.round(point); // 리뷰 평점 반올림

        for (let i = 0; i < roundedPoint; i++) {
            const starImg = document.createElement('img');
            starImg.src = '/image/star.png';
            starImg.alt = '별점';
            starReviewContainer.appendChild(starImg);
        }
    }

    generateStarRating(reviewPoint); // 별점 생성 함수 호출
});

// ---------- 리뷰 목록 별 개수 설정
function renderStarRating(starPoint) {
    const starContainer = document.createElement('div');
    starContainer.classList.add('star-review');

    const fullStars = Math.round(starPoint); // 반올림된 별점 개수

    for (let i = 0; i < fullStars; i++) {
        const starImg = document.createElement('img');
        starImg.src = '/image/star.png'; // 별 이미지 파일 경로
        starImg.alt = '별점';
        starContainer.appendChild(starImg);
    }

    return starContainer;
}

document.addEventListener('DOMContentLoaded', () => {
    const reviewElements = document.querySelectorAll('.review');
    reviewElements.forEach(reviewElement => {
        const starPoint = parseFloat(reviewElement.querySelector('.point').textContent);
        const starContainer = renderStarRating(starPoint);

        const existingStarContainer = reviewElement.querySelector('.star-review');
        existingStarContainer.replaceWith(starContainer);
    });
});

// ---- nickname 가져오기
$(document).ready(function () {
    console.log(userId);
    var apiUrl = '/api/users/' + userId;

    $.get(apiUrl, function (data) {
        var nickname = data.nickname;

        $('.nickname').text(nickname);
    })
        .fail(function () {
            // API 호출 실패 시 에러 처리
            console.error('Failed to fetch user data');
        });
});


// ------------- 공구 참여 버튼 이벤트 헨들러
const joinGroupBuy = document.querySelector('.join-group-buying');
joinGroupBuy.addEventListener('click', event => {
    window.location.href = ('http://localhost:8080/orders/item/' + itemId);
})

// -------------  문의하기 버튼 이벤트 헨들러

/*const questions = document.querySelector('.ask');
questions.addEventListener('click', event => {
    window.location.href = `/productAsk/` + itemId;
})*/

// ------------ 알림 모달창 상태 변경
// document.addEventListener('DOMContentLoaded', function () {
//     console.log(itemId);
//     var apiUrl = '/api/groupbuy/items/' + itemId;
//
//     const endTime = document.querySelector('.end-time').innerText;
//
//     fetch(apiUrl)
//         .then(response => {
//             if (!response.ok) {
//                 throw new Error('Network response was not ok');
//             }
//             return response.json();
//         })
//         .then(data => {
//             console.log(data);
//             var groupBuyStatus = data.groupBuyList[0].groupBuyStatus;
//             console.log(groupBuyStatus);
//             if (groupBuyStatus === 'CANCELLED' || Date.parse(endTime) <= Date.now()) {
//                 showCancelledModal();
//                 hideButtons();
//             } else if (groupBuyStatus === 'COMPLETED') {
//                 showCompletedModal();
//                 hideButtons();
//             }
//         })
//         .catch(error => {
//             console.error('Error fetching group buy data:', error);
//
//             if (Date.parse(endTime) <= Date.now()) {
//                 showCancelledModal();
//                 hideButtons();
//             }
//         });
// });

function showCancelledModal() {
    var modal = document.querySelector('.notice-modal-CANCELLED');
    if (modal) {
        modal.style.display = 'block';
    }
}

function showCompletedModal() {
    var modal = document.querySelector('.notice-modal-COMPLETED');
    if (modal) {
        modal.style.display = 'block';
    }
}

// function hideButtons() {
//     // 공구 참여 버튼과 찜하기 버튼 숨기기
//     var joinButton = document.querySelector('.join-group-buying');
//     var cartButton = document.querySelector('.join-cart');
//
//     if (joinButton) {
//         joinButton.style.display = 'none';
//     }
//
//     if (cartButton) {
//         cartButton.style.display = 'none';
//     }
// }


// ------------- 찜 하기 버튼 이벤트 핸들러
const joinCart = document.querySelector('.join-cart');
joinCart.addEventListener('click', event => {
    $.ajax({
        url: "http://localhost:8080/api/cart/" + itemId,
        method: 'post',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            itemCount: 1
        }),
        success: function (data) {
            alert(data.message);
        },
        error: function (data, status, error) {
            alert('찜 목록에 추가 도중 문제가 발생했습니다.\n[status: ' + data.status + ']\n[error: ' + data.responseText + ']');
        }
    })
})

// ---- 글 수정
function showEditForm() {
    document.getElementById('editFormContainer').style.display = 'block';
    var currentDescription = document.querySelector('.description-text').textContent.trim();
    document.getElementById('editDescription').value = currentDescription;
}

function submitEdit() {
    var updatedDescription = document.getElementById('editDescription').value;

    $.ajax({
        type: 'PUT',
        url: '/api/items/' + itemId,
        contentType: 'application/json',
        data: JSON.stringify({description: updatedDescription}),
        success: function (response) {
            console.log('글 수정 완료:', response);
            window.location.reload();
        },
        error: function (xhr, status, error) {
            console.error('글 수정 실패:', error);
        }
    });

    // 수정 폼을 숨김
    document.getElementById('editFormContainer').style.display = 'none';
}

// --- 글 삭제
function deleteItem() {
    if (confirm('정말로 삭제하시겠습니까?')) {
        $.ajax({
            type: 'DELETE',
            url: '/api/items/' + itemId,
            success: function () {
                alert('글이 삭제되었습니다. 메인 페이지로 이동합니다.');
                window.location.href = '/';
            },
            error: function (xhr, status, error) {
                console.error('글 삭제 실패:', error);
            }
        });
    }
}
