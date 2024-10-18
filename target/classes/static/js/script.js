$(document).ready(function () {

    $.ajax({
        url: "api/1.0/marketing/campaigns",
        method: "GET",
        success: function (response) {
            let campaign = response.data[0];
            $('#campaign-image').attr('src', campaign.picture);
            let story = campaign.story
                .replace(/\\n/g, '<br/>')
                .replace(/\u003C/g, '&lt;')
                .replace(/\u003E/g, '&gt;')
                .replace(/&lt;br\/&gt;/g, '<br/>');
            $('.banner-text').html(story);

            $('.banner').on('click', function () {
                window.location.href = `product.html?id=${campaign.product_id}`;
            });
        },
        error: function (error) {
            console.log("Error fetching campaign data", error);
        }
    });
});
