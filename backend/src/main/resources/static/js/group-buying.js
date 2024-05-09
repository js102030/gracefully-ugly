document.addEventListener('DOMContentLoaded', function() {
    const modal = document.querySelector('.modal');
    const modalCloseButton = document.querySelector('.modal-close');
    const listButton = document.querySelector('.list-button');

    if (listButton) {
        listButton.addEventListener('click', function() {
            modal.style.display = 'block';
        });
    }

    if (modalCloseButton) {
        modalCloseButton.addEventListener('click', function() {
            modal.style.display = 'none';
        });
    }

    window.addEventListener('click', function(event) {
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
document.addEventListener('DOMContentLoaded', function() {
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

// ---- username 가져오기
//  ??????????????


// ------------- 공구 참여 버튼 이벤트 헨들러
const joinGroupBuy = document.querySelector('.join-group-buying');
joinGroupBuy.addEventListener('click', event => {
    window.location.href = ('http://localhost:8080/orders/item/' + itemId);
})



// ------------ 알림 모달창 상태 변경

// 페이지 로드 완료 후 실행
    document.addEventListener('DOMContentLoaded', function() {
        console.log(itemId);
        var apiUrl = '/api/groupbuy/items/' + itemId;

        // 서버에서 공동 구매 목록 요청
        fetch(apiUrl)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log(data);

                var groupBuyStatus = data.groupBuyList[0].groupBuyStatus;
                console.log(groupBuyStatus);
                if (groupBuyStatus === 'CANCELLED') {
                    showCancelledModal();
                } else if (groupBuyStatus === 'COMPLETED') {
                    showCompletedModal();
                }
            })
            .catch(error => {
                console.error('Error fetching group buy data:', error);
            });
    });

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
