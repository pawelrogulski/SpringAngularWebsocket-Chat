import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './user';
import { UserRegister } from './user-register';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  getUsersList(): Observable<User[]>{
    return this.httpClient.get<User[]>('http://localhost:8080/api/app/add_friend',{withCredentials:true});
  }

  register(userRegister: UserRegister): Observable<Object>{
    return this.httpClient.post('http://localhost:8080/api/app/register',userRegister);
  }
}
