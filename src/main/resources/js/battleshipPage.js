$(function(){
    var battleship = new BattleshipController({
        onBeginGame:function(event) {
            $("#status").text("Game Started");
            var opponent;
            if(event.playerOneName !== $("#playerName").val()) {
                opponent = event.playerOneName;
            } else {
                opponent = event.playerTwoName;
            }
            $("#opponent").find("span").text(opponent);
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

    var createBoard = function() {
        var rows = [];
        for(var row=0;row<10;row++) {
            rows[row] = {cols:[]};
            for(var col=0;col<10;col++) {
                rows[row].cols[col] = {val:' '};
            }
        }
        return {rows:rows};
    };

    var boardTemplate = Handlebars.compile($("#board-template").html());

    $("#board1").html(boardTemplate(createBoard()));

    var boardModel = {
        pieces:[]
    };

    var buildModel = function(size,horizontal) {
        var actualSize = size-1;
        var model = {
            first:{},
            middle:{},
            last:{}
        };

        if(horizontal) {
            model.width=size * 39;
            model.height=40;
            model.first.d="M 8,20.5 L 38.76,31.59 67.54,21.45 67.54,10.55 38.76,8.41 8,18 Z M 8,17";

            model.middle.x=37;
            model.middle.y=8;
            model.middle.width=actualSize * 39 - 12;
            model.middle.height=24;

            model.last.x=actualSize * 40 + 9;
            model.last.y=8;
            model.last.width=20;
            model.last.height=24;
        } else {
            model.width=40;
            model.height=size * 39;
            model.first.d = "M 20.5,8 L 31.59,38.76 21.45,67.54 10.55,67.54 8.41,38.76 18,8 Z M 17,8";

            model.middle.x=8;
            model.middle.y=37;
            model.middle.width=24;
            model.middle.height=actualSize * 39 - 12;

            model.last.x=8;
            model.last.y=actualSize * 40 + 9;
            model.last.width=24;
            model.last.height=20;
        }
        return model;
    };
    var isSpotOcupied = function(x,y) {
        var occupied = false;
        $.each(boardModel.pieces,function(idx,piece) {
            $.each(piece.positions,function(idx,pos){
                if(pos.x === x || pos.y === y) {
                    occupied = true;
                }
            });
        });
        return occupied;
    };
    var pieceTemplate = Handlebars.compile($("#ship-template").html());
    var sizes = [2,3,4,4,5];
    $.each(sizes,function(idx,size){
        var ship_battleship = $(pieceTemplate(buildModel(size,true)));
        $(".pieces").append(ship_battleship);
    });

});