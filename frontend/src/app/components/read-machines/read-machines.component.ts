import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {MachineService} from "../../services/machine.service";
import {Machine} from "../../model/Machine";
import {MachineStatusEnum} from "../../model/enums/MachineStatusEnum";
import {FormArray, FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MachineAction} from "../../model/enums/MachineAction";

@Component({
  selector: 'app-read-machines',
  templateUrl: './read-machines.component.html',
  styleUrls: ['./read-machines.component.css']
})
export class ReadMachinesComponent implements AfterViewInit {

  displayedColumns: string[] = ['id', 'userId', 'name', 'status', 'created_at', 'active'];

  searchMachinesForm: FormGroup

  machines: Machine[] = [
      {
        id: -1,
        userId: -1,
        status: MachineStatusEnum.NONE,
        createdAt: new Date(),
        active: false,
        name: ""
      }
    ]

  dataSource = new MatTableDataSource<Machine>(this.machines);
  // @ts-ignore
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private formBuilder: FormBuilder, private machineService: MachineService) {
    this.searchMachinesForm = this.formBuilder.group({
      machineName: new FormControl(''),
      runningStatus: new FormControl(null), // boolean
      stoppedStatus: new FormControl(null), // boolean
      dischargingStatus: new FormControl(null), // boolean
      dateFrom: new FormControl(null),
      dateTo: new FormControl(null),
    })

  }

  private addStatusCheckboxes() {
    const statusesArray = [MachineStatusEnum.STOPPED, MachineStatusEnum.RUNNING, MachineStatusEnum.DISCHARGING];

  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  ngOnInit(){
   this.searchMachines()
  }

  searchMachines(){

    let machineName = this.searchMachinesForm.get('machineName')?.value
    let dateFrom = this.searchMachinesForm.get('dateFrom')?.value
    let dateTo = this.searchMachinesForm.get('dateTo')?.value
    let machineStatus: MachineStatusEnum[] = []

    if(this.searchMachinesForm.get('runningStatus')?.value != null &&
        this.searchMachinesForm.get('runningStatus')?.value) {
      machineStatus.push(MachineStatusEnum.RUNNING)
    }
    if(this.searchMachinesForm.get('stoppedStatus')?.value != null &&
        this.searchMachinesForm.get('stoppedStatus')?.value){
      machineStatus.push(MachineStatusEnum.STOPPED)
    }
    if(this.searchMachinesForm.get('dischargingStatus')?.value != null &&
        this.searchMachinesForm.get('dischargingStatus')?.value){
      machineStatus.push(MachineStatusEnum.DISCHARGING)
    }

    console.log("asdasd" + machineStatus.toString())


    this.machineService.searchMachines(machineName, machineStatus, dateFrom, dateTo).subscribe({
      next: (machines) => {
        this.machines = machines
        this.dataSource = new MatTableDataSource<Machine>(machines);
      },
      error: (err) => console.error(err)
    })
  }
}
