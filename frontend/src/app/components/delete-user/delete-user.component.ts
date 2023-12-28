import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";
import {User} from "../../model/User";

@Component({
  selector: 'app-delete-user',
  templateUrl: './delete-user.component.html',
  styleUrls: ['./delete-user.component.css']
})
export class DeleteUserComponent {
  deleteUserByIdForm: FormGroup
  deleteUserByEmailForm: FormGroup

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private userService: UserService
  ) {
    this.deleteUserByIdForm = this.formBuilder.group({
      id: ['', Validators.required],
    })
    this.deleteUserByEmailForm = this.formBuilder.group({
      email: ['', Validators.required],
    });
  }

  deleteUserById() {
    this.userService.deleteUserById(this.deleteUserByIdForm.get('id')?.value)
      .subscribe({
        next: (val) => this.deleteUserByIdForm.reset(),
        error: (err) => alert(err)
      })
  }

  deleteUserByEmail() {
    this.userService.deleteUserByEmail(this.deleteUserByIdForm.get('email')?.value).subscribe({
      next: (val) => this.deleteUserByEmailForm.reset(),
      error: (err) => alert(err)
    })
  }

}
