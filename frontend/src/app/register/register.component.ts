import { Component, OnInit } from '@angular/core';
import { UserRegister } from '../user-register';
import { UserService } from '../user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  user: UserRegister = new UserRegister();
  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {
  }

  register(){
    this.userService.register(this.user).subscribe(data => {
      console.log(data);
      this.goToLoginPage();
    },
    error => console.log(error));
  }

  goToLoginPage(){
    this.router.navigate(['/login']);
  }
  onSubmit(){
    this.register();
  }

}
