$(function() {

    var initChat = function(username,room){
        chatController.init({
            onConnect:function(){
                $("#roomName").html(room);
                $("#loginArea").animate({
                    "opacity":0.0
                }, {
                    "duration":300,
                    "easing":"linear",
                    "complete":function(){
                        $(this).css("display","none");
                        $("#mainBlock").animateCss("growMainArea",function(){
                            $(this).removeClass("loginBlock").addClass("chatBlock");
                            $("#chatWrapper")
                                .css("display","block")
                                .animate({
                                    opacity:1.0
                                });
                        });
                    }
                });
            },
            onDisconnect:function(){

            },
            onMessage:function(chatMessage){
                var chatContentDiv = $("#chatContent");
                chatContentDiv.append(
                    $("<p>" + chatMessage.username + ": " + chatMessage.message + "</p>")
                        .addClass("chatLine")
                );
                chatContentDiv.animate({ scrollTop: chatContentDiv.prop("scrollHeight")}, 1000);

            },
            onJoin:function(joinMessage){
                var chatContentDiv = $("#chatContent");
                chatContentDiv.append(
                    $("<p><strong>" + joinMessage.username + "</strong> has joined</p>")
                        .addClass("chatLine")
                );
                chatContentDiv.animate({ scrollTop: chatContentDiv.prop("scrollHeight")}, 1000);
            },
            onLeave:function(leaveMessage){
                var chatContentDiv = $("#chatContent");
                chatContentDiv.append($("<p><strong>" + leaveMessage.username + "</strong> has left</p>")
                    .addClass("chatLine")
                );
                chatContentDiv.animate({ scrollTop: chatContentDiv.prop("scrollHeight")}, 1000);
            }
        });
        chatController.joinRoom(username,room);
    };
    var sendTheMessage = function(){
        var sendInput = $("#sendMessage");
        chatController.sendMessage(sendInput.val());
        sendInput.val("");
    };
    $("#sendButton").click(function() {
        sendTheMessage();
    });
    $("#sendMessage").keydown(function(event){
        if ( event.which === 13 ) {
            sendTheMessage();
        }
    });
    $("#startChat").click(function() {
        var name = $("#name");
        var room = $("#room");
        if(name.val() === '') {
            name.parent().addClass("has-error");
            name.animateCss('shake');
        }
        if(room.val() === '') {
            room.parent().addClass("has-error");
            room.animateCss('shake');
        }
        if(name.val() === ''
             || room.val() === '') {
            return;
        }
        localStorage.setItem("name", name.val());
        localStorage.setItem("room", room.val());
        initChat(name.val(),room.val());
    });
    $(window).on("beforeunload",function(){
        chatController.leaveRoom();
    });
    var name = localStorage.getItem("name");
    var room = localStorage.getItem("room");
    if(!utils.isEmpty(name)) {
        $("#name").val(name);
    }
    var hashValue = utils.getHashValue();
    if(!utils.isEmpty(hashValue)) {
        $("#room").val(hashValue);
    } else if(!utils.isEmpty(room)) {
        $("#room").val(room);
    }
});