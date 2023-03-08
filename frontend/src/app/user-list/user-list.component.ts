import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {
  users: User[];
  chatId: number;

  constructor(private userService: UserService, private router: Router, private httpClient: HttpClient) { }

  ngOnInit() :void {
    this.getUsers();
  }

  private getUsers(){
    this.userService.getUsersList().subscribe(data => {
      this.users = data;
    })
  }

  getConversationId(userId: number): Observable<number>{
    return this.httpClient.get<number>(`http://localhost:8080/user/${userId}`,{withCredentials:true});
  }

  startChat(userId:number){
    this.getConversationId(userId).subscribe(data=>{
      this.router.navigate(['chat',data]);
    })
  }

}
