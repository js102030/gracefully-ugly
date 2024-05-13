// 현재 페이지의 URL에서 itemId 가져오기
const pathSegments = window.location.pathname.split('/');
const itemId = pathSegments[pathSegments.length - 1];
