import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Account, AccountResponse } from '../model/account.interface';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  constructor(private http: HttpClient) {}

  saveAccount(account: Account): Observable<AccountResponse> {
    return this.http.post<AccountResponse>(
      `${environment.accountsBackEndUrl}/addAccount`,
      account
    );
  }

  getAccountsByUserId(userId: number): Observable<AccountResponse[]> {
    return this.http.get<AccountResponse[]>(
      `${environment.accountsBackEndUrl}/getAccounts/${userId}`
    );
  }

  getAllAccounts(): Observable<AccountResponse[]> {
    return this.http.get<AccountResponse[]>(
      `${environment.accountsBackEndUrl}/getAllAccounts`
    );
  }

  updateAccount(details: string[]): Observable<AccountResponse> {
    return this.http.put<AccountResponse>(
      `${environment.accountsBackEndUrl}/editAccount`,
      details
    );
  }
}
