import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {Subscription} from "rxjs";
import {PermissionEnum} from "../../model/enums/PermissionEnum";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private router: Router, private authService: AuthService) {

  }

  ngOnInit(): void {
    this.authService.isLoggedIn.subscribe((loggedIn) => {
      if(!loggedIn){
        this.router.navigate(['/login']).then(r => {  })
      }
    })
  }

  logout(){
    this.authService.logout()
  }

  isLoggedIn(){
    return this.authService.isLoggedIn
  }
}
