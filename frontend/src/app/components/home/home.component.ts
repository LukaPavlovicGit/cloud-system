import { Component } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";
import {PermissionEnum} from "../../model/enums/PermissionEnum";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {


  protected readonly PermissionEnum = PermissionEnum;

  constructor(private authService: AuthService) {

  }

  hasPermission(permission: PermissionEnum){
    return this.authService.hasPermission(permission)
  }
}
