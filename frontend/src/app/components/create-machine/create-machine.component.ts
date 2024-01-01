import { Component } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MachineService} from "../../services/machine.service";
import {AuthService} from "../../services/auth.service";
import {PermissionEnum} from "../../model/enums/PermissionEnum";

@Component({
  selector: 'app-create-machine',
  templateUrl: './create-machine.component.html',
  styleUrls: ['./create-machine.component.css']
})
export class CreateMachineComponent {

  createMachineForm: FormGroup

  constructor(private formBuilder: FormBuilder, private machineService: MachineService) {
    this.createMachineForm = this.formBuilder.group({
      machineName: ['']
    })
  }

  machineCreate(){
    this.machineService.machineCreate(this.createMachineForm.get('machineName')?.value).subscribe({
      complete: () => this.createMachineForm.get('machineName')?.reset(),
      error: (err) => console.error(err)
    })
  }

}
