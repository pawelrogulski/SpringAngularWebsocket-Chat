import { TokenStorageService } from './Services/token-storage.service';
import {Component, HostListener} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  constructor(private tokenStorage: TokenStorageService) { }

  signOut(){
    this.tokenStorage.signOut();
    window.location.reload();
  }
}
