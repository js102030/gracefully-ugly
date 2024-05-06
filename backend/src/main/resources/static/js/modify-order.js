document.addEventListener('DOMContentLoaded', function() {
    const modal = document.querySelector('.modal');
    const modalCloseButton = document.querySelector('.modal-close');
    const listButton = document.querySelector('.list-button');

    if (listButton) {
        listButton.addEventListener('click', function() {
            modal.style.display = 'block';
        });
    }

    if (modalCloseButton) {
        modalCloseButton.addEventListener('click', function() {
            modal.style.display = 'none';
        });
    }

    window.addEventListener('click', function(event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
});

// 수정 & 저장

// DOM 요소들을 가져오기
const existingModifyButtons = document.querySelectorAll('.modify-button');
const existingSaveButtons = document.querySelectorAll('.save-button');
const addressInput = document.querySelector('input[name="받는분 주소"]');
const numberInput = document.querySelector('input[name="받는분 연락처"]');

// 페이지 로드 후 실행될 함수
document.addEventListener('DOMContentLoaded', () => {
    // 모든 입력 필드를 비활성화
    const inputs = document.querySelectorAll('input');
    inputs.forEach((input) => {
        input.disabled = true;
    });
});

// 초기 상태에서 저장 버튼 숨기기
existingSaveButtons.forEach((button) => {
    button.style.display = 'none';
});

// 각 수정 버튼에 이벤트 리스너 추가
existingModifyButtons.forEach((button) => {
    button.addEventListener('click', (event) => {
        const parentDiv = event.target.parentElement;
        const inputField = parentDiv.querySelector('input');

        // 입력 필드 활성화
        inputField.removeAttribute('disabled');
        inputField.classList.add('active'); // 필요에 따라 스타일링을 위한 클래스 추가

        // 수정 버튼 숨기고 저장 버튼 보이기
        button.style.display = 'none';
        parentDiv.querySelector('.save-button').style.display = 'inline-block';
    });
});

// 각 저장 버튼에 이벤트 리스너 추가
existingSaveButtons.forEach((button) => {
    button.addEventListener('click', (event) => {
        const parentDiv = event.target.parentElement;
        const inputField = parentDiv.querySelector('input');

        // 수정된 내용 저장
        const newValue = inputField.value;

        // 입력 필드 비활성화
        inputField.setAttribute('disabled', true);
        inputField.classList.remove('active'); // 스타일 클래스 제거

        // 저장 버튼 숨기고 수정 버튼 보이기
        button.style.display = 'none';
        parentDiv.querySelector('.modify-button').style.display = 'inline-block';

        // 저장 완료 메시지 표시 (예시로 콘솔에 로그 출력)
        console.log(`저장 완료: ${newValue}`);
    });
});

