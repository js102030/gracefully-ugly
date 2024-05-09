document.addEventListener('DOMContentLoaded', function () {
    const modal = document.querySelector('.modal');
    const modalCloseButton = document.querySelector('.modal-close');
    const listButton = document.querySelector('.list-button');
    const backButton = document.querySelector('.back-button');
    const modifyOrderButton = document.querySelector('.modify-order-btn');
    const refundButton = document.querySelector('.refund-btn');

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

    backButton.addEventListener('click', () => {
        window.location.href = "/";
    })

    modifyOrderButton.addEventListener('click', () => {
        const orderId = document.querySelector('.order-id').value;

        window.location.href = ('http://localhost:8080/orders/modify/' + orderId);
    })

    refundButton.addEventListener('click', () => {
        const orderId = document.querySelector('.order-id').value;

        $.ajax({
            url: 'http://localhost:8080/api/payment/kakaopay/refund/' + orderId,
            method: 'put',
            success: function (data) {
                alert("정상적으로 환불되었습니다.");
                window.location.reload();
            },
            error: function (data, status, error) {
                alert('환불 도중 문제가 발생했습니다.\n[status: ' + data.status +']\n[error: ' + data.responseText + ']');
            }
        })
    })
})