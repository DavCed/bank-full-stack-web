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

  saveAccount(account: Account): Observable<AccountResponse> {
    return this.http.post<AccountResponse>(
      `${environment.accountsApiUrl}/addAccount`,
      account
    );
  }

  getAccountsByUserId(userId: number): Observable<AccountResponse[]> {
    return this.http.get<AccountResponse[]>(
      `${environment.accountsApiUrl}/getAccounts/${userId}`
    );
  }

  getAllAccounts(): Observable<AccountResponse[]> {
    return this.http.get<AccountResponse[]>(
      `${environment.accountsApiUrl}/getAllAccounts`
    );
  }

  updateAccount(transactionObj: Transaction): Observable<AccountResponse> {
    return this.http.put<AccountResponse>(
      `${environment.accountsApiUrl}/editAccount`,
      transactionObj
    );
  }
}
