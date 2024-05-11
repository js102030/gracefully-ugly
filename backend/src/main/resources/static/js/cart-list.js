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

function moveToGroupBuying(itemId) {
    window.location.href = `http://localhost:8080/group-buying/` + itemId;
}

function deleteCartItem(cartItemId) {
    $.ajax({
        url: "http://localhost:8080/api/cart/" + cartItemId,
        method: 'delete',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            alert("해당 상품이 찜 목록에서 삭제되었습니다.");
            window.location.reload();
        },
        error: function (data, status, error) {
            alert('삭제 도중 문제가 발생했습니다.\n[status: ' + data.status +']\n[error: ' + data.responseText + ']');
        }
    })
}

function orderCartItem(itemId) {
    window.location.href = ('http://localhost:8080/orders/item/' + itemId);
}