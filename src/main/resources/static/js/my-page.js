const deleteUserBtn = document.getElementById("deleteUserBtn");

deleteUserBtn.addEventListener("click", event => {
    const check = confirm("회원 탈퇴 하시겠습니까?");

    if (check) {
        $.ajax({
            url: "/api/users",
            method: "delete",
            success: function (data) {
                alert("회원 탈퇴가 성공적으로 이루어졌습니다.");
                document.cookie = "token" + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
                window.location.href = "/";
            },
            error: function (data, status, err) {
                alert('회원 탈퇴 중 문제가 발생했습니다.\n[status: ' + data.status +']\n[error: ' + data.responseText + ']');
            }
        })
    }
})

