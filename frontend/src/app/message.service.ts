import { Injectable } from '@angular/core';
declare var SockJS;
declare var Stomp;

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor() {
    this.initializeWebSocketConnection();
  }
  public stompClient;
  public msg = [];
  initializeWebSocketConnection() {
    const serverUrl = 'http://localhost:8080/chat';
    console.log(serverUrl);
    const ws = new SockJS(serverUrl);
    this.stompClient = Stomp.over(ws);
    const that = this;
    this.stompClient.connect({}, function(frame) {
      that.stompClient.subscribe('/users/queue/messages', (message) => {
        if (message.body) {
          that.msg.push(message.body);
        }
      });
    });
  }

  sendMessage(message) {
    this.stompClient.send('/chat' , {}, message);
  }
}
