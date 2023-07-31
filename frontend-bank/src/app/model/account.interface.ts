export interface Account {
  accountId: number;
  userId: number;
  balance: number;
  accountNumber: number;
  routingNumber: number;
  accountType: string;
}

export interface AccountResponse {
  userId: number;
  balance: number;
  accountNumber: number;
  routingNumber: number;
  accountType: string;
  message: string;
}

export interface Transaction {
  amount: number;
  transactionType: string;
  accountNumber: string;
}
