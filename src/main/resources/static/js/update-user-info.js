const modifyNicknameButton = document.querySelector(".modify-nickname-button");
const modifyPasswordButton = document.querySelector(".modify-password-button");
const modifyAddressButton = document.querySelector(".modify-address-button");
const backBtn = document.querySelector(".back-button");
const userId = document.querySelector(".user-id").value;

modifyNicknameButton.addEventListener("click", event => {
    const nickname = document.getElementById("nickname").value;

    $.ajax({
        url: "/api/users/nickname/" + userId,
        method: "patch",
        contentType: "application/json",
        data: JSON.stringify({
            nickname: nickname
        }),
        success: function (data) {
            alert("닉네임이 성공적으로 변경되었습니다.");
            window.location.reload();
        },
        error: function (data, status, err) {
            alert('닉네임 변경 도중 문제가 발생했습니다.\n[status: ' + data.status +']\n[error: ' + data.responseText + ']');
        }
    })
})

modifyPasswordButton.addEventListener("click", event => {
    const password = document.getElementById("password").value;

    $.ajax({
        url: "/api/users/password/" + userId,
        method: "patch",
        contentType: "application/json",
        data: JSON.stringify({
            password: password
        }),
        success: function (data) {
            alert("비밀번호가 성공적으로 변경되었습니다.");
            window.location.reload();
        },
        error: function (data, status, err) {
            alert('비밀번호 변경 도중 문제가 발생했습니다.\n[status: ' + data.status +']\n[error: ' + data.responseText + ']');
        }
    })
})

modifyAddressButton.addEventListener("click", event => {
    const address = document.getElementById("address").value;

    $.ajax({
        url: "/api/users/address/" + userId,
        method: "patch",
        contentType: "application/json",
        data: JSON.stringify({
            address: address
        }),
        success: function (data) {
            alert("주소가 성공적으로 변경되었습니다.");
            window.location.reload();
        },
        error: function (data, status, err) {
            alert('주소 변경 도중 문제가 발생했습니다.\n[status: ' + data.status +']\n[error: ' + data.responseText + ']');
        }
    })
})

backBtn.addEventListener("click", event => {
    window.history.back();
})