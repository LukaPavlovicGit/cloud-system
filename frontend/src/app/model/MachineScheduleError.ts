import {MachineAction} from "./enums/MachineAction";

export interface MachineScheduleError {
  userId: number,
  message: string,
  action: MachineAction,
  machineId: number,
  dateError: Date
}
