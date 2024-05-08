document.addEventListener('DOMContentLoaded', function() {
    const modal = document.querySelector('.modal');
    const modalCloseButton = document.querySelector('.modal-close');
    const listButton = document.querySelector('.list-button');
    const backButton = document.querySelector('.back-button');
    const createOrder = document.querySelector('.create-order');

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

    backButton.addEventListener('click', () => {
        window.history.back();
    })

    createOrder.addEventListener('click', () => {
        const itemId = document.querySelector('.item-id').value;
        const quantity = document.getElementById('quantity').value;
        const address = document.getElementById('address').value;
        const phoneNumber = document.getElementById('number').value;

        $.ajax({
            url: 'http://localhost:8080/api/orders',
            method: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: {
                itemIdList: [
                    {itemId: itemId, quantity: quantity}
                ],
                address: address,
                phoneNumber: phoneNumber
            },
            success: function (data) {
                $.ajax({
                    url: 'http://localhost:8080/payment',
                    method: 'post',
                    contentType: 'application/json',
                    data: data,
                    success: function (data) {
                        window.location.href = data;
                    }
                })
            },
            error: function (data, status, error) {
                alert('주문서 생성 중 문제가 발생했습니다. [status: ' + status +', error: ' + error + ']');
            }
        })
    })
});
