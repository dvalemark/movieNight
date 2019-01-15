const CLIENT_ID = "108458082317-bdsimcu84edbgn4nvsok2kl3q1u89uin.apps.googleusercontent.com";


function start() {
    gapi.load('auth2', function() {
        auth2 = gapi.auth2.init({
            client_id: CLIENT_ID,
            scope: "https://www.googleapis.com/auth/calendar.events"
        });
    });
}

$('#signinButton').click(function() {
// signInCallback defined in step 6.
    auth2.grantOfflineAccess().then(signInCallback);
});

function signInCallback(authResult) {
    console.log('authResult', authResult);
    if (authResult['code']) {

        // Hide the sign-in button now that the user is authorized, for example:
        $('#signinButton').attr('style', 'display: none');
        {1}
        // Send the code to the server
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/storeauthcode',
            // Always include an `X-Requested-With` header in every AJAX request,
            // to protect against CSRF attacks.
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            },
            contentType: 'application/octet-stream; charset=utf-8',
            success: function(result) {
                // Handle or verify the server response.
            },
            processData: false,
            data: authResult['code']
        });
    } else {
        }

}

function addEvent(){
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/addEvent',
        // Always include an `X-Requested-With` header in every AJAX request,
        // to protect against CSRF attacks.
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        },
        contentType: 'application/octet-stream; charset=utf-8',
        success: function(result) {

        },
        processData: false,
        data: authResult['code']
    })
}