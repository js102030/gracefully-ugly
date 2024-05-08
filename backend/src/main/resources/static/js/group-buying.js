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

// ------------ 알림 모달창 상태 변경


// ------------- 리뷰 별 개수 설정
document.addEventListener('DOMContentLoaded', function() {
    const reviewPoint = document.querySelector('.point').textContent; // HTML 요소에서 리뷰 평점 가져오기

    const starReviewContainer = document.querySelector('.star-review'); // 별점을 표시할 컨테이너 요소 선택

    // 리뷰 평점에 따라 별점 이미지 동적 생성
    function generateStarRating(point) {
        const roundedPoint = Math.round(point); // 리뷰 평점 반올림

        // 별점 이미지를 생성하여 컨테이너에 추가
        for (let i = 0; i < roundedPoint; i++) {
            const starImg = document.createElement('img');
            starImg.src = '/image/star.png';
            starImg.alt = '별점';
            starReviewContainer.appendChild(starImg);
        }
    }

    generateStarRating(reviewPoint); // 별점 생성 함수 호출
});

// ------------- 리뷰 목록 가져오기
// 현재 페이지의 URL에서 itemId 가져오기
const urlParams = new URLSearchParams(window.location.search);
const itemId = urlParams.get('itemId');

