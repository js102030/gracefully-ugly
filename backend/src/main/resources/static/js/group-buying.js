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
document.addEventListener('DOMContentLoaded', function() {
    // Item의 closedDate 변수를 체크하여 모달을 표시
    checkItemStatus();
});

// ------------- 별점 보여주기

document.addEventListener('DOMContentLoaded', function() {
    // 페이지가 로드된 후 실행되는 코드

    // 서버에서 전달받은 review 객체 (이 예시에서는 임의의 데이터를 사용)
    const review = {
        starPoint: 4 // 실제로는 서버에서 전달받은 데이터
    };

    // 별점을 표시할 부모 요소를 선택
    const starContainer = document.querySelector('.star-review');

    // 서버에서 전달받은 review.starPoint의 값에 따라 별점 이미지를 동적으로 생성하여 추가
    for (let i = 0; i < review.starPoint; i++) {
        const starImg = document.createElement('img');
        starImg.src = '/image/star.png';
        starImg.alt = '별점';
        starContainer.appendChild(starImg);
    }
});

