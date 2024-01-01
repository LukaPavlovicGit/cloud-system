import {Component} from '@angular/core';
import {User} from "../../model/User";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";
import {PermissionEnum} from "../../model/enums/PermissionEnum";

@Component({
  selector: 'app-read-user',
  templateUrl: './read-user.component.html',
  styleUrls: ['./read-user.component.css']
})
export class ReadUserComponent {

  users: User[] = [
    {
      id: -1,
      firstName: '',
      lastName: '',
      email: '',
      permissions: [PermissionEnum.NONE],
    },
  ]

  constructor(private authService: AuthService, private userService: UserService) {

  }

  ngOnInit(): void {
    this.getUsers()
  }

  getUsers(){
    this.userService.getUsers().subscribe({
      next: (users) => this.users = users,
      error: (err) => console.error(err),
    })
  }

  hasPermission(permission: PermissionEnum){
    return this.authService.hasPermission(permission)
  }

  deleteUser(id: number){
    this.userService.deleteUserById(id).subscribe({
      next: (user) => alert('user deleted'),
      error: (err) => console.error(err)
    })
  }

  protected readonly PermissionEnum = PermissionEnum;
}
