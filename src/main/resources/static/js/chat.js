var chatController = {
    stompClient: null,
    username:null,
    room:null,
    onConnect:function(){},
    onDisconnect:function(){},
    onMessage:function(){},
    onJoin:function(){},
    onLeave:function(){},
    options:{},
    init:function(opt) {
        this.options = opt;
        this.overrideOrDefault("onConnect");
        this.overrideOrDefault("onDisconnect");
        this.overrideOrDefault("onMessage");
        this.overrideOrDefault("onJoin");
        this.overrideOrDefault("onLeave");
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
    joinRoom:function(user,roomName) {
        this.username = user;
        this.room = roomName;
        this.connect();
    },
    leaveRoom:function() {
        this.stompClient.send("/chatApp/userLeave",{},JSON.stringify({
            "userId":this.username,
            "sessionId":this.room
        }));
        this.disconnect();
    },
    connect: function () {
        var me = this;
        var socket = new SockJS('/chat');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, function () {
            me.setConnected(true);
            me.stompClient.subscribe('/topic/chats-'+me.room, function (message) {
                var chatMessage = JSON.parse(message.body);
                me.showMessage(chatMessage);
            });
            me.stompClient.send("/chatApp/userJoin",{},JSON.stringify({
                "userId":me.username,
                "sessionId":me.room
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
            "sessionId":this.room,
            "userId":this.username,
            "message":message
        };
        this.stompClient.send("/chatApp/sendMessage", {}, JSON.stringify(data));
    },
    showMessage: function (chatMessage) {
        if(chatMessage.type === 'JoinNotify') {
            this.onJoin(chatMessage);
        } else if(chatMessage.type === 'LeaveNotify') {
            this.onLeave(chatMessage);
        } else {
            this.onMessage(chatMessage);
        }
    }
};
