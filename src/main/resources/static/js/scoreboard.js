$( document ).ready(function() {
    var data;

    $.ajax({
        url : '/scores',
        method : 'GET',
        async : false,
        success : function(response) {
            data = response;
        },
        error: function(){
            alert('Error while request..');
        }
    });

    var scores = data.scores;

    var table = $("#scoreTable");

    var tableHtml = "<tr><td>Naam</td><td>Score</td></tr>";

    for(var i = 0; i < scores.length; i++) {
        tableHtml += "<tr>" +
            "<td>" + scores[i].username + "</td>" + "<td>" + scores[i].score + "</td>"
            + "</tr>";
    }

    table.html(tableHtml);

});