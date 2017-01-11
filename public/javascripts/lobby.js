let websocket;
let webSocketAddress = "wss://strategoweb.herokuapp.com/ws";
// let webSocketAddress = "ws://localhost:9000/ws";

function initWebSocket() {
    websocket = new WebSocket(webSocketAddress);
    websocket.onopen = function(evt) { onOpen(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onerror = function(evt) { onError(evt) };
}

function onOpen(evt) {
    alert("CONNECTED");
    // TODO: unnötig
    //let json = {'command': "lobby-join"};
    //websocket.send(JSON.stringify(json));
}

function onClose(evt) {
    alert("DISCONNECTED");
}

function onMessage(evt) {
    //alert("RESPONSE: " + evt.data);
    //console.log("ws received " + evt.data);
    refresh(JSON.parse(evt.data));
    //websocket.close();

}

function onError(evt) {
    alert("ERROR: " + evt.data);
}

$(function() {
    initWebSocket();
});


function newGame() {
    let json = {'command': "new"};
    websocket.send(JSON.stringify(json));
}

function refresh(lobbyJson) {
    let {users: users, openGames: openGames} = lobbyJson;
    users.forEach(item => console.log(item));
}















