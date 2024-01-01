import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DeleteUserComponent} from "./components/delete-user/delete-user.component";
import {UpdateUserComponent} from "./components/update-user/update-user.component";
import {CreateUserComponent} from "./components/create-user/create-user.component";
import {ReadUserComponent} from "./components/read-user/read-user.component";
import {LoginUserComponent} from "./components/login-user/login-user.component";
import {HomeComponent} from "./components/home/home.component";
import {AuthService} from "./services/auth.service";
import {PermissionEnum} from "./model/enums/PermissionEnum";
import {ReadMachinesComponent} from "./components/read-machines/read-machines.component";
import {SingleMachineComponent} from "./components/single-machine/single-machine.component";
import {ScheduleComponent} from "./components/schedule/schedule.component";
import {CreateMachineComponent} from "./components/create-machine/create-machine.component";
import {ReadErrorsComponent} from "./components/read-errors/read-errors.component";

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
  },
  {
    path: 'search-machines',
    component: ReadMachinesComponent,
    canActivate: [AuthService],
    data: {permissions: [PermissionEnum.CAN_SEARCH_VACUUM]}
  },
  {
    path: 'single-machine/:id',
    component: SingleMachineComponent,
  },
  {
    path: 'schedule/:action/:id',
    component: ScheduleComponent,
  },
  {
    path: 'machine-create',
    component: CreateMachineComponent,
    canActivate: [AuthService],
    data: {permissions: [PermissionEnum.CAN_CREATE_VACUUM]}
  },
  {
    path: 'machine-schedule-action-errors',
    component: ReadErrorsComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
