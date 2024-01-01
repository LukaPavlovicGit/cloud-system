import {MachineStatusEnum} from "./enums/MachineStatusEnum";

export interface Machine {
  id: number,
  userId: number,
  status: MachineStatusEnum,
  createdAt: Date,
  active: boolean,
  name: string
}
