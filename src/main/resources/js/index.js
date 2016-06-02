$(function() {
    var controller = {
        chatContentDiv:$("#chatInnerContent"),
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
                                    $(this).css("opacity","1");
                                    $(".nano").nanoScroller({
                                        alwaysVisible: true
                                    });
                                });
                        });
                    });
                },
                onDisconnect:function() {
                    me.exitRoom();
                },
                onTyping:function(event) {
                    me.createTypingTimeout(event);
                },
                onMessage:function(chatMessage){
                    me.clearPossibleTimeout(chatMessage);
                    me.appendMessage(
                        $("<p/>").text("<b>"+ chatMessage.username "</b>: " + chatMessage.message)
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
                                    .addClass("userLine")
                                    .append($("<span class='username'>"+user.username+"</span>"))
                                    .append($("<span class='vote'></span>").addClass(user.vote))
                            );
                        }
                    });
                },
                onLeave:function(leaveMessage){
                    $("#userId" + leaveMessage.publicId).remove();
                    me.announce(leaveMessage.username, " has left");
                },
                onVote:function(vote) {
                    if(vote.vote === 'UNVOTE') {
                        me.announce(vote.username, " has un-voted.");
                    } else {
                        me.announce(vote.username, " has voted.");
                    }
                    $("#userId" + vote.publicId)
                        .find(".vote")
                        .removeClass()
                        .addClass(vote.vote + " vote");
                },
                onReveal:function(votes) {
                    me.announce(votes.username, " revealed the votes.");
                    $.each(votes.votes,function(idx,val){
                        $("#userId" + val.publicId)
                            .find(".vote")
                            .removeClass()
                            .addClass(val.vote + " vote");
                    });
                    $("#revealButton").find("button").text("Clear");
                },
                onClear:function(event){
                    me.announce(event.username, " cleared the votes.");
                    $("div.vote-btn-group > button").addClass("btn-default").removeClass("btn-primary");
                    $("#chatUsers").find(".vote").removeClass().addClass("UNVOTE vote");
                    $("#revealButton").find("button").text("Reveal");
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
        exitRoom:function(){
            
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
    $("div.unVote > button,div.vote-btn-group > button").click(function(){
        $("div.vote-btn-group > button").addClass("btn-default").removeClass("btn-primary");
        chatController.vote($(this).attr('data-vote'));
    });
    $("div.vote-btn-group > button").click(function() {
        $("div.vote-btn-group > button").addClass("btn-default").removeClass("btn-primary");
        $(this).addClass("btn-primary").removeClass("btn-default");
    });
    $("#revealButton").click(function(){
        var button = $(this).find("button");
        if(button.text() === 'Reveal') {
            chatController.reveal();
        } else {
            chatController.clear();
        }
    });

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