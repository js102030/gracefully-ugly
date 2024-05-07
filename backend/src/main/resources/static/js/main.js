
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
        .catch(error => console.error('Error fetching popular items:', error));
}

function displayPopularItems(items) {
    const popularItemsContainer = document.getElementById('popular-items-container');

    items.forEach(item => {
        const pickElement = document.createElement('div');
        pickElement.classList.add('pick');

        pickElement.innerHTML = `
                <img src="${item.imageUrl}" alt="제품사진">
                <div>
                    <div>${item.totalSalesUnit}Kg</div>
                    <div>${item.name}</div>
                    <div>${item.price}원</div>
                </div>
            `;

        popularItemsContainer.appendChild(pickElement);
    });
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
// 뉴스 데이터를 화면에 표시하는 함수
function displayNews(newsList) {
    const newsContainer = document.querySelector('.news div');
    newsContainer.innerHTML = ''; // 기존 내용 초기화

    newsList.forEach(news => {
        const newsElement = document.createElement('div');
        newsElement.innerHTML = news.content; // 데이터베이스에서 가져온 뉴스 내용을 삽입

        newsContainer.appendChild(newsElement);
    });
}

// API를 호출하여 뉴스 데이터 가져오기
function fetchNews() {
    fetch('/api/news') // API 엔드포인트 호출
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                displayNews(data.data); // 데이터 성공적으로 가져오면 화면에 표시
            } else {
                console.error('뉴스 데이터를 가져오지 못했습니다.');
            }
        })
        .catch(error => {
            console.error('API 호출 중 오류 발생:', error);
        });
}

// 페이지 로드 시 뉴스 데이터 가져오기
window.addEventListener('load', fetchNews);
