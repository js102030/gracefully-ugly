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
// 페이지 로드 시 리뷰 데이터 가져와서 HTML 업데이트
document.addEventListener("DOMContentLoaded", function() {
    var urlParams = new URLSearchParams(window.location.search);
    var itemId = urlParams.get('itemId');

// API 엔드포인트 호출
    fetch(`/api/reviews/items/${itemId}`)
        .then(response => response.json())
        .then(data => {
            console.log(data); // 반환된 데이터 확인

            if (data && data.data && data.data.length > 0) {
                // data 객체와 data.data 배열이 존재하고 비어 있지 않은 경우
                var reviews = data.data;
                var totalReviews = reviews.length;
                var totalStarPoints = 0;

                // 각 리뷰의 별점을 합산
                reviews.forEach(review => {
                    totalStarPoints += review.starPoint;
                });

                // 평균 별점 계산
                var averageStarPoint = totalStarPoints / totalReviews;
                // 평균 별점 표시
                document.querySelector(".point").innerText = averageStarPoint.toFixed(1);

                // 별점 이미지 표시
                var starRatingHtml = '';
                var roundedAverage = Math.round(averageStarPoint);
                for (var i = 0; i < roundedAverage; i++) {
                    starRatingHtml += '<img src="/image/star.png" alt="별" class="star-icon">';
                }
                document.querySelector(".star-review").innerHTML = starRatingHtml;

                // 리뷰 개수 표시
                document.querySelector(".review-count-get").innerText = totalReviews;
            } else {
                console.error('반환된 데이터가 비어 있거나 형식이 올바르지 않습니다.');
            }
        })
        .catch(error => console.error('리뷰 데이터를 불러오는 중 오류가 발생했습니다:', error));
});


// ------------- 리뷰 목록 가져오기


