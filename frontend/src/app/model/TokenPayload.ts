export interface TokenPayload {
  id: number,
  sub: string,
  permissions: string[],
  exp: number
}
