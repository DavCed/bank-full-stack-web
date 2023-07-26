export interface Account {
  accountId?: number;
  userId: number;
  balance: number;
  accountNumber: number;
  routingNumber?: number;
  accountType: string;
}

export interface AccountResponse {
  accountNumber: number;
  accountType: string;
  routingNumber: number;
  balance: number;
  message: string;
  userId: number;
}

export interface Transaction {
  amount: number;
  transaction: string;
  account: string;
}
