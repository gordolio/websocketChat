$(function(){
    var battleship = new BattleshipController({
        onBeginGame:function(event) {
            $("#status").text("Game Started");
        },
        onNewGame:function(event) {
            $("#gameId").val(event.gameId);
            $("#status").text("Connected");
        }
    });
    $("#newGame").click(function(){
        var playerName = $("#playerName").val();
        battleship.newGame(playerName);
    });

    $("#joinGame").click(function(){
        var playerName = $("#playerName").val();
        var gameId = $("#gameId").val();
        battleship.joinGame(playerName, gameId);
    });
});