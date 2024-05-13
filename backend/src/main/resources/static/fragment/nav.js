document.addEventListener("DOMContentLoaded", function () {
    function getCookie(name) {
        const cookies = document.cookie.split(';');
        for (let i = 0; i < cookies.length; i++) {
            const cookie = cookies[i].trim();
            if (cookie.startsWith(name + '=')) {
                return cookie.substring(name.length + 1);
            }
        }
        return null;
    }

    //로그인
    var loginButton = document.querySelector('.login-button');
    //직접로그아웃
    var logoutButton = document.getElementById('logout-button');
    //카카오로그아웃
    var kakaologout = document.querySelector('.logout-kakao-btn');

    // 쿠키에서 토큰 가져오기
    const token = getCookie("token");
    const kakao = getCookie("kakao")
    // 토큰이 있는 경우 로그아웃 버튼 표시
    if (!token) {
        loginButton.style.display = 'inline-block';
        logoutButton.style.display = 'none';
        kakaologout.style.display = 'none';
    } else if (kakao) {
        logoutButton.style.display = 'none';
        loginButton.style.display = 'none';
        kakaologout.style.display = 'inline-block';
    } else {
        // 토큰이 없는 경우 로그인 버튼 표시
        logoutButton.style.display = 'inline-block';
        loginButton.style.display = 'none';
        kakaologout.style.display = 'none';

    }

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
