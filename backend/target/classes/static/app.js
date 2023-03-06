let stompClient = null;
let recipient = document.getElementById('recipient').innerHTML;
let user = document.getElementById('user').innerHTML;
connect();
function connect() {
    let user = document.getElementById('user').innerHTML;
    let socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function() {
        console.log('Web Socket is connected');
        stompClient.subscribe('/users/queue/messages', function(message) {
            messages = document.getElementById("messageTable").innerHTML;
            let msg = message.body;
            let receivedMessage = msg.substring(0,user.length);
            if(recipient==receivedMessage){
                document.getElementById("messageTable").innerHTML=messages+"<tr><td>"+message.body+"</td></tr>";
                let scroll_to_bottom =  document.getElementById("scroll");
                scroll_to_bottom.scrollTop = scroll_to_bottom.scrollHeight;
            }
        });
    });
}

$(function() {
    $("form").on('submit', function(e) {
        e.preventDefault();
    });
    $("#connect").click(function() {
        connect($("#user").text());
    });
    $("#send").click(function() {
        let newMessage = document.getElementById('newMessage').value;
        stompClient.send("/chat", {}, recipient+" "+newMessage);
        messages = document.getElementById("messageTable").innerHTML;
        document.getElementById("messageTable").innerHTML=messages+"<tr><td>"+user+": "+newMessage+"</td></tr>";
        document.getElementById("newMessage").value="";
        let scroll_to_bottom =  document.getElementById("scroll");
        scroll_to_bottom.scrollTop = scroll_to_bottom.scrollHeight;
    });
});