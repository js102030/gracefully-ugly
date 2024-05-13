// 현재 페이지의 URL에서 itemId 가져오기
const pathSegments = window.location.pathname.split('/');
const itemId = pathSegments[pathSegments.length - 1];

// 닉네임 가져오기
document.addEventListener('DOMContentLoaded', function() {
    var userIdElement = document.querySelector('.qna-userId');

    if (userIdElement) {
        var userId = userIdElement.textContent.trim();

        var apiUrl = '/users/' + userId;

        $.get(apiUrl, function(data) {
            var username = data.username;
            userIdElement.textContent = username;
        }).fail(function() {
            console.error('Failed to fetch user data');
        });
    }
});

// 리뷰 & 문의 게시판 보러가기
const reviewButton = document.querySelector('.goToReview-button');
reviewButton.addEventListener('click', event => {
    window.location.href = '/group-buying/' + itemId;
})

const askButton = document.querySelector('.goToAsk-button');
askButton.addEventListener('click', event => {
    window.location.href = `/productAsk/` + itemId;
})

