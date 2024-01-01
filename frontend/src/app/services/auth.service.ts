import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { environment } from '../../environments/environment'
import { TokenPayload } from '../model/TokenPayload'
import { Token } from '../model/Token'
import { jwtDecode } from 'jwt-decode'
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router'
import {BehaviorSubject, catchError, Observable, tap, throwError} from "rxjs";
import {PermissionEnum} from "../model/enums/PermissionEnum";


@Injectable({
  providedIn: 'root'
})
export class AuthService implements CanActivate {

  private readonly authUrl = environment.authUrl

  private loggedInBehavior = new BehaviorSubject(this.getLoginStatus());
  isLoggedIn = this.loggedInBehavior.asObservable();

  constructor(private httpClient: HttpClient, private router: Router) {

  }

  login(email: string, password: string){
    let params = {
      email: email,
      password: password,
    }
    this.httpClient.post<Token>(`${this.authUrl}/login`, params).subscribe(
      (data) => {
        this.setLoggedInBehavior(true)
        localStorage.setItem('jwt', data.token)
        this.router.navigate(['/home'])
      },
      (error) => {
        console.log(error);
      }
    )
  }

  logout(){
    localStorage.removeItem('jwt')
    this.setLoggedInBehavior(false)
    this.router.navigate(['/login'])
  }

  hasPermission(permission: PermissionEnum) {
    let decoded = this.decodeToken()
    return decoded.permissions.includes(permission.valueOf()) && !this.isTokenExpired()
  }

  getLoginStatus() {
    return !!localStorage.getItem('jwt') && !this.isTokenExpired()
  }

  decodeToken(){
    let token = localStorage.getItem('jwt')
    if(token) {
      return jwtDecode<TokenPayload>(token)
    }
    return {id: -1, sub: '', permissions: [], exp: -1}
  }

  private isTokenExpired(){
    let decoded = this.decodeToken()
    return (new Date().getTime() < decoded.exp)
  }

  setLoggedInBehavior(loggedIn: boolean){
    this.loggedInBehavior.next(loggedIn)
  }



  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    let routePermissions = route.data['permissions'];
    let decoded = this.decodeToken()

    for (let mustHavePermission of routePermissions) {
      if (!decoded.permissions.includes(mustHavePermission)) {
        return false;
      }
    }
    return true;
  }
}
