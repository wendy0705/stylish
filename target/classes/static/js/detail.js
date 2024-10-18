$(document).ready(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');

    if (productId) {
        fetchProductDetails(productId);
    }
    $('#add-to-cart').prop('disabled', true);
});

function fetchProductDetails(productId) {
    $.ajax({
            url: `/api/1.0/products/details?id=${productId}`,
            method: 'GET',
            success: function (response) {
                const data = response.data;
                $('#main-image').attr('src', data.main_image);
                $('#product-title').text(data.title);
                $('#product-id').text(data.id);
                $('#product-price').text(`TWD.${data.price}`);
                $('#product-description').text(data.description);
                $('#product-texture').text(`材質: ${data.texture}`);
                $('#product-wash').text(`洗滌方式: ${data.wash}`);
                $('#product-place').text(`產地: ${data.place}`);
                $('#product-note').text(data.note);
                $('#product-story').text(data.story);

                data.colors.forEach(color => {
                    $('#product-colors').append(`<div style="background-color: #${color.code};"></div>`);
                });

                const availableVariants = data.variants;
                // console.log("Available variants:", availableVariants);

                $('#product-sizes').empty();
                data.sizes.forEach(size => {
                    // console.log("Processing size:", size);
                    const sizeVariant = availableVariants.find(variant => variant.size === size);
                    // console.log("Found size variant:", sizeVariant);

                    const sizeSpan = $(`<span>${size}</span>`);
                    if (sizeVariant && sizeVariant.stock > 0) {
                        // console.log(`Size ${size} is available with stock ${sizeVariant.stock}`);
                        sizeSpan.addClass('available');
                    } else {
                        // console.log(`Size ${size} is not available or has no stock`);
                        sizeSpan.addClass('unavailable');
                    }
                    $('#product-sizes').append(sizeSpan);
                });

                let selectedColor = null;
                let selectedSize = null;
                let selectedProduct = {
                    id: data.id,
                    name: data.title,
                    price: data.price
                };

                function checkSelection() {
                    console.log('Checking selection:', selectedColor, selectedSize);
                    if (selectedColor && selectedSize) {
                        $('#add-to-cart').prop('disabled', false);
                    } else {
                        $('#add-to-cart').prop('disabled', true);
                    }
                }

                $('#product-colors div').click(function () {
                    $('#product-colors div').removeClass('selected');
                    $(this).addClass('selected');
                    selectedColor = {
                        code: $(this).data('color-code'),
                        name: $(this).data('color-name')
                    };
                    checkSelection();
                });
                //
                $('#product-sizes').on('click', 'span.available', function () {
                    $('#product-sizes span').removeClass('selected');
                    $(this).addClass('selected');
                    selectedSize = $(this).text();
                    checkSelection();
                });
                //
                $('#quantity-minus').on('click', function () {
                    let qty = parseInt($('#product-quantity').val());
                    if (qty > 1) {
                        $('#product-quantity').val(qty - 1);
                        console.log("Quantity decreased. New quantity:", $('#product-quantity').val());
                    }
                });

                $('#quantity-plus').on('click', function () {
                    let qty = parseInt($('#product-quantity').val());
                    if (qty < 10) {
                        $('#product-quantity').val(qty + 1);
                        console.log("Quantity increased. New quantity:", $('#product-quantity').val());
                    }
                });

                data.images.forEach(image => {
                    $('#product-images').append(`<img src="${image}" alt="Product Image">`);
                });

                $('#add-to-cart').click(function () {
                    console.log(localStorage);

                    const token = localStorage.getItem('token');
                    if (!token || token === 'undefined') {
                        window.location.href = '/profile.html';
                        return;
                    }

                });

                document.addEventListener('primeAcquired', function () {

                    const token = localStorage.getItem('token');
                    if (!token || token === 'undefined') {
                        alert('Something wrong during sign in/sign up, please try again.');
                    }

                    const prime = localStorage.getItem('paymentPrime');
                    if (!prime || prime === 'undefined') {
                        alert('Payment token not found. Please try again.');
                        return;
                    }

                    const quantity = parseInt($('#product-quantity').val());

                    const orderData = {
                        prime: prime,
                        order: {
                            shipping: "delivery",
                            payment: "credit_card",
                            subtotal: selectedProduct.price * quantity,
                            freight: 10,
                            total: selectedProduct.price * quantity + 10,
                            recipient: {
                                name: "Luke",
                                phone: "0987654321",
                                email: "luke@gmail.com",
                                address: "市政府站",
                                time: "morning"
                            },
                            list: [
                                {
                                    id: selectedProduct.id,
                                    name: selectedProduct.name,
                                    price: selectedProduct.price,
                                    color: selectedColor,
                                    size: selectedSize,
                                    qty: quantity
                                }
                            ]
                        }
                    };

                    $.ajax({
                        url: '/api/1.0/order/checkout',
                        method: 'POST',
                        contentType: 'application/json',
                        headers: {
                            'Authorization': `Bearer ${token}`
                        },
                        data: JSON.stringify(orderData),
                        success: function (response) {
                            console.log('Response:', JSON.stringify(response, null, 2));
                            window.location.href = '/thankyou.html';
                        },
                        error: function (error) {
                            alert('Error sending order:', error);
                        }
                    });
                });

            },
            error: function (err) {
                console.error('Error fetching product details:', err);
            }
        }
    )
    ;


}

// $('#add-to-cart').click(function () {
//     const quantity = $('#product-quantity').val();
//     alert(`Added ${quantity} items to the cart.`);
// });



