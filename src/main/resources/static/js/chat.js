var chatController = {
    stompClient: null,
    userId:null,
    sessionId:null,
    username:null,
    roomName:null,
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
    joinRoom:function(user,room) {
        this.username = user;
        this.roomName = room;
        var me = this;
        $.ajax({
            url:"/startChat",
            contentType:"application/json;charset=UTF-8",
            data:{
                username:this.username
            }
        }).done(function(data){
            me.sessionId = data.sessionId;
            me.connect();
        });
    },
    leaveRoom:function() {
        if(this.stompClient !== null) {
            this.stompClient.send("/chatApp/userLeave", {}, JSON.stringify({
                "userId": this.userId,
                "sessionId": this.sessionId
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
                me.showMessage(chatMessage);
            });
            me.stompClient.send("/chatApp/userJoin",{},JSON.stringify({
                "sessionId":me.sessionId,
                "roomName":me.roomName
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
    },
    showMessage: function (chatMessage) {
        if(chatMessage.type === 'JoinEvent') {
            this.onJoin(chatMessage);
        } else if(chatMessage.type === 'LeaveEvent') {
            this.onLeave(chatMessage);
        } else {
            this.onMessage(chatMessage);
        }
    }
};
