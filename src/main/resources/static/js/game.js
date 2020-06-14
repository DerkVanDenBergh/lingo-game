function startTimer(time, gameEnd) {
    clearInterval(window.interval);
    var countdown = new Date(Date.now()+time*1000)
    var timer = document.getElementById("timerClock");

    window.interval = setInterval(function() {
        var timeLeft = countdown.getTime() - Date.now();
        if((timeLeft < 0) && (gameEnd)) {
            clearInterval(interval);
            timeLeft = 0;
            gameOver();
        }
        timer.innerText = Math.round(timeLeft/1000);
    }, 1000);
}


$( document ).ready(function() {
    var gameInfo = createGame();

    gameId = gameInfo.gameId;
    window.round = 1;

    $("#gameFeedback").html("Ronde " + window.round).css("color", "cornflowerblue");

    initializeLingoTable(gameInfo.firstLetter, 5);

    $("#inputFieldText").focus();

    $("#inputFieldText").on("keydown",function search(e) {
        if(e.keyCode === 13) {

            input = $(this).val();

            var table = $("#lingoTable");

            if(input.length > table.find("tr:first td").length) {
                while(input.length > table.find("tr:first td").length) {
                    input = input.substring(0, input.length - 1);
                }
            }

            if(input.length < table.find("tr:first td").length) {
                while(input.length < table.find("tr:first td").length) {
                    input += ".";
                }
            }
            var data;

            $.ajax({
                url : '/games/' + gameId + "/checkWord/" + input,
                method : 'GET',
                async : false,
                success : function(response) {
                    data = response;
                },
                error: function(){
                    alert('Error while request..');
                }
            });

            if(data.status === "won") {
                processFeedback(data, $(this).val());

                setTimeout(function() {
                    var audio = new Audio('../sound/succes.mp3');
                    audio.play();
                },data.feedback.length * 250);


                setTimeout(function() {
                    nextRound(gameId)
                },5000);

            } else if(data.status === "lost") {
                processFeedback(data, $(this).val());
                gameOver();
            } else {
                processFeedback(data, $(this).val());
            }
            $(this).val("");
        }
    });
});

function createGame() {
    var data;

    $.ajax({
        url : '/games/new',
        method : 'GET',
        async : false,
        success : function(response) {
            data = response;
        },
        error: function(){
            alert('Error while request..');
        }
    });

    return data;
}

function nextRound(gameId) {
    var data;

    $.ajax({
        url : '/games/' + gameId + '/nextRound',
        method : 'GET',
        async : false,
        success : function(response) {
            data = response;
        },
        error: function(){
            alert('Error while request..');
        }
    });

    $("#gameFeedback").html("Ronde " + window.round++).css("color", "cornflowerblue");
    initializeLingoTable(data.firstLetter, data.wordLength);
}

function initializeLingoTable(firstLetter, wordLength) {
    tableHtml = '<tr>';
    tableHtml += '<td></td>'.repeat(wordLength);
    tableHtml += '</tr>';
    tableHtml = tableHtml.repeat(5);

    var table = $("#lingoTable");
    table.html(tableHtml);
    table.find("tr:first td").html(".");
    table.find("td:first").html(firstLetter);
    table.find("td").css({
        "width": (100 / wordLength) + "%"
    });
    table.find("tr").css({
        "height": "20%"
    });

    startTimer(60, true);
}

async function processFeedback(data, word) {
    $("#scoreCount").html(data.score);
    var row = $("#lingoTable tr").eq(data.turn);
    var correctWord = ".".repeat(data.feedback.length);
    for(i = 0; i < data.feedback.length; i++) {
        if(data.feedback.charAt(i) === "c") {
            row.find("td").eq(i).html(word.charAt(i)).css("background-color", "red");
            correctWord = correctWord.substring(0, i) + word.charAt(i) + correctWord.substring(i + 1);
            var audio = new Audio('../sound/correct.mp3');
            audio.play();
        } else if(data.feedback.charAt(i) === "a") {
            row.find("td").eq(i).html(word.charAt(i)).css("background-color", "yellow");
            var audio = new Audio('../sound/present.mp3');
            audio.play();
        } else {
            row.find("td").eq(i).html(word.charAt(i)).css("background-color", "cornflowerblue");
            var audio = new Audio('../sound/incorrect.mp3');
            audio.play();
        }
        await new Promise(done => setTimeout(() => done(), 250));
    }

    if((data.turn != 5) && (data.status != "won") && data.status != "lost") {
        var nextRow = $("#lingoTable tr").eq(data.turn + 1);
        for(i = 0; i < data.feedback.length; i++) {
            nextRow.find("td").eq(i).html(correctWord.charAt(i));
        }
    }

}

function gameOver() {
    var data;

    $.ajax({
        url : '/games/' + gameId + '/end',
        method : 'GET',
        async : false,
        success : function(response) {
            data = response;
        }
    });

    var audio = new Audio('../sound/error.mp3');
    audio.play();

    startTimer(0, false);
    clearInterval(window.interval);
    $("#gameFeedback").html("Game over!\nHet woord was '" + data.word + "'!").css("color", "red");
    $("#gameButtons").show();
    $("#inputFieldText").remove();
}


