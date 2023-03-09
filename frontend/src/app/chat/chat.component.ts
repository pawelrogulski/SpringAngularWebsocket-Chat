import { HttpClient } from '@angular/common/http';
import { Component, OnInit, HostListener } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { Chat } from './chat';
import {MessageService} from '../message.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  constructor(private httpClient: HttpClient, private route: ActivatedRoute, private messageService: MessageService) { }
  id:number;
  chat:Chat;
  input:string;

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    this.chat = new Chat();
    this.getChatData(this.id).subscribe(data =>{
      this.chat = data;
    })
  }

  getChatData(chatId:number): Observable<Chat>{
    return this.httpClient.get<Chat>(`http://localhost:8080/conversation/${chatId}`,{withCredentials:true});
  }
  sendMessage() {
    if (this.input) {
      this.messageService.sendMessage(this.chat.recipient+' '+this.input);
      this.input = '';
    }
  }

  @HostListener('document:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.sendMessage();
    }
  }
}
