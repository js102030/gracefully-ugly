//----------------------------2차 회원가입----------------------------------------
document.addEventListener("DOMContentLoaded", function () {
    if (document.getElementById("joinForm2")) {
        document.getElementById("joinForm2").addEventListener("submit", function (event) {
            event.preventDefault(); // 기본 동작인 폼 제출 방지

            // 닉네임과 주소 가져오기
            const nickname = document.getElementById("nickname").value;
            const address = document.getElementById("address").value;
            const role = document.querySelector('input[name="role"]:checked').value;
            const loginId = document.getElementById("loginId").value;

            // 닉네임과 주소가 비어 있는지 확인
            if (!nickname.trim() || !address.trim()) {
                alert("닉네임과 주소를 입력해주세요.");
                return;
            }

            // 요청 데이터 생성
            const requestData = {
                loginId: loginId,
                nickname: nickname,
                address: address,
                role: role
            };

            fetch(`/api/all/nickname-availability?nickname=${nickname}`, {
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
                        console.log("이미 존재하는 닉네임입니다");
                        alert("이미 존재하는 닉네임입니다");
                    } else {
                        // AJAX 요청 보냄
                        fetch("/api/all/users/registration", {
                            method: "PATCH",
                            headers: {
                                "Content-Type": "application/json"
                            },
                            body: JSON.stringify(requestData)
                        })
                            .then(response => {
                                if (response.ok) {
                                    alert("회원가입에 성공했습니다")
                                    window.location.href = "/"; //
                                } else {
                                    console.log("회원가입 실패");
                                    // 실패 시 사용자에게 알림을 표시하거나 다른 처리를 수행할 수 있습니다.
                                }
                            })
                            .catch(error => {
                                console.error("Error:", error);
                            });
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                });
        });
    }

    // 닉네임 중복 확인 버튼 클릭 이벤트 처리
    const duplicateCheckButton = document.getElementById("duplicateCheckButton");
    if (duplicateCheckButton) {
        duplicateCheckButton.addEventListener("click", function (event) {
            event.preventDefault(); // 기본 동작인 폼 제출 방지

            const nickname = document.getElementById("nickname").value;

            // AJAX 요청 보냄
            fetch(`/api/all/nickname-availability?nickname=${nickname}`, {
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
                        console.log("이미 존재하는 닉네임입니다");
                        alert("이미 존재하는 닉네임입니다");
                    } else {
                        console.log("사용 가능한 닉네임입니다");
                        alert("사용 가능한 닉네임입니다");
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                });
        });
    }
});
