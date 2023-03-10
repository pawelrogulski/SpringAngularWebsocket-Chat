import { AuthService } from './Services/AuthService';
import { ChatComponent } from './chat/chat.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { UserListComponent } from './user-list/user-list.component';
import { RouteGuard } from './Services/RouteGuard';
import { BrowserModule } from '@angular/platform-browser';

const routes: Routes =[
  {path: 'users', component: UserListComponent, canActivate:[RouteGuard] },
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: 'chat/:id',component:ChatComponent, canActivate:[RouteGuard] },
  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path: '**', redirectTo: 'users'}
];

@NgModule({
  declarations: [],
  imports: [BrowserModule, RouterModule.forRoot(routes)],
  providers: [AuthService, RouteGuard, LoginComponent],
  exports: [RouterModule]
})
export class AppRoutingModule { }
