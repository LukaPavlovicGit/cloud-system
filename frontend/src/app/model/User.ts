import {PermissionEnum} from "./enums/PermissionEnum";

export interface User {
  id: number
  firstName: string
  lastName: string
  email: string
  permissions: [PermissionEnum]
}
