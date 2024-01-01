import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";
import {AuthService} from "./auth.service";
import {MachineStatusEnum} from "../model/enums/MachineStatusEnum";
import {Machine} from "../model/Machine";
import {catchError, throwError} from "rxjs";
import {MachineAction} from "../model/enums/MachineAction";
import {MachineScheduleError} from "../model/MachineScheduleError";

@Injectable({
  providedIn: 'root'
})
export class MachineService {

  private readonly machinesUrl = environment.machinesUrl
  private headers = new HttpHeaders({
    Authorization: `Bearer ${localStorage.getItem('jwt')}`,
    'Content-Type': 'application/json',
    Accept: 'application/json'
  })

  constructor(private httpClient: HttpClient, private router: Router, private authService: AuthService) {

  }

  searchMachines(
    machineName: string | null,
    machineStatuses: MachineStatusEnum[] | null,
    dateFrom: Date | null,
    dateTo: Date | null
  ){

    let queryParams = `userId=${this.getUserId()}`

    if(machineName != null){
      queryParams += `&machineName=${machineName}`
    }
    if(machineStatuses != null){
      queryParams += `&statuses=${machineStatuses.toString()}`
    }
    if(dateFrom != null && dateTo != null){
      queryParams += `&dateFrom=${dateFrom.getMilliseconds()}`
      queryParams += `&dateTo=${dateTo.getMilliseconds()}`
    }

    return this.httpClient.get<Machine[]>(`${this.machinesUrl}?${queryParams}`,
      {
        headers: this.headers
      }).pipe(
        catchError(err => {
          return throwError(() => new Error(err.error.message));
      })
    )
  }

  getMachineById(id: string){
    return this.httpClient.get<Machine>(`${this.machinesUrl}/${id}`,
      {
        headers: this.headers
      }).pipe(
      catchError(err => {
        return throwError(() => new Error(err.error.message));
      })
    )
  }

  machineCreate(name: string){
    return this.httpClient.post<Machine>(`${this.machinesUrl}`,
      {
        userId: this.authService.decodeToken().id,
        name: name
      },
      {
        headers: this.headers
      }).pipe(
        catchError(err => {
          return throwError(() => new Error(err.error.message));
        })
      )
  }

  machineAction(action: MachineAction, id: number){
    return this.httpClient.put<any>(`${this.machinesUrl}/machine-${action.toLowerCase()}/${id}`,
      { },
      { headers: this.headers }
    ).pipe(
      catchError(err => {
        return throwError(() => new Error(err.error.message));
      })
    )
  }

  scheduleMachineAction(action: MachineAction, id: string, dateTime: Date){
    return this.httpClient.put<any>(`${this.machinesUrl}/schedule-machine-${action.toLowerCase()}`,
      {
        userId: this.getUserId(),
        machineId: id,
        scheduleDate: dateTime.getTime()
      },
      { headers: this.headers }
    ).pipe(
      catchError(err => {
        return throwError(() => new Error(err.error.message));
      })
    )
  }

  getMachineScheduleErrors(){
    return this.httpClient.get<MachineScheduleError[]>(`${this.machinesUrl}/machine-schedule-action-errors/${this.getUserId()}`,
      { headers: this.headers }
    ).pipe(
      catchError(err => {
        return throwError(() => new Error(err.error.message));
      })
    )
  }

  private getUserId(){
    return this.authService.decodeToken().id
  }

}
