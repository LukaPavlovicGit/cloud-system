import {Component} from '@angular/core';
import {MachineService} from "../../services/machine.service";
import {MachineScheduleError} from "../../model/MachineScheduleError";
import {MachineAction} from "../../model/enums/MachineAction";

@Component({
  selector: 'app-read-errors',
  templateUrl: './read-errors.component.html',
  styleUrls: ['./read-errors.component.css']
})
export class ReadErrorsComponent {

  displayedColumns: string[] = ['userId', 'machineId', 'machineAction', 'message', 'dateError'];
  machineScheduleErrors: MachineScheduleError[] = [
    {
      userId: -1,
      machineId: -1,
      action: MachineAction.NONE,
      message: '',
      dateError: new Date(),
    }
  ]

  constructor(private machineService: MachineService) {

  }

  ngOnInit(){
    this.getMachineScheduleErrors()
  }

  getMachineScheduleErrors(){
    this.machineService.getMachineScheduleErrors().subscribe({
      next: (value) => this.machineScheduleErrors = value,
      error: (err) => console.log(err)
    })
  }
}
