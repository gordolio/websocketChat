$(function() {
    var controller = {
        chatContentDiv:$("#chatContent"),
        chatUsersDiv:$("#chatUsers"),
        initChat:function(username,room){
            var me = this;
            chatController.init({
                onConnect:function(){
                    $("#roomName").html(room);
                    $("#loginArea").animateCss("hideArea",function(){
                        $(this).css("display","none");
                        $("#mainBlock").animateCss("growMainArea",function() {
                            $(this).removeClass("loginBlock").addClass("chatBlock");
                            $("#chatWrapper")
                                .css("display", "block")
                                .animateCss("showArea",function () {
                                    $(".nano").nanoScroller({
                                        alwaysVisible: true
                                    });
                                });
                        });
                    });
                },
                onDisconnect:function(){

                },
                onMessage:function(chatMessage){
                    me.chatContentDiv.append(
                        $("<p/>").text(chatMessage.username + ": " + chatMessage.message)
                            .addClass("chatLine")
                    );
                    $(".nano.chatNanoWrapper").nanoScroller().nanoScroller({ scroll: 'bottom' });
                },
                onJoin:function(joinMessage){
                    me.announce(joinMessage.username, " has joined");
                    $.each(joinMessage.allUsers,function(idx,user) {
                        if($("#userId"+user.publicId).size() === 0) {
                            me.chatUsersDiv.append(
                                $("<div></div>")
                                    .attr("id","userId"+user.publicId)
                                    .text(user.username)
                            );
                        }
                    });
                },
                onLeave:function(leaveMessage){
                    $("#userId" + leaveMessage.publicId).remove();
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
            $(".nano").nanoScroller().nanoScroller({ scroll: 'bottom' });
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
    $(window).unload(function(){
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