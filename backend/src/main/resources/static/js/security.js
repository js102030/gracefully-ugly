//----------------------------사이트 로그아웃 ----------------------------------------
document.addEventListener("DOMContentLoaded", function () {
    const logoutButton = document.getElementById('logout-button');

    if (logoutButton) {
        logoutButton.addEventListener('click', event => {
            fetch(`/logout`, {
                method: 'POST',
            })
                .then(response => {
                    alert('로그아웃 완료됏습니다');
                    location.replace(`/`);
                })
                .catch(error => {
                    console.error('Error logout:', error);
                    alert('로그아웃중 오류 발생');
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
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                });
        });
    }
});