import { Injectable } from '@angular/core'
import { HttpClient, HttpHeaders } from "@angular/common/http"
import { Router } from "@angular/router"
import { environment } from "../../environments/environment"
import {catchError, Observable, tap, throwError} from 'rxjs'
import { User } from "../model/User"

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly usersUrl = environment.usersUrl
  private headers = new HttpHeaders({
    Authorization: `Bearer ${localStorage.getItem('jwt')}`,
    'Content-Type': 'application/json',
    Accept: 'application/json'
  })

  constructor(private httpClient: HttpClient, private router: Router) {

  }

  getUsers() {
    return this.httpClient.get<User[]>(`${this.usersUrl}`,
      {
        headers: this.headers
      }).pipe(
      catchError(err => {
        return throwError(() => new Error(err.error.message));
      })
    )
  }

  getUser(id: number) {
    return this.httpClient.get<User>(`${this.usersUrl}/${id}`,
      {
      headers: this.headers
    }).pipe(
      catchError(err => {
        return throwError(() => new Error(err.error.message));
      })
    )
  }

  createUser(firstName: string, lastName: string, email: string, password: string, permissions: string[]): Observable<User> {
    return this.httpClient.post<User>(this.usersUrl,
      {
        firstName: firstName,
        lastName: lastName,
        email: email,
        password: password,
        permissions: permissions
      },
      {
        headers: this.headers
      }).pipe(
      catchError(err => {
        return throwError(() => new Error(err.error.message));
      })
    )
  }

  updateUser(id: number, firstName: string, lastName: string, email: string, password: string, permissions: string[]){
    return this.httpClient.put<User>(this.usersUrl,
      {
        id: id,
        firstName: firstName,
        lastName: lastName,
        email: email,
        password: password,
        permissions: permissions
      },
      {
        headers: this.headers
      }).pipe(
      catchError(err => {
        return throwError(() => new Error(err.error.message));
      })
    )
  }

  deleteUserById(id: number){
    return this.httpClient.delete<any>(`${this.usersUrl}/id/${id}`,
      {
        headers: this.headers
      }).pipe(
      catchError(err => {
        return throwError(() => new Error(err.error.message));
      })
    )
  }

  deleteUserByEmail(email: string){
    return this.httpClient.delete<any>(`${this.usersUrl}/email/${email}`,
      {
        headers: this.headers
      }).pipe(
      catchError(err => {
        return throwError(() => new Error(err.error.message));
      })
    )
  }
}
