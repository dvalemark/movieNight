const CLIENT_ID = "108458082317-bdsimcu84edbgn4nvsok2kl3q1u89uin.apps.googleusercontent.com";

let movieTime;
let startTime;
let title;

function start() {
    gapi.load('auth2', function() {
        auth2 = gapi.auth2.init({
            client_id: CLIENT_ID,
            scope: "https://www.googleapis.com/auth/calendar.events"
        });
    });
}

$('#signinButton').click(function() {
    auth2.grantOfflineAccess().then(signInCallback);
});

function signInCallback(authResult) {

    if (authResult['code']) {

        // Hide the sign-in button now that the user is authorized, for example:
        $('#signinButton').attr('style', 'display: none');

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



$('#findDates').click(function(){
    getDates();
})

function getDates(){
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/getFreeTimes',
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        },
        contentType: 'application/octet-stream; charset=utf-8',
        success: function(freeTime) {
            document.getElementById('showDates').innerHTML = "";
            freeTime.forEach(time=> $('#showDates').append("<li>"+time+"<button type='button' class ='chooseDate' id= '"+time+"'>Choose this date</button></li>"))
        },
        error: function (result) {
            console.log("error" + result.httpRequestStatusCode)
        },
        processData: false,
    });
}

$(document).on('click', '.chooseDate', function () {
    startTime = $(this).attr('id');
    alert("time chosen!");
})

$('#searchMovieButton').click(function(){
    var search = $('#movieSearch').val();

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/searchMovie/"+search,
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        },
        contentType: 'application/octet-stream; charset=utf-8',
        success: function(result) {
            document.getElementById("showMovieList").innerHTML = "";
            result.forEach(movie => $('#showMovieList').append("<li>"+movie.Title+"<button type='button' class='showTest' id = '"+movie.imdbID+"'>Choose this film</button></li>"))
        },
        error: function (result) {
            console.log("error" + result.httpRequestStatusCode)
        },
        processData: false,
    })
});

$(document).on('click','.showTest',function(){

    var id = $(this).attr('id')
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/getById/"+id,
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        },
        contentType: 'application/octet-stream; charset=utf-8',
        success: function(result) {

            $('#showOneMovie').append("<ul></ul>");
            $('#showOneMovie').append("<li><img src="+result.poster+"></li>");
            $('#showOneMovie').append("<li ><div id ='movietitle'>"+result.title+"</div><button type='button' class='selectedMovie' id= '"+result.title+"'>choose this movie</button></li>");
            $('#showOneMovie').append("<li id='movieRuntime' value = "+result.runtime+">"+result.runtime+"</li>");


        },
        error: function (result) {
            console.log("error" + result.httpRequestStatusCode)
        },
        processData: false,
    })
});


$(document).on('click', '.selectedMovie', function () {
     title = $('#movietitle').text();
     movieTime = $('#movieRuntime').text();
    alert("Movie chosen!");
})



$('#createEvent').on('click', function addEvent() {
    if(movieTime == null || startTime == null || title==null){
        alert("you need to choose a movietime and a movie!")
    }
    else{
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/addEvent/'+movieTime+'/'+startTime+'/'+title,
        // Always include an `X-Requested-With` header in every AJAX request,
        // to protect against CSRF attacks.
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        },
        contentType: 'application/octet-stream; charset=utf-8',
        success: function () {
            console.log("Event created")
            movieTime ="";
            startTime="";
            title="";
            getDates();
            alert("Event created!")
        },
        error: function (result) {
            console.log("error no event was created")
        },
        processData: false,
    })}
})