import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent {

  createUserForm: FormGroup

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private formBuilder: FormBuilder
  ) {
    this.createUserForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      can_read_users: [],
      can_create_users: [],
      can_update_users: [],
      can_delete_users: [],
    });
  }

  createUser() {
    let firstName = this.createUserForm.get('firstName')?.value
    let lastName = this.createUserForm.get('lastName')?.value
    let email = this.createUserForm.get('email')?.value
    let password = this.createUserForm.get('password')?.value
    let permissions = []

    if (this.createUserForm.get('can_read_users')?.value == true)
      permissions.push('CAN_READ_USERS');
    if (this.createUserForm.get('can_create_users')?.value == true)
      permissions.push('CAN_CREATE_USERS');
    if (this.createUserForm.get('can_update_users')?.value == true)
      permissions.push('CAN_UPDATE_USERS');
    if (this.createUserForm.get('can_delete_users')?.value == true)
      permissions.push('CAN_DELETE_USERS');


    this.userService.createUser(firstName, lastName, email, password, permissions).subscribe({
      next: (user) => this.createUserForm.reset(),
      error: (err) => console.error(err),
    })
  }

}
