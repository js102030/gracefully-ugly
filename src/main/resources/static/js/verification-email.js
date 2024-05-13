const inputEmailBtn = document.querySelector(".input-email-btn");
const verificationBtn = document.querySelector(".verification-btn");

inputEmailBtn.addEventListener("click", event => {
    const inputEmail = document.querySelector(".input-email").value;

    if (!inputEmail) {
        alert("이메일을 입력해주세요");
        return;
    }

    $.ajax({
        url: "/api/verification?email=" + inputEmail,
        method: "post",
        success: function (data) {
            enableVerification();
            startCounter();
        },
        error: function (data, status, err) {
            alert('이메일 인증 중 문제가 발생했습니다.\n[status: ' + data.status +']\n[error: ' + data.responseText + ']');
        }
    });
});

verificationBtn.addEventListener("click", event => {
    const inputEmail = document.querySelector(".input-email").value;
    const inputVerificationCode = document.querySelector(".verification-code").value;

    $.ajax({
        url: "/api/verification/check",
        method: "patch",
        contentType: "application/json",
        data: JSON.stringify({
            email: inputEmail,
            code: inputVerificationCode
        }),
        success: function (data) {
            alert("인증에 성공했습니다.");
            window.location.href = "/my-page";
        },
        error: function (data, status, err) {
            console.log(data);

            alert(data.responseText);
        }
    })
})

function enableVerification() {
    const inputVerificationCode = document.querySelector(".verification-code");

    inputVerificationCode.disabled = false;
    verificationBtn.disabled = false;
}

function startCounter() {
    let time = 60;
    const remainingSec = document.getElementById("remaining-sec");
    remainingSec.innerText = (time + "초");

    setInterval(() => {
        if (time > 0) {
            time -= 1;
            remainingSec.innerText = (time + "초");
        }
    }, 1000);
}