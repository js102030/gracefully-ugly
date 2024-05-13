document.addEventListener("DOMContentLoaded", function () {
    const answer_btns = document.querySelectorAll('.answer_btn');

    if (answer_btns) {
        answer_btns.forEach(btn => {
            btn.addEventListener('click', event => {
                // 현재 클릭된 답변 버튼에 해당하는 인덱스 추출
                const index = btn.parentNode.parentNode.dataset.index;
                // 해당 인덱스를 사용하여 답변란을 찾음
                const answer_input = document.getElementById(`answer_input_${index}`);

                // 답변란이 숨겨진 상태인지 확인
                const isHidden = answer_input.style.display === 'none';

                // 답변란의 표시 상태를 변경
                answer_input.style.display = isHidden ? 'block' : 'none';
            });
        });
    }
});
