//----------------------------회원가입----------------------------------------
document.addEventListener("DOMContentLoaded", function () {
    if (document.getElementById("joinForm")) {
        document.getElementById("joinForm").addEventListener("submit", function (event) {
            event.preventDefault(); // 기본 동작인 폼 제출 방지

            // 비밀번호와 비밀번호 확인 값 가져오기
            const password = document.getElementById("password").value;
            const confirmPassword = document.getElementById("confirmPassword").value;
            const loginId = document.getElementById("loginId").value;

            // 닉네임과 주소가 비어 있는지 확인
            if (!loginId.trim() || !password.trim()) {
                alert("사용하실 아이디와 비밀번호를 입력해주세요.");
                return;
            }
            console.log(loginId.length);
            // 아이디와 비밀번호 유효성 검사
            if (loginId.length < 4 || loginId.length > 20) {
                alert("아이디는 4자 이상 20자 이하로 입력해주세요.");
                return;
            }

            if (password.length < 4 || password.length > 20) {
                alert("비밀번호는 4자 이상 20자 이하로 입력해주세요.");
                return;
            }

            // 비밀번호와 비밀번호 확인이 일치하는지 확인
            if (password !== confirmPassword) {
                alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                return; // 폼 제출을 중지하고 함수 종료
            }

            // 아이디와 비밀번호 데이터 생성
            const formData = {
                loginId: loginId,
                password: password
            };

            // AJAX 요청 보냄
            fetch(`/api/all/loginId-availability?loginId=${loginId}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(response => {
                    if (response.ok) {
                        return response.json(); // JSON 형식으로 응답 데이터 파싱
                    } else {
                        throw new Error('네트워크 상태가 이상합니다.');
                    }
                })
                .then(data => {
                    if (data) {
                        console.log("이미 사용중인 아이디입니다");
                        alert("이미 사용중인 아이디입니다");
                    } else {
                        // AJAX 요청 보냄
                        fetch("/api/all/users", {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json"
                            },
                            body: JSON.stringify(formData)
                        })
                            .then(response => {
                                if (response.ok) {
                                    console.log("회원가입 성공");
                                    window.location.href = `/join2?loginId=${loginId}`;
                                } else {
                                    console.log("회원가입 실패");
                                }
                            })
                            .catch(error => {
                                console.error("Error:", error);
                                alert("서버 오류: " + error.message); // 서버에서 반환한 오류 메시지를 표
                            });
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                });
        });
    }
});
