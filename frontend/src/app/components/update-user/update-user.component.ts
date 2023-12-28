import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";
import {User} from "../../model/User";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css']
})
export class UpdateUserComponent {
  updateUserForm: FormGroup;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute
  ) {

    this.updateUserForm = this.formBuilder.group({
      id: [`${this.route.snapshot.paramMap.get('id')}`, Validators.required],
      firstName: [],
      lastName: [],
      email: ['', Validators.email],
      password: [],
      can_read_users: [],
      can_create_users: [],
      can_update_users: [],
      can_delete_users: [],
    })
  }

  updateUser() {
    let id = this.updateUserForm.get('id')?.value
    let firstName = this.updateUserForm.get('firstName')?.value
    let lastName = this.updateUserForm.get('lastName')?.value
    let email = this.updateUserForm.get('email')?.value
    let password = this.updateUserForm.get('password')?.value
    let permissions = []

    if (this.updateUserForm.get('can_read_users')?.value == true)
      permissions.push('CAN_READ_USERS');
    if (this.updateUserForm.get('can_create_users')?.value == true)
      permissions.push('CAN_CREATE_USERS');
    if (this.updateUserForm.get('can_update_users')?.value == true)
      permissions.push('CAN_UPDATE_USERS');
    if (this.updateUserForm.get('can_delete_users')?.value == true)
      permissions.push('CAN_DELETE_USERS');

    this.userService.updateUser(id, firstName, lastName, email, password, permissions)
      .subscribe({
        next: (val) => {
          this.updateUserForm.reset()
        },
        error: (err) => alert(err)
      })
  }

}
