function BattleshipController(callbacks) {
    this.gameId=null;
    this.playerName=null;
    this.opponentName=null;
    this.stompClient=null;
    this.callbacks=callbacks;
    this.eventHandler=function(event){
        var match = /^(.*)Event$/.exec(event.type);
        var callback = "on" + match[1];
        if(typeof this[callback] === 'function') {
            this[callback](event);
        }
        if(typeof this.callbacks[callback] === 'function') {
            this.callbacks[callback](event);
        }
    };
    this.onNewGame=function(event){
        this.gameId = event.gameId;
    };
    this.onBeginName=function(event) {
        this.gameId = event.gameId;
    };
    this.newGame=function(playerName) {
        var me = this;
        this.playerName = playerName;
        this.__connect(function(){
            me.stompClient.send('/app/newGame',{},JSON.stringify({
                playerName:playerName
            }));
        });
    };
    this.joinGame=function(playerName, gameId) {
        var me = this;
        this.__connect(function(){
            me.stompClient.send('/app/joinGame',{},JSON.stringify({
                playerName:playerName,
                gameId:gameId
            }));
        });
    };
    this.joinObjects=function(obj1,obj2) {
        var newObj = {};
        for(var obj in [obj1,obj2]) {
            for(var key in obj) {
                if(obj.hasOwnProperty(key)) {
                    newObj[key] = obj[key];
                }
            }
        }
        return newObj;
    };
    this.sendMessage=function(route,body) {
        var newBody = this.joinObjects({
            gameId:this.gameId
        },body);
        this.stompClient.send('/app/'+route,{},JSON.stringify(newBody));
    };

    this.__connect=function(callback) {
        var me = this;
        var socket = new SockJS('/stomp');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({},function(){
            me.stompClient.subscribe('/user/queue/gameEvents',function(message){
                me.eventHandler(JSON.parse(message.body));
            });
            if(typeof callback === 'function') {
                callback();
            }
        });
    };
}