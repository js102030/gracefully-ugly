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

const btnReview = document.querySelector('.btn-review');
const btnItemPage = document.querySelector('.btn-item-page');
const btnReceipt = document.querySelector('.btn-receipt');

function moveToReview(itemId) {

}

function moveToItemPage(itemId) {
    window.location.href = ("http://localhost:8080/group-buying/" + itemId);
}

function moveToReceipt(orderId) {
    window.location.href = ("http://localhost:8080/orders/" + orderId);
}

function moveToReviewPage(itemId) {
    window.location.href = ("http://localhost:8080/create-review/" + itemId);
}