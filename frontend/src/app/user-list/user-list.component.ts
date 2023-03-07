import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {
  users: User[];

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() :void {
    this.getUsers();
  }

  private getUsers(){
    this.userService.getUsersList().subscribe(data => {
      this.users = data;
    })
  }

}
