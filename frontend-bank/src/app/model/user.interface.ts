import { AccountResponse } from './account.interface';

export interface User {
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phoneNumber: string;
  userType: string;
}

export interface Credentials {
  email: string;
  password: string;
}

export interface UserResponse {
  userId: number;
  name: string;
  email: string;
  message: string;
  userType: string;
  checkingAccount?: AccountResponse;
  savingsAccount?: AccountResponse;
}
