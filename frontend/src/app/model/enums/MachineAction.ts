export enum MachineAction {
  START = "START",
  STOP = "STOP",
  DISCHARGE = "DISCHARGE",
  DESTROY = "DESTROY",

  NONE = "NONE"
}

export function getMachineAction(action: string): MachineAction {
  for(let machineAction of Object.values(MachineAction)){
    if(machineAction.valueOf() == action){
      return action as MachineAction;
    }
  }
  return MachineAction.NONE;
}
