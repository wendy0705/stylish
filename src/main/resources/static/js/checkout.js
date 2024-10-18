document.addEventListener('DOMContentLoaded', function () {
    const addToCartButton = document.getElementById('add-to-cart');
    const tpFields = document.querySelectorAll('.tpfield');
    const submitButton = document.getElementById('submit-button');

    addToCartButton.addEventListener('click', function () {
        tpFields.forEach(field => field.classList.add('visible'));
        submitButton.classList.add('visible');
    });
    const APP_ID = '12348';
    const APP_KEY = 'app_pa1pQcKoY22IlnSXq5m5WP5jFKzoRG58VEXpT7wU62ud7mMbDOGzCYIlzzLF';

    TPDirect.setupSDK(APP_ID, APP_KEY, 'sandbox');

    const fields = {
        number: {
            element: '#card-number',
            placeholder: '**** **** **** ****'
        },
        expirationDate: {
            element: document.getElementById('card-expiration-date'),
            placeholder: 'MM / YY'
        },
        ccv: {
            element: '#card-ccv',
            placeholder: 'ccv'
        }
    };

    TPDirect.card.setup({
        fields: fields,
        styles: {
            'input': {
                'color': 'gray'
            },
            '.valid': {
                'color': 'green'
            },
            '.invalid': {
                'color': 'red'
            },
            '@media screen and (max-width: 400px)': {
                'input': {
                    'color': 'orange'
                }
            }
        },
        isMaskCreditCardNumber: true,
        maskCreditCardNumberRange: {
            beginIndex: 6,
            endIndex: 11
        }
    });

    TPDirect.card.onUpdate(function (update) {
        const submitButton = document.getElementById('submit-button');

        if (update.canGetPrime) {
            submitButton.removeAttribute('disabled');
        } else {
            submitButton.setAttribute('disabled', true);
        }
    });

    document.getElementById('submit-button').addEventListener('click', function (event) {
        event.preventDefault();

        // Get TapPay prime value
        const tappayStatus = TPDirect.card.getTappayFieldsStatus();

        if (!tappayStatus.canGetPrime) {
            alert('Cannot get prime');
            return;
        }

        TPDirect.card.getPrime((result) => {
            if (result.status !== 0) {
                alert('Get prime error: ' + result.msg);
                return;
            }

            const prime = result.card.prime;
            localStorage.setItem("paymentPrime", prime);
            console.log("prime: " + prime);

            document.dispatchEvent(new Event('primeAcquired'));

        });
    });
});
