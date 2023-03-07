import { Component, OnInit } from '@angular/core';
import { AuthService } from '../Services/AuthService';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  username: string;
  password: string;
  errorMessage="Inwalid Credentials";
  successMessage: string;
  invalidLogin = false;
  loginSuccess = false;

  constructor(private authService: AuthService) { }

  ngOnInit() {
  }

  handleLogin() {
    this.authService.login(this.username, this.password).subscribe((result) => {
      this.invalidLogin = false;
      this.loginSuccess = true;
      this.successMessage = 'Login Successful';
    }, () => {
      this.invalidLogin = true;
      this.loginSuccess = false;
    });
  }

}
