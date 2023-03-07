import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  public username: string;
  public password: string;

  constructor(private http: HttpClient) {
    const token = localStorage.getItem('token');
  }

  login(username: string, password: string) {
    return this.http.get('http://localhost:8080/login',
      { headers: { authorization: this.createBasicAuthToken(username, password) } }).pipe(map((res) => {
        this.username = username;
        this.password = password;
        this.registerSuccessfulLogin(username, password);
      }));
  }

  createBasicAuthToken(username: string, password: string) {
    return 'Basic ' + window.btoa(username + ":" + password);
  }

  registerSuccessfulLogin(username, password) {
    // save the username to session
  }

}
