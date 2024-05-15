//----- 상품 클릭 시 itemId에 따라서 다른 페이지 띄워주기
document.addEventListener('DOMContentLoaded', function () {
    const impendingItemsContainer = document.querySelector('.impending-items');
    const popularItemsContainer = document.getElementById('popular-items-container');
    const moreItemsContainer = document.querySelector('.more-items');

    function handleItemClick(itemId) {

        window.location.href = `/group-buying/` + itemId;
    }

    // 마감임박 상품 클릭 시
    impendingItemsContainer.addEventListener('click', function (event) {
        const itemElement = event.target.closest('.item');
        if (itemElement) {
            const itemId = itemElement.dataset.itemId;
            if (itemId) {
                handleItemClick(itemId);
            }
        }
    });

    // 인기 상품 클릭 시
    popularItemsContainer.addEventListener('click', function (event) {
        const itemElement = event.target.closest('.item');
        if (itemElement) {
            const itemId = itemElement.dataset.itemId;
            if (itemId) {
                handleItemClick(itemId);
            }
        }
    });

    // 더보기 상품 클릭 시
    moreItemsContainer.addEventListener('click', function (event) {
        const itemElement = event.target.closest('.item');
        if (itemElement) {
            const itemId = itemElement.dataset.itemId;
            if (itemId) {
                handleItemClick(itemId);
            }
        }
    });
});

// ----------------------- 마감임박 상품 조회
document.addEventListener('DOMContentLoaded', function () {
    // 페이지 로드가 완료된 후 실행될 코드
    fetchImpendingItems();
});

function fetchImpendingItems() {
    fetch('/api/all/items/impending')
        .then(response => response.json())
        .then(data => displayImpendingItems(data))
        .catch(error => console.error('Error fetching impending items:', error));
}

function displayImpendingItems(items) {
    const impendingItemsContainer = document.querySelector('.impending-items');

    items.forEach(item => {
        const itemElement = document.createElement('div');
        itemElement.classList.add('item');
        itemElement.dataset.itemId = item.id;

        itemElement.innerHTML = `
                <img src="${item.imageUrl ? item.imageUrl : '/image/item.png'}" alt="제품 사진" width="240" height="240">
                    <div>
                        <div>전체물량 : ${(item.totalSalesUnit * item.minUnitWeight).toLocaleString()}g</div>
                        <div style="font-size: 25px">${item.name}</div>
                        <div style="font-size: 20px">${item.minUnitWeight}g - ${item.price.toLocaleString()}원</div>
                    </div>
            `;

        impendingItemsContainer.appendChild(itemElement);
    });
}

// ----------------------- 인기 상품 조회

document.addEventListener('DOMContentLoaded', function () {
    // API 엔드포인트 URL
    const apiUrl = '/api/all/items/popularity';

    // API 호출
    fetch(apiUrl)
        .then(response => {
            // HTTP 응답 코드를 확인하여 처리
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // JSON 형태로 응답 데이터 파싱
        })
        .then(data => {
            // API로부터 받은 데이터를 처리하여 HTML에 추가
            const popularItemsContainer = document.getElementById('popular-items-container');

            // 받은 데이터를 반복하여 각 상품을 HTML에 추가
            data.forEach(item => {
                const itemElement = document.createElement('div');
                itemElement.classList.add('item');
                itemElement.dataset.itemId = item.id;

                // 각 상품 정보를 표시할 방법에 따라 구성
                itemElement.innerHTML = `
                    <img src="${item.imageUrl ? item.imageUrl : '/image/item.png'}" alt="제품 사진" width="240" height="240">
                    <div>
                        <div>전체물량 : ${(item.totalSalesUnit * item.minUnitWeight).toLocaleString()}g</div>
                        <div style="font-size: 25px">${item.name}</div>
                        <div style="font-size: 20px">${item.minUnitWeight}g - ${item.price.toLocaleString()}원</div>
                    </div>
                `;

                // 상품 요소를 컨테이너에 추가
                popularItemsContainer.appendChild(itemElement);
            });
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        });
});


// ----------------------- 판매글 목록 조회
document.addEventListener('DOMContentLoaded', function () {
    // DOM이 로드된 후 실행되는 부분

    // 모든 상품 목록을 가져오는 함수
    function fetchAllItems() {
        fetch('/api/all/items')
            .then(response => {
                if (!response.ok) {
                    throw new Error('상품 목록을 불러오는 중 오류가 발생했습니다.');
                }
                return response.json();
            })
            .then(data => {
                displayItems(data); // 모든 상품 목록을 화면에 표시
            })
            .catch(error => {
                console.error('상품 목록을 불러오는 중 오류가 발생했습니다:', error);
            });
    }

    // 카테고리별 상품 목록을 가져오는 함수
    function fetchItemsByCategory(category) {
        fetch('/api/all/items/category/' + category)
            .then(response => {
                if (!response.ok) {
                    throw new Error('상품 목록을 불러오는 중 오류가 발생했습니다.');
                }
                return response.json();
            })
            .then(data => {
                displayItems(data); // 해당 카테고리 상품 목록을 화면에 표시
            })
            .catch(error => {
                console.error('상품 목록을 불러오는 중 오류가 발생했습니다:', error);
            });
    }

    // 상품 목록을 화면에 표시하는 함수
    function displayItems(items) {
        const itemsContainer = document.getElementById('items-container');
        itemsContainer.innerHTML = ''; // 기존 내용을 초기화

        if (items.length === 0) {
            itemsContainer.innerHTML = '<p>상품이 없습니다.</p>';
        } else {
            items.forEach(item => {
                const itemElement = document.createElement('div');
                itemElement.classList.add('item');
                itemElement.dataset.itemId = item.id;

                // 상품 이미지 및 정보 표시
                itemElement.innerHTML = `
                    <img src="${item.imageUrl ? item.imageUrl : '/image/item.png'}" alt="제품 사진" width="240" height="240">
                    <div>
                        <div>전체물량 : ${(item.totalSalesUnit * item.minUnitWeight).toLocaleString()}g</div>
                        <div style="font-size: 25px">${item.name}</div>
                        <div style="font-size: 20px">${item.minUnitWeight}g - ${item.price.toLocaleString()}원</div>
                    </div>
                `;


                itemsContainer.appendChild(itemElement);
            });
        }
    }

    // 페이지가 로드될 때 모든 상품 목록을 가져와서 화면에 표시
    fetchAllItems();

    // 채소 버튼 클릭 시
    document.querySelector('.pickup-button:nth-child(1)').addEventListener('click', function () {
        fetchItemsByCategory('VEGETABLE');
    });

    // 과일 버튼 클릭 시
    document.querySelector('.pickup-button:nth-child(2)').addEventListener('click', function () {
        fetchItemsByCategory('FRUIT');
    });

    // 기타 버튼 클릭 시
    document.querySelector('.pickup-button:nth-child(3)').addEventListener('click', function () {
        fetchItemsByCategory('OTHER');
    });
});
// ----------------------- 뉴스 조회
document.addEventListener("DOMContentLoaded", function () {
    // DOM이 로드된 후 실행되는 부분

    // API 호출 및 데이터 가져오기
    fetch('/api/all/news')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch news');
            }
            return response.json();
        })
        .then(data => {
            // 응답 데이터 처리
            const newsContainer = document.querySelector('.news-content');

            // data 객체에서 뉴스 데이터 배열 가져오기
            const newsData = data.data; // data 객체의 'data' 속성에 배열이 있음

            // 뉴스 데이터를 반복하여 HTML 요소로 추가
            newsData.forEach(newsItem => {
                const newsElement = document.createElement('div');
                newsElement.classList.add('news-item');

                // 뉴스 제목과 내용을 추가
                const titleElement = document.createElement('h3');
                titleElement.textContent = newsItem.contents;
                newsElement.appendChild(titleElement);

                // 뉴스 생성일 추가 (예시: 날짜 형식을 변환하여 표시)
                const createdAt = new Date(newsItem.createdAt);
                const dateElement = document.createElement('p');
                dateElement.textContent = `게시일: ${createdAt.toLocaleDateString()}`;
                newsElement.appendChild(dateElement);

                // newsContainer에 뉴스 요소 추가
                newsContainer.appendChild(newsElement);
            });
        })
        .catch(error => {
            console.error('Error fetching news:', error);
        });
});

