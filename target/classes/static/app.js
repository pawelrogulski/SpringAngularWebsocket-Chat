let stompClient = null;
let username = document.getElementById('username').innerHTML;
let from = document.getElementById('from').innerHTML;
connect();
function connect() {
    let from = document.getElementById('from').innerHTML;
    let socket = new SockJS('/hello');
    stompClient = Stomp.over(socket);
    stompClient.connect({ username: from, }, function() {
        console.log('Web Socket is connected');
        stompClient.subscribe('/users/queue/messages', function(message) {
            messages = document.getElementById("messageTable").innerHTML;
            let msg = message.body;
            let newMessageFrom = msg.substring(0,username.length);
            if(username==newMessageFrom){
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
        connect($("#from").text());
    });
    $("#send").click(function() {
        let newMessage = document.getElementById('newMessage').value;
        stompClient.send("/app/hello", {}, getCookie('webSocketKey')+" "+username+" "+newMessage);
        messages = document.getElementById("messageTable").innerHTML;
        document.getElementById("messageTable").innerHTML=messages+"<tr><td>"+from+": "+newMessage+"</td></tr>";
        document.getElementById("newMessage").value="";
        let scroll_to_bottom =  document.getElementById("scroll");
        scroll_to_bottom.scrollTop = scroll_to_bottom.scrollHeight;
    });
});

function getCookie(cname) {
    let name = cname + "=";
    let ca = document.cookie.split(';');
    for(let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}