document.addEventListener("DOMContentLoaded", function () {

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

    //---------------------------------알림 예외----------------------------------
    const alert_button = document.getElementById('test_btn');

    if(alert_button){
        alert_button.addEventListener('click', function(){
            alert("추후에 알림 기능이 업데이트 될 예정이에요!")
            window.location.reload();
        })
    }

    //-----------------------------판매자만 판매내역 보이게 설정-----------------
    const salesList_btn = document.getElementById('modal-4');

    const user_Role = getRoleFromToken(token);
    console.log(user_Role);
    if(!user_Role || user_Role != 'ROLE_SELLER'){
        salesList_btn.style.display = 'none';
    }

    // ----------------------------------쿠키 찾는 함수------------------------
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
    // -----------------------------------역할 찾는 함수-------------------
    function getRoleFromToken(token) {
        // 토큰을 '.'으로 분리하여 세 부분으로 나눔
        const tokenParts = token.split('.');

        // 토큰의 페이로드 부분은 두 번째 요소
        const payload = JSON.parse(atob(tokenParts[1]));

        // 역할(Claims)이 payload 객체의 속성 중 하나일 것으로 가정
        const role = payload.role;

        return role;
    }
});
