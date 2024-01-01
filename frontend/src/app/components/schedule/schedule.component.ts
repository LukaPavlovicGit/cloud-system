import { Component } from '@angular/core';
import {getMachineAction, MachineAction} from "../../model/enums/MachineAction";
import {AuthService} from "../../services/auth.service";
import {MachineService} from "../../services/machine.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-schedule',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.css']
})
export class ScheduleComponent {

  machineAction = MachineAction.NONE
  machineId = ''

  constructor(private authService: AuthService, private machineService: MachineService, private route: ActivatedRoute, private router: Router) {

  }
  ngOnInit(){
    this.machineAction = getMachineAction(this.route.snapshot.paramMap.get('action')!)
    this.machineId = this.route.snapshot.paramMap.get('id')!
  }

  scheduleMachineAction(){
    let scheduleDate = document.getElementById('date-schedule') as HTMLInputElement;
    let scheduleTime = document.getElementById('time-schedule') as HTMLInputElement;

    if(!isNaN(scheduleDate.valueAsNumber) && !isNaN(scheduleTime.valueAsNumber)) {
      let date = new Date(scheduleDate.value + " " + scheduleTime.value);

      this.machineService.scheduleMachineAction(this.machineAction, this.machineId, date).subscribe({
        complete: () => this.router.navigate(['single-machine', this.machineId]),
        error: (err) => console.error(err),
        next: (value) => { }
      })
    }


  }

}
