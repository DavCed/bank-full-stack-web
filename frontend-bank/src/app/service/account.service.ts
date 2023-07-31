import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import {
  Account,
  AccountResponse,
  Transaction,
} from '../model/account.interface';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  constructor(private http: HttpClient) {}

  saveBankAccount(account: Account): Observable<AccountResponse> {
    return this.http.post<AccountResponse>(
      `${environment.accountsApiUrl}/addBankAccount`,
      account
    );
  }

  fetchBankAccountsByUserId(
    userId: string | null
  ): Observable<AccountResponse[]> {
    return this.http.get<AccountResponse[]>(
      `${environment.accountsApiUrl}/getBankAccounts/${userId}`
    );
  }

  fetchAllBankAccounts(): Observable<AccountResponse[]> {
    return this.http.get<AccountResponse[]>(
      `${environment.accountsApiUrl}/getAllBankAccounts`
    );
  }

  updateBankAccount(transaction: Transaction): Observable<AccountResponse> {
    return this.http.put<AccountResponse>(
      `${environment.accountsApiUrl}/editBankAccount`,
      transaction
    );
  }
}
