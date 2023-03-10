import { HttpClient } from '@angular/common/http';
import { Component, OnInit, HostListener, ElementRef, ViewChild, AfterViewChecked, Renderer2 } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { Chat } from './chat';
import { TokenStorageService } from '../Services/token-storage.service';
declare var SockJS;
declare var Stomp;

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit, AfterViewChecked {

  @ViewChild('scroll',{static:false}) private scrollContainer: ElementRef;
  @ViewChild('newMessagePanel',{static:false}) private newMessagePanel: ElementRef;

  constructor(private httpClient: HttpClient,
    private route: ActivatedRoute,
    private renderer:Renderer2,
    private tokenStorage: TokenStorageService) {}

  id:number;
  chat:Chat;
  input:string;
  public stompClient;
  public msg = [];
  public ws;

  ngOnInit() {
    this.closeConnection();
    this.id = this.route.snapshot.params['id'];
    this.chat = new Chat();
    this.getChatData(this.id).subscribe(data =>{
      this.chat = data;
    })
    this.initializeWebSocketConnection();
  }
  ngAfterViewChecked() {
    this.scrollToBottom();
}

  getChatData(chatId:number): Observable<Chat>{
    return this.httpClient.get<Chat>(`http://localhost:8080/api/app/conversation/${chatId}`,{withCredentials:true});
  }
  sendMessage() {
    if(this.input){
      this.stompClient.send('/chat' , {},this.chat.recipient+' '+this.input);
      this.updateMessages(this.chat.username+': '+this.input);
      this.input = '';
      this.scrollToBottom();
    }
  }

  @HostListener('document:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.sendMessage();
    }
  }

  scrollToBottom(){
    this.scrollContainer.nativeElement.scrollTop = this.scrollContainer.nativeElement.scrollHeight;
  }

  updateMessages(msg:string){
    if(msg.split(": ",1)[0]===this.chat.recipient || msg.split(": ",1)[0]===this.chat.username){
      const newMessage = this.renderer.createElement('div');
      const text = this.renderer.createText(msg);
      this.renderer.appendChild(newMessage,text);
      this.renderer.appendChild(this.newMessagePanel.nativeElement,newMessage);
    }

  }

  initializeWebSocketConnection() {
    const serverUrl = 'http://localhost:8080/chat';
    console.log(serverUrl);
    this.ws = new SockJS(serverUrl);
    this.stompClient = Stomp.over(this.ws);
    const that = this;
    this.stompClient.connect({token: this.tokenStorage.getToken()}, function(frame) {
      that.stompClient.subscribe('/users/queue/messages', (message) => {
        if (message.body) {
          that.msg.push(message.body);
          that.updateMessages(message.body);
        }
      });
    });
  }

  closeConnection(){
    if(this.ws!==undefined){
      this.ws.close();
    }
  }
}
