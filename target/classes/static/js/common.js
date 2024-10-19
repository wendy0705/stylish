$(document).ready(function () {
    const urlParams = new URLSearchParams(window.location.search);
    // console.log('Current URL:', window.location.href);
    // console.log('URLSearchParams:', urlParams.toString());

    if (urlParams.has('category')) {
        const category = urlParams.get('category');
        // console.log('category', category);
        fetchProducts(category, 0);
    } else {
        fetchProducts("all", 0);
    }

    $('#user-button').click(function () {
        window.location.href = `profile.html`;
    });

});


function fetchProducts(category, page) {
    $.ajax({
        url: `api/1.0/products/${category}`,
        type: 'GET',
        data: {paging: page},
        success: function (data) {
            if (!data.data.length && page > 0) return;

            const productList = $('#product-list');
            if (page === 0) {
                productList.empty();
            }
            productList.data('page', page);
            console.log(data);

            data.data.forEach(product => {
                console.log(product);
                const colorCodes = product.colors.map(color => `<span style="background-color: #${color.code}; width: 20px; height: 20px; display: inline-block;"></span>`).join(' ');
                const productItem = $(`
                    <div class="product-item" data-id="${product.id}">
                        <img src="${product.main_image}" alt="${product.title}">
                        <div class="product-title">${product.title}</div>
                        <div class="product-price">TWD.${product.price}</div>
                        <div class="product-colors">${colorCodes}</div>
                    </div>
                `);
                productList.append(productItem);
            });
            $('.product-item img').on('click', function () {
                const productId = $(this).closest('.product-item').data('id');
                window.location.href = `product.html?id=${productId}`;
            });
        },
        error: function (jqXHR) {
            console.error('Error:', jqXHR.responseJSON.error);
        }
    });
}

