document.addEventListener("DOMContentLoaded", function () {
    //-------------------------상품 등록하기----------------------------------
    const askTextarea = document.querySelector('.ask');
    const askAddButton = document.querySelector('.askAdd');
    const item = document.getElementById('itemId');

    askAddButton.addEventListener('click', function () {
        const askContent = askTextarea.value.trim();
        const itemId = item.value;

        // 만약 입력된 내용이 없다면 처리 중지
        if (askContent === '') {
            alert('문의사항을 입력해주세요.');
            return;
        }

        // Fetch를 사용하여 API 호출
        fetch('/api/questions/' + itemId, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                question: askContent
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('상품 등록에 실패하였습니다.');
                }
                return response.json();
            })
            .then(data => {
                // API 호출이 성공했을 때 처리할 코드 작성
                console.log('상품 등록 성공:', data);
                alert('상품 등록 성공');
                window.location.reload();
                // 성공적으로 등록되었다는 알림 표시 등 추가적인 작업 수행 가능
            })
            .catch(error => {
                // API 호출이 실패했을 때 처리할 코드 작성
                console.error('문의 실패:', error.message);
                // 실패했다는 알림 표시 등 추가적인 작업 수행 가능
            });
    });
});

//--------------------------답변 등록하기------------------------------
function addAnswer(event) {
    event.preventDefault();

    let qnaContainer = event.target.closest('.oneRoute');

    let qnaId = qnaContainer.querySelector('#qnaId').value;

    let answerContent = qnaContainer.querySelector('#answer_text').value;

    console.log(qnaContainer);
    console.log(qnaId);
    console.log(answerContent);
    // 요청 본문 데이터
    const requestData = {
        answer: answerContent
    };

    fetch(`/api/answers/` + qnaId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
        .then(response => {
            if (response.ok) {
                alert("답변 등록에 성공하셨습니다.");
                window.location.reload();
            } else {
                throw new Error('Failed to submit answer');
            }
        })
        .catch(error => {
            console.error('Error submitting answer:', error);
            alert(error);
        });
}

//----------------------------답변하기누르면 해당 칸에만 답변하는 상자 나오게 함----------------
function showAddAnswer(event) {
    const container = event.target.closest('.oneRoute');
    let qnaId = container.querySelector('#qnaId').value;
    console.log(container);
    console.log(qnaId);

    // 해당 인덱스를 사용하여 답변란을 찾음
    const answer_input = document.getElementById(`answer_input_${qnaId}`);

    // 답변란이 존재하는지 확인
    if (answer_input) {
        // 답변란의 표시 상태를 변경
        answer_input.style.display = answer_input.style.display === 'none' ? 'block' : 'none';
    } else {
        console.error(`Answer input with ID 'answer_input_${qnaId}' not found.`);
    }
}

// --------------------------------뒤로 가기 버튼--------------------------------------
document.querySelector('.back').addEventListener('click', function() {
    window.history.back();
});