document.addEventListener('DOMContentLoaded', function() {
    const modal = document.querySelector('.modal');
    const modalCloseButton = document.querySelector('.modal-close');
    const listButton = document.querySelector('.list-button');
    const viewOrder = document.querySelector('.view-order');
    const goToMain = document.querySelector(".go-to-main");

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

    viewOrder.addEventListener('click', event => {
        const orderId = document.querySelector('.order-id').value;

        window.location.href = ('http://localhost:8080/orders/' + orderId);
    })

    goToMain.addEventListener('click', event => {
        window.location.href = '/';
    })
});
