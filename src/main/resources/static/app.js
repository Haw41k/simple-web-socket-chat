"use strict";
{

    const TOPIC_CHAT_MESSAGES = "/topic/main";
    const TOPIC_USER_LIST = "/topic/userlist";

    const APP_CHAT_MESSAGE = "/app/main";
    const APP_INIT_MESSAGE_LIST = "/app/init/messagelist";
    const APP_USER = "/app/user";

    let messageForm;
    let messageInput;
    let messageList;

    let userForm;
    let userName;
    let userList;

    let connectionSwitch;

    let stompClient = null;

    $(initApp);

    function initApp(){

        messageForm = $("#message-form");
        messageInput = $("#input-chatMessage");
        messageList = $("#chat");

        userForm = $("#user-name-form");
        userName = $("#name");
        userList = $("#user-list");

        connectionSwitch = $("#connection-switch");

        connectionSwitch.click(function () {
            if (connectionSwitch.prop("checked")) {
                connect();
            } else {
                disconnect();
            } 
        });

        messageForm.on("submit", function (e) { 
            e.preventDefault();
            sendChatMessage(); 
        });
        userForm.on("submit", function (e) { 
            e.preventDefault();
            setUserName();
        });

        connect();
    }

    function connect(){
        let socket = new SockJS("/ws");
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnect, onConnectionError);
    }
    
    function onConnect(){
        
        stompClient.subscribe(
            TOPIC_USER_LIST,
            function (msg) {
                updateUserList(JSON.parse(msg.body));
            }
        );

        stompClient.subscribe(
            TOPIC_CHAT_MESSAGES,
            function (msg) {
                showChatMessage(JSON.parse(msg.body));
            }
        );
        
        stompClient.subscribe(
            APP_INIT_MESSAGE_LIST,
            function (msg) {
                updateChatMessageList(JSON.parse(msg.body));
            }
        );

        connectionSwitch.prop("checked", true);
    }
    
    function disconnect(){
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        onDisconnect();
    }

    function onDisconnect(){
        userList.empty();
    }

    function onConnectionError(){
        connectionSwitch.prop("checked", false);
        onDisconnect();
    }

    function sendChatMessage(){
        stompClient.send(
            APP_CHAT_MESSAGE,
            {},
            JSON.stringify({ 'body': messageInput.val() }));

        messageInput.val("");
    }

    function showChatMessage(msg){
        messageList.append(
            `<p class="chat-message">
                <span class="chat-message-date">[${new Date(msg.creationDate).toLocaleString()}] </span>
                <span class="chat-message-sender-name">${msg.senderName}: </span>
                <span class="chat-message-body">${msg.body}</span>
            </p>`
            );
        
        messageList.scrollTop(messageList[0].scrollHeight);
    }

    function updateChatMessageList(messages){
        messageList.empty();
        
        messages.forEach(message => {
            showChatMessage(message);
        });
    }
    
    function setUserName(){
        stompClient.send(
            APP_USER,
            {},
            JSON.stringify(
                { "name" : userName.val() }
            ));
    }

    function updateUserList(users){
        
        userList.empty();

        users.forEach(user => {
            userList.append(`<li class="list-group-item">${user.name}</li>`);
        });
    }

}