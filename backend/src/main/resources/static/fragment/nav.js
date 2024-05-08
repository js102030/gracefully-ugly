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


    var loginButton = document.querySelector('.login-button');
    var logoutButton = document.getElementById('logout-button');

    // 쿠키에서 토큰 가져오기
    const token = getCookie("token");

    console.log("토큰 :" + token);

    // 토큰이 있는 경우 로그아웃 버튼 표시
    if (token) {
        logoutButton.style.display = 'inline-block';
        loginButton.style.display = 'none';
    } else {
        // 토큰이 없는 경우 로그인 버튼 표시
        loginButton.style.display = 'inline-block';
        logoutButton.style.display = 'none';
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