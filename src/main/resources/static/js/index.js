$(function() {
    var controller = {
        chatContentDiv:$("#chatContent"),
        chatUsersDiv:$("#chatUsers"),
        typingFeedbackDiv:$("#typingFeedback"),
        usersTypingTimeouts:{},
        typingTimeout:function(event) {
            delete this.usersTypingTimeouts[event.publicId];
            this.renderTypingDiv();
        },
        clearPossibleTimeout:function(event){
            if(typeof this.usersTypingTimeouts[event.publicId] === 'object') {
                clearTimeout(this.usersTypingTimeouts[event.publicId].timeoutId);
                this.typingTimeout(event);
            }
        },
        createTypingTimeout:function(event){
            var me = this;
            if(!utils.isEmpty(this.usersTypingTimeouts[event.publicId])) {
                clearTimeout(this.usersTypingTimeouts[event.publicId].timeoutId);
            }
            this.usersTypingTimeouts[event.publicId] = {
                username:event.username,
                publicId:event.publicId,
                timeoutId:setTimeout(function(){
                    me.typingTimeout(event);
                },6000)
            };
            this.renderTypingDiv();
        },
        renderTypingDiv:function(){
            var text = "";
            var count = 0;
            $.each(this.usersTypingTimeouts,function(publicId,val){
                if(count > 0) {
                    text += ", ";
                }
                text += val.username;
                count++;
            });
            if(count === 0) {
                this.typingFeedbackDiv.hide();
                return;
            }
            text += (count > 1 ? " are" : " is") + " typing...";
            this.typingFeedbackDiv.text(text);
            this.typingFeedbackDiv.show();
            $(".nano").nanoScroller().nanoScroller({ scroll: 'bottom' });
        },
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
                onTyping:function(event) {
                    me.createTypingTimeout(event);
                },
                onMessage:function(chatMessage){
                    me.clearPossibleTimeout(chatMessage);
                    me.appendMessage(
                        $("<p/>").text(chatMessage.username + ": " + chatMessage.message)
                            .addClass("chatLine")
                    );
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
            this.appendMessage($("<p/>")
                .append(
                    $("<strong/>").text(username)
                ).append($("<span/>").text(announcement))
                .addClass("chatLine"));
        },
        appendMessage:function(messageElem){
            this.typingFeedbackDiv.before(messageElem);
            $(".nano").nanoScroller().nanoScroller({ scroll: 'bottom' });
        },
        sendTheMessage:function(){
            var sendInput = $("#sendMessage");
            chatController.sendMessage(sendInput.val());
            sendInput.val("");
        },
        typing:function() {
            chatController.typing();
        }
    };

    $("#sendButton").click(function() {
        controller.sendTheMessage();
    });
    $("#sendMessage").keydown(function(event){
        if ( event.which === 13 ) {
            controller.sendTheMessage();
        } else {
            controller.typing();
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