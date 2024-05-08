
// ----------------------- 마감임박 상품 조회
    document.addEventListener('DOMContentLoaded', function() {
    // 페이지 로드가 완료된 후 실행될 코드
    fetchImpendingItems();
});

    function fetchImpendingItems() {
    fetch('/api/items/impending')
        .then(response => response.json())
        .then(data => displayImpendingItems(data))
        .catch(error => console.error('Error fetching impending items:', error));
}

    function displayImpendingItems(items) {
    const impendingItemsContainer = document.querySelector('.impending-items');

    items.forEach(item => {
    const itemElement = document.createElement('div');
    itemElement.classList.add('item');

    itemElement.innerHTML = `
                <img src="${item.imageUrl}" alt="제품사진">
                <div>
                    <div>${item.totalSalesUnit}Kg</div>
                    <div>${item.name}</div>
                    <div>${item.price}원</div>
                </div>
            `;

    impendingItemsContainer.appendChild(itemElement);
});
}

// ----------------------- 인기 상품 조회
document.addEventListener('DOMContentLoaded', function() {
    fetchPopularItems();
});

function fetchPopularItems() {
    fetch('/api/items/popularity')
        .then(response => response.json())
        .then(data => displayPopularItems(data))
        .catch(error => console.error('Error fetching popularity items:', error))
}

function displayPopularItems(popularItems) {
    const popularItemsContainer = document.getElementById('popular-items-container');

    // popularItems가 배열이 아니더라도 데이터가 있는지 확인
    if (popularItems && typeof popularItems === 'object') {
        // 객체에서 데이터를 추출하여 처리
        for (const key in popularItems) {
            if (Object.hasOwnProperty.call(popularItems, key)) {
                const item = popularItems[key];

                const popularElement = document.createElement('div');
                popularElement.classList.add('item');

                popularElement.innerHTML =  `
                    <img src="${item.imageUrl}" alt="제품사진">
                    <div>
                        <div>${item.totalSalesUnit}Kg</div>
                        <div>${item.name}</div>
                        <div>${item.price}원</div>
                    </div>
                `;

                popularItemsContainer.appendChild(popularElement);
            }
        }
    } else {
        console.error('Error fetching popularity items: Invalid data format');
    }
}

// ----------------------- 판매글 목록 조회
// 페이지가 로드될 때 모든 상품 목록을 가져오기
document.addEventListener('DOMContentLoaded', function() {
    fetchItems(); // 모든 상품 목록 가져오기
});

// 버튼 클릭 시 상품 목록을 가져오는 함수
function fetchItemsByCategory(category) {
    fetch('/api/items/category/' + category) // 해당 카테고리에 따른 API 호출
        .then(response => response.json())
        .then(data => {
            displayItems(data); // 상품 목록을 화면에 표시
        })
        .catch(error => {
            console.error('상품 목록을 불러오는 중 오류가 발생했습니다:', error);
        });
}

// 모든 상품 목록을 가져오는 함수
function fetchItems() {
    fetch('/api/items') // 전체 상품 목록을 가져오는 API 호출
        .then(response => response.json())
        .then(data => {
            displayItems(data); // 상품 목록을 화면에 표시
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
            itemElement.innerHTML = `
                <img src="${item.imageUrl}" alt="제품사진">
                <div>
                    <div>${item.totalSalesUnit}Kg</div>
                    <div>${item.name}</div>
                    <div>${item.price}원</div>
                </div>
            `;

            itemsContainer.appendChild(itemElement);
        });
    }
}

// 채소 버튼 클릭 시
document.querySelector('.pickup-button:nth-child(1)').addEventListener('click', function() {
    fetchItemsByCategory('VEGETABLE');
});

// 과일 버튼 클릭 시
document.querySelector('.pickup-button:nth-child(2)').addEventListener('click', function() {
    fetchItemsByCategory('FRUIT');
});

// 기타 버튼 클릭 시
document.querySelector('.pickup-button:nth-child(3)').addEventListener('click', function() {
    fetchItemsByCategory('OTHER');
});

// ----------------------- 뉴스 조회
document.addEventListener("DOMContentLoaded", function() {
    // DOM이 로드된 후 실행되는 부분

    // API 호출 및 데이터 가져오기
    fetch('/api/news')
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