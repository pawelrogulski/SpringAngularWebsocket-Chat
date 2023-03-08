import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { Chat } from './chat';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  constructor(private httpClient: HttpClient, private route: ActivatedRoute) { }
  id:number;
  chat:Chat;

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

}
