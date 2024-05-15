document.addEventListener('DOMContentLoaded', function () {
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

const backButton = document.querySelector('.back-button');

backButton.addEventListener('click', () => {
    window.history.back();
})

// 수정 & 저장

// DOM 요소들을 가져오기
const existingModifyButtons = document.querySelectorAll('.modify-button');
const saveAddressButton = document.querySelector('.save-address-button');
const savePhoneNumberButton = document.querySelector('.save-phone-number-button');

// 페이지 로드 후 실행될 함수
document.addEventListener('DOMContentLoaded', () => {
    // 모든 입력 필드를 비활성화
    const inputs = document.querySelectorAll('input');
    inputs.forEach((input) => {
        input.disabled = true;
    });
});

// 초기 상태에서 저장 버튼 숨기기
saveAddressButton.style.display = 'none';
savePhoneNumberButton.style.display = 'none';

// 각 수정 버튼에 이벤트 리스너 추가
existingModifyButtons.forEach((button) => {
    button.addEventListener('click', (event) => {
        enableSaveButton(button, event);
    });
});

// 각 저장 버튼에 이벤트 리스너 추가
saveAddressButton.addEventListener('click', (event) => {
    const orderId = document.querySelector('.order-id').value;
    const newAddress = document.getElementById('address').value;

    enableModifyButton(saveAddressButton, event);

    $.ajax({
        url: "/api/orders/address/" + orderId,
        method: 'put',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            address: newAddress
        }),
        success: function (data) {
            alert('주소가 성공적으로 변경되었습니다.');
        },
        error: function (data, status, error) {
            alert('변경 도중 문제가 발생했습니다.\n[status: ' + data.status + ']\n[error: ' + data.responseText + ']');
        }
    });
});

savePhoneNumberButton.addEventListener('click', (event) => {
    const orderId = document.querySelector('.order-id').value;
    const newPhoneNumber = document.getElementById('number').value;

    enableModifyButton(savePhoneNumberButton, event);

    if (!validatePhoneNumber(newPhoneNumber)) {
        alert('올바른 연락처를 입력해주세요 (010[숫자8개])');
    } else {
        $.ajax({
            url: "/api/orders/phone_number/" + orderId,
            method: 'put',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({
                phoneNumber: newPhoneNumber
            }),
            success: function (data) {
                alert('연락처가 성공적으로 변경되었습니다.');
            },
            error: function (data, status, error) {
                alert('변경 도중 문제가 발생했습니다.\n[status: ' + data.status + ']\n[error: ' + data.responseText + ']');
            }
        });
    }
});

function enableSaveButton(button, event) {
    const parentDiv = event.target.parentElement;
    const inputField = parentDiv.querySelector('input');

    // 입력 필드 활성화
    inputField.removeAttribute('disabled');
    inputField.classList.add('active'); // 필요에 따라 스타일링을 위한 클래스 추가

    // 수정 버튼 숨기고 저장 버튼 보이기
    button.style.display = 'none';
    parentDiv.querySelector('.save-button').style.display = 'inline-block';
}

function enableModifyButton(button, event) {
    const parentDiv = event.target.parentElement;
    const inputField = parentDiv.querySelector('input');

    // 입력 필드 비활성화
    inputField.setAttribute('disabled', true);
    inputField.classList.remove('active'); // 스타일 클래스 제거

    // 저장 버튼 숨기고 수정 버튼 보이기
    button.style.display = 'none';
    parentDiv.querySelector('.modify-button').style.display = 'inline-block';
}

function validatePhoneNumber(phoneNumber) {
    const regex = new RegExp('010[0-9]{8}');
    return regex.test(phoneNumber);
}