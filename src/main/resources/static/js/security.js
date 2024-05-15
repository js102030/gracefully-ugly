//----------------------------직접 사이트 로그아웃 ----------------------------------------
document.addEventListener("DOMContentLoaded", function () {
    const logoutButton = document.getElementById('logout-button');
    const kakaobutton = document.querySelector('.logout-kakao-btn');

    if (logoutButton) {
        logoutButton.addEventListener('click', event => {
            fetch(`/logout`, {
                method: 'POST',
            })
                .then(response => {
                    if (response.ok) {
                        // 로그아웃 성공
                        alert('로그아웃 완료됐습니다');
                        // 로그아웃 성공 후 리다이렉트
                        location.replace(`/`);
                    }
                })
                .catch(error => {
                    // 로그아웃 실패
                    console.error('로그아웃 중 에러:', error);
                    alert('로그아웃 중 오류 발생');
                });
        });
    }

    if (kakaobutton) {
        kakaobutton.addEventListener('click', event => {
            fetch(`/logout`, {
                method: 'POST',
            })
                .then(response => {
                    if (response.ok) {
                        // 로그아웃 성공
                        // 로그아웃 성공 후 리다이렉트
                        alert("더 안전한 보안을 위해서 카카오톡 계정과 함께 로그아웃을 눌러주셔야 정상 로그아웃 됩니다! ")
                        location.href = `https://kauth.kakao.com/oauth/logout?client_id=48f99478e44507e05c99dfeee8e1a42a&logout_redirect_uri=http://15.164.14.204:8080/`;
                    }
                })
                .catch(error => {
                    // 로그아웃 실패
                    console.error('로그아웃 중 에러:', error);
                    alert('로그아웃 중 오류 발생');
                });
        });
    }
});

//----------------------------사이트 직접 로그인 ----------------------------------------
document.addEventListener("DOMContentLoaded", function () {

    if (document.getElementById("joinForm")) {
        document.getElementById("joinForm").addEventListener("submit", function (event) {
            event.preventDefault(); // 기본 동작인 폼 제출 방지

            // 폼 데이터를 FormData 객체로 가져옴
            const formData = new FormData(this);

            // AJAX 요청 보냄
            fetch("/login", {
                method: "POST",
                body: formData
            })
                .then(response => {
                    if (response.ok) {
                        console.log("로그인 성공");

                        // 로그인 성공 알림을 표시합니다.
                        alert("로그인에 성공했습니다!");

                        // 로그인 성공 후 특정 URL로 리다이렉트합니다.
                        window.location.href = "/";
                    } /*else if(response.status === 403){
                    console.log("로그인 하셔야 들어갈수 있는 page입니다");
                    window.location.href = "/log";
                }else if((response.status === 401){
                    console.log("만료된 토큰으로 재로그인 해주세요!");
                    window.location.href = "/log";
                }*/ else {
                        // 로그인이 실패했을 때
                        console.log("로그인 실패");
                        alert("아이디나 비밀번호를 확인해주세요");
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                });
        });
    }
});