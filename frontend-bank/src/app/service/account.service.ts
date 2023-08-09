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

  public saveBankAccount$ = (account: Account) =>
    <Observable<AccountResponse>>(
      this.http.post<AccountResponse>(
        `${environment.accountsApiUrl}/addBankAccount`,
        account
      )
    );

  public fetchBankAccountsByUserId$ = (userId: string | null) =>
    <Observable<AccountResponse[]>>(
      this.http.get<AccountResponse[]>(
        `${environment.accountsApiUrl}/getBankAccounts/${userId}`
      )
    );

  public fetchAllBankAccounts$ = () =>
    <Observable<AccountResponse[]>>(
      this.http.get<AccountResponse[]>(
        `${environment.accountsApiUrl}/getAllBankAccounts`
      )
    );

  public updateBankAccountBalance$ = (transaction: Transaction) =>
    <Observable<AccountResponse>>(
      this.http.put<AccountResponse>(
        `${environment.accountsApiUrl}/editBankAccountBalance`,
        transaction
      )
    );

  public updateBankAccountStatus$ = (account: Account) =>
    <Observable<AccountResponse>>(
      this.http.put<AccountResponse>(
        `${environment.accountsApiUrl}/editBankAccountStatus`,
        account
      )
    );
}
