$(document).ready(function () {

    const token = localStorage.getItem('token');
    if (!token || token === 'undefined') {
        $('#auth-container').show();
    } else {
        $.ajax({
            url: 'api/1.0/user/profile',
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (response) {
                console.log('Response received:', JSON.stringify(response, null, 2));

                // Show the user profile information
                $('#user-name').text(response.data.name);
                $('#user-email').text(response.data.email);
                $('#user-picture').attr('src', response.data.picture);
                $('#user-profile').show();
            },
            error: function (error) {
                console.error('Error fetching profile:', error);
                alert('Failed to fetch profile. Please sign in again.');
                localStorage.removeItem('token');
                $('#auth-container').show();
            }
        });
    }

    $('#login-form').submit(function (event) { //native sign in listener
        event.preventDefault();

        const email = $('#login-email').val();
        const password = $('#login-password').val();

        const requestBody = {
            provider: "native",
            email: email,
            password: password
        };

        // console.log('Request body:', requestBody);

        $.ajax({ //get native sign in token
            url: 'api/1.0/user/signin',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(requestBody),
            success: function (response) {
                const accessToken = response.data.access_token;
                localStorage.setItem('token', accessToken);
                window.history.back();
            },
            error: function (error) {
                console.error('Error logging in:', error);
            }
        });
    });

    (function (d, s, id) { //fb sign in listener
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) {
            return;
        }
        js = d.createElement(s);
        js.id = id;
        js.src = "https://connect.facebook.net/en_US/sdk.js";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));

    window.fbAsyncInit = function () {
        FB.init({
            appId: '887781579838690',
            cookie: true,
            xfbml: true,
            version: 'v20.0'
        });

        FB.AppEvents.logPageView();
    };

    $('#register-form').submit(function (event) { //native sign up listener
        event.preventDefault();

        const name = $('#register-name').val();
        const email = $('#register-email').val();
        const password = $('#register-password').val();

        const requestBody = {
            name: name,
            email: email,
            password: password
        };

        console.log(requestBody);

        $.ajax({ //get native sign up token
            url: 'api/1.0/user/signup',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(requestBody),
            success: function (response) {
                const accessToken = response.data.access_token;
                localStorage.setItem('token', accessToken);
                window.history.back();
            },
            error: function (error) {
                console.error('Error logging in:', error);
            }
        });
    });
});

function checkLoginState() {
    FB.getLoginStatus(function (response) {
        statusChangeCallback(response);
    });
}

function statusChangeCallback(response) {
    if (response.status === 'connected') {
        // User is logged into Facebook and your app
        const accessToken = response.authResponse.accessToken;
        console.log("Access Token: " + accessToken);
        sendLoginInfoToBackend(accessToken);
    } else {
        // User is not logged into Facebook or your app
        console.log('User not authenticated');
    }
}

// Send login info to backend
function sendLoginInfoToBackend(accessToken) {
    $.ajax({ //get fb sign in token
        type: 'POST',
        url: 'api/1.0/user/signin',
        contentType: 'application/json',
        data: JSON.stringify({
            provider: "facebook",
            access_token: accessToken
        }),
        success: function (response) {
            const accessToken = response.data.access_token;
            localStorage.setItem('token', accessToken);
            // console.log('Access token stored:', accessToken);
            // console.log(localStorage);
            // console.log(response);
            window.history.back();
        },
        error: function (error) {
            console.log('Login failed:', error);
        }
    });

}
