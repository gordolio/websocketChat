$(function() {
    var controller = {
        chatContentDiv:$("#chatContent"),
        initChat:function(username,room){
            var me = this;
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
                    me.chatContentDiv.append(
                        $("<p/>").text(chatMessage.username + ": " + chatMessage.message)
                            .addClass("chatLine")
                    );
                    me.chatContentDiv.animate({ scrollTop: me.chatContentDiv.prop("scrollHeight")}, 1000);
                },
                onJoin:function(joinMessage){
                    me.announce(joinMessage.username, " has joined");
                },
                onLeave:function(leaveMessage){
                    me.announce(leaveMessage.username, " has left");
                }
            });
            chatController.joinRoom(username,room);
        },
        announce:function(username, announcement){
            this.chatContentDiv.append(
                $("<p/>")
                    .append(
                        $("<strong/>").text(username)
                    ).append($("<span/>").text(announcement))
                    .addClass("chatLine")
            );
            this.chatContentDiv.animate({ scrollTop: this.chatContentDiv.prop("scrollHeight")}, 1000);
        },
        sendTheMessage:function(){
            var sendInput = $("#sendMessage");
            chatController.sendMessage(sendInput.val());
            sendInput.val("");
        }
    };

    $("#sendButton").click(function() {
        controller.sendTheMessage();
    });
    $("#sendMessage").keydown(function(event){
        if ( event.which === 13 ) {
            controller.sendTheMessage();
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
        controller.initChat(name.val(),room.val());
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