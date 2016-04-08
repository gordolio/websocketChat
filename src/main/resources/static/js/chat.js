var chatController = {
    stompClient: null,
    userId:null,
    sessionId:null,
    username:null,
    roomName:null,
    typingSetTimeout:null,
    typingTimeoutHit:false,
    onConnect:function(){},
    onDisconnect:function(){},
    onMessage:function(){},
    onJoin:function(){},
    onLeave:function(){},
    onTyping:function(){},
    onVote:function(){},
    onReveal:function(){},
    options:{},
    init:function(opt) {
        this.options = opt;
        this.overrideOrDefault("onConnect");
        this.overrideOrDefault("onDisconnect");
        this.overrideOrDefault("onMessage");
        this.overrideOrDefault("onJoin");
        this.overrideOrDefault("onLeave");
        this.overrideOrDefault("onTyping");
        this.overrideOrDefault("onVote");
        this.overrideOrDefault("onReveal");
    },
    overrideOrDefault:function(name) {
        if(typeof this.options[name] === 'function') {
            this[name] = this.options[name];
        }
    },
    setConnected: function (connected) {
        if(connected) {
            this.onConnect.call(this);
        } else {
            this.onDisconnect.call(this);
        }
    },
    joinRoom:function(user,room) {
        this.username = user;
        this.roomName = room;
        var me = this;
        $.ajax({
            url:"/startSession",
            contentType:"application/json;charset=UTF-8"
        }).done(function(data){
            me.sessionId = data.sessionId;
            me.connect();
        });
    },
    leaveRoom:function() {
        if(this.stompClient !== null) {
            this.stompClient.send("/chatApp/userLeave", {}, JSON.stringify({
                sessionId: this.sessionId,
                roomName: this.roomName
            }));
        }
        this.disconnect();
    },
    connect: function () {
        var me = this;
        var socket = new SockJS('/chat');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, function () {
            me.setConnected(true);
            me.stompClient.subscribe('/topic/chats-'+me.roomName, function (message) {
                var chatMessage = JSON.parse(message.body);
                me.showEvent(chatMessage);
            });
            me.stompClient.send("/chatApp/socketConnect",{},JSON.stringify({
                "sessionId":me.sessionId
            }));
            me.stompClient.send("/chatApp/joinRoom",{},JSON.stringify({
                "sessionId":me.sessionId,
                "roomName":me.roomName,
                "username":me.username
            }));
        });
    },
    disconnect: function () {
        if (this.stompClient != null) {
            this.stompClient.disconnect();
            this.stompClient = null;
        }
        this.setConnected(false);
    },
    sendMessage: function (message) {
        var data = {
            "sessionId":this.sessionId,
            "roomName":this.roomName,
            "message":message
        };
        this.stompClient.send("/chatApp/sendMessage", {}, JSON.stringify(data));
        if(this.typingSetTimeout !== null) {
            clearTimeout(this.typingSetTimeout);
            this.typingSetTimeout = null;
        }
    },
    vote:function(vote){
        var data = {
            "sessionId":this.sessionId,
            "roomName":this.roomName,
            "vote":vote
        };
        this.stompClient.send("/chatApp/userVote", {}, JSON.stringify(data));
    },
    reveal:function() {
        var data = {
            "sessionId":this.sessionId,
            "roomName":this.roomName
        };
        this.stompClient.send("/chatApp/revealVotes", {}, JSON.stringify(data));
    },
    typing:function() {
        var me = this;
        if(me.typingSetTimeout == null) {
            var data = {
                "sessionId":me.sessionId,
                "roomName":me.roomName
            };
            me.stompClient.send("/chatApp/userTyping", {}, JSON.stringify(data));
            me.typingSetTimeout = setTimeout(function() {
                me.typingSetTimeout = null;
            },4000);
        }
    },
    showEvent: function (event) {
        if(event.type === 'JoinEvent') {
            this.onJoin(event);
        } else if(event.type === 'LeaveEvent') {
            this.onLeave(event);
        } else if(event.type === 'TypingEvent') {
            this.onTyping(event);
        } else if(event.type === 'VoteEvent') {
            this.onVote(event);
        } else if(event.type === 'RevealVotesEvent') {
            this.onReveal(event);
        } else {
            this.onMessage(event);
        }
    }
};
