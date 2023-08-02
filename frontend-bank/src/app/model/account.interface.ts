export interface Account {
  accountId: number;
  userId: number;
  balance: number;
  accountNumber: number;
  routingNumber: number;
  accountType: string;
  accountStatus: string;
}

export interface AccountResponse {
  userId: number;
  balance: number;
  accountNumber: number;
  routingNumber: number;
  accountType: string;
  accountStatus: string;
  message: string;
}

export interface Transaction {
  amount: number;
  transactionType: string;
  accountNumber: string;
}
