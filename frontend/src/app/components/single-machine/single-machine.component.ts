import { Component } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {MachineService} from "../../services/machine.service";
import {Machine} from "../../model/Machine";
import {MachineStatusEnum} from "../../model/enums/MachineStatusEnum";
import {ActivatedRoute} from "@angular/router";
import {MachineAction} from "../../model/enums/MachineAction";
import {PermissionEnum} from "../../model/enums/PermissionEnum";

@Component({
  selector: 'app-single-machine',
  templateUrl: './single-machine.component.html',
  styleUrls: ['./single-machine.component.css']
})
export class SingleMachineComponent {

  machine: Machine = {id: -1, userId: -1, status: MachineStatusEnum.NONE, createdAt: new Date(), active: false, name: ''}
  protected readonly MachineStatusEnum = MachineStatusEnum;
  protected readonly MachineAction = MachineAction;

  constructor(private authService: AuthService, private machineService: MachineService, private route: ActivatedRoute) {

  }

  ngOnInit(){
    this.getMachine(this.route.snapshot.paramMap.get('id')!)
  }

  getMachine(id: string){
    this.machineService.getMachineById(id).subscribe({
        next: (machine) => this.machine = machine,
        error: (err) => console.error(err)
      })
  }

  machineAction(action: MachineAction, id:number){
    this.machineService.machineAction(action, id).subscribe({
      error: (err) => console.error(err)
    })
  }

  hasPermission(permission: PermissionEnum){
    return this.authService.hasPermission(permission)
  }

  protected readonly PermissionEnum = PermissionEnum;
}
