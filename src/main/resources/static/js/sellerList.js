$(document).ready(function() {
    var completedCount = 0;
    var ingCount = 0;
    // 각 행의 itemId를 가져와서 API를 호출하여 모집 상태를 업데이트
    $(".table_items .getItemId").each(function() {
        var $this = $(this);
        var itemId = $this.text().trim(); // 각 행의 itemId 가져오기
        document.getElementById('moveDetails').addEventListener('click', function() {
            window.location.href = '/sellerDetails/' + itemId;
        });
        document.getElementById('moveItems').addEventListener('click', function() {
            window.location.href = '/group-buying/' + itemId;
        });
        document.getElementById('salesPostButton').addEventListener('click', function() {
            window.location.href = '/salesPost';
        });
        var apiUrl = "/api/groupbuy/items/" + itemId;

        fetch(apiUrl)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                // groupBuyList가 비어있을 때는 모집중으로 넣어줘야함
                if (data && data.groupBuyList) {
                    var groupBuyStatus = data.groupBuyList.length > 0 ? data.groupBuyList[0].groupBuyStatus : 'IN_PROGRESS';

                    var statusText = "";
                    if (groupBuyStatus === 'CANCELLED' || groupBuyStatus === 'COMPLETED') {
                        statusText = '모집 완료';
                        completedCount++;
                    } else {
                        statusText = '모집중';
                        ingCount++;
                    }

                    // 테이블의 해당 행에 모집 상태 텍스트 추가
                    var $row = $this.closest('tr');
                    $row.find('td:last').text(statusText);

                    // 센 결과를 HTML 요소에 추가
                    $('.input_data_completed').text(completedCount);
                    $('.input_data_ing').text(ingCount);
                } else {
                    console.error("API 응답 데이터에 groupBuyList가 존재하지 않습니다.");
                }
            })
            .catch(error => {
                console.error("API 요청이 실패하였습니다.", error);
            });
    });
});


