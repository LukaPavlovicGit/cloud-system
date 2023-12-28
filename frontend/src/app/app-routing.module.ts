import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DeleteUserComponent} from "./components/delete-user/delete-user.component";
import {UpdateUserComponent} from "./components/update-user/update-user.component";
import {CreateUserComponent} from "./components/create-user/create-user.component";
import {ReadUserComponent} from "./components/read-user/read-user.component";
import {LoginUserComponent} from "./components/login-user/login-user.component";
import {HomeComponent} from "./components/home/home.component";
import {AuthService} from "./services/auth.service";
import {PermissionEnum} from "./model/roleEnum/PermissionEnum";

const routes: Routes = [
  {
    path: 'login',
    component: LoginUserComponent,
  },
  {
    path: 'home',
    component: HomeComponent,

  },
  {
    path: 'read-users',
    component: ReadUserComponent,
    canActivate: [AuthService],
    data: {permissions: [PermissionEnum.CAN_READ_USERS]}
  },
  {
    path: 'create-user',
    component: CreateUserComponent,
    canActivate: [AuthService],
    data: {permissions: [PermissionEnum.CAN_CREATE_USERS]}
  },
  {
    path: 'update-user/:id',
    component: UpdateUserComponent,
    canActivate: [AuthService],
    data: {permissions: [PermissionEnum.CAN_UPDATE_USERS]}
  },
  {
    path: 'delete-user',
    component: DeleteUserComponent,
    canActivate: [AuthService],
    data: {permissions: [PermissionEnum.CAN_DELETE_USERS]}
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
