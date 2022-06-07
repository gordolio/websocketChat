import $ from "jquery";
import "bootstrap-sass";
import "nanoscroller";
import utils from "./utils";
import chatController from "./chat";

$(function() {

    $.fn.extend({
        animateCss: function (animationName, onComplete) {
            const animationEnd = 'webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend';
            $(this).addClass('animated ' + animationName).one(animationEnd, function() {
                if(typeof onComplete === 'function') {
                    onComplete.call(this);
                }
                $(this).removeClass('animated ' + animationName);
            });
            return this;
        }
    });

    const controller = {
        chatContentDiv:$("#chatInnerContent"),
        chatUsersDiv:$("#chatUsers"),
        typingFeedbackDiv:$("#typingFeedback"),
        usersTypingTimeouts:{},
        notifications:[],
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
            const me = this;
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
            let text = "";
            let count = 0;
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
            const me = this;
            chatController.init({
                onConnect:function(){
                    $("#roomName").html(room);
                    $("#loginArea").animateCss("hideArea",function(){
                        $(this).css("display","none");
                        const mainBlock = $("#mainBlock");
                        mainBlock.addClass("growMainArea").removeClass("loginBlock");
                        setTimeout(function() {
                            mainBlock.addClass("chatBlock");
                            $("#chatWrapper")
                                .css("display", "block")
                                .animateCss("showArea",function () {
                                    $(this).css("opacity","1");
                                    $(".nano").nanoScroller({
                                        alwaysVisible: true
                                    });
                                });
                        },500);
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
                        $("<p/>").append($("<b/>").text(chatMessage.username))
                            .append($("<span/>").text(": " + chatMessage.message))
                            .addClass("chatLine")
                    );
                    me.notify(chatMessage.username, chatMessage.message);
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
        notify:function(user, body) {
            const me = this;
            if(user === chatController.username
                || this.focused) {
                return;
            }
            if(me.titleTimeout) {
                clearTimeout(me.titleTimeout);
                delete me['titleTimeout'];
            }
            const titleMessage = user + " said...";
            const timeoutFunc = function() {
                if(document.title === titleMessage) {
                    document.title = "Hello WebSocket";
                } else {
                    document.title = titleMessage;
                }
                me.titleTimeout = setTimeout(timeoutFunc,2000);
            };
            timeoutFunc();
            if (!("Notification" in window)) {
                // not supported
            } else if(Notification.permission === "granted") {
                me.notifications.push(new Notification(user, {
                    body:body,
                    icon:"/android-chrome-192x192.png"
                }));
            } else if (Notification.permission !== "denied") {
                Notification.requestPermission().then(function (permission) {
                    // If the user accepts, let's create a notification
                    if (permission === "granted") {
                        me.notifications.push(new Notification(user,{
                            body:body,
                            icon:"/android-chrome-192x192.png"
                        }));
                    }
                });
            }
        },
        appendMessage:function(messageElem){
            this.typingFeedbackDiv.before(messageElem);
            $(".nano").nanoScroller().nanoScroller({ scroll: 'bottom' });
        },
        exitRoom:function(){
            
        },
        sendTheMessage:function(){
            const sendInput = $("#sendMessage");
            chatController.sendMessage(sendInput.val());
            sendInput.val("");
        },
        typing:function() {
            chatController.typing();
        }
    };

    controller.focused = true;
    $(window).focus(function() {
        controller.focused = true;
        clearTimeout(controller.titleTimeout);
        document.title = "Hello WebSocket";
        for(let idx=0;idx<controller.notifications.length;idx++) {
            const n = controller.notifications[idx];
            if(n) {
                n.close();
            }
            delete controller.notifications[idx];
        }
    });

    $(window).blur(function() {
        controller.focused = false;
    });
    $("div.unVote > button,div.vote-btn-group > button").click(function(){
        $("div.vote-btn-group > button").addClass("btn-default").removeClass("btn-primary");
        chatController.vote($(this).attr('data-vote'));
    });
    $("div.vote-btn-group > button").click(function() {
        $("div.vote-btn-group > button").addClass("btn-default").removeClass("btn-primary");
        $(this).addClass("btn-primary").removeClass("btn-default");
    });
    $("#revealButton").click(function(){
        const button = $(this).find("button");
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
        const name = $("#name");
        const room = $("#room");
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
    const name = localStorage.getItem("name");
    const room = localStorage.getItem("room");
    if(!utils.isEmpty(name)) {
        $("#name").val(name);
    }
    const hashValue = utils.getHashValue();
    if(!utils.isEmpty(hashValue)) {
        $("#room").val(hashValue);
    } else if(!utils.isEmpty(room)) {
        $("#room").val(room);
    }
});

