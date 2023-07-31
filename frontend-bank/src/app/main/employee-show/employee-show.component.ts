import { Component, OnInit } from '@angular/core';
import { AccountResponse } from 'src/app/model/account.interface';
import { UserResponse } from 'src/app/model/user.interface';
import { AccountService } from 'src/app/service/account.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-employee-show',
  templateUrl: './employee-show.component.html',
  styleUrls: ['./employee-show.component.scss'],
})
export class EmployeeShowComponent implements OnInit {
  public customerCheckingAccounts: UserResponse[] = [];
  public customerSavingsAccounts: UserResponse[] = [];

  public dataSourceCheck: UserResponse[] = [];
  public dataSourceSav: UserResponse[] = [];
  public dataHeaders: string[] = [
    'Name',
    'Email',
    'Account Number',
    'Routing Number',
    'Balance',
  ];

  constructor(
    private userService: UserService,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.accountService.fetchAllBankAccounts().subscribe((accounts) => {
      this.userService.fetchAllUsers().subscribe((users) => {
        accounts.forEach((account) => {
          users.forEach((user) => this.setBankAccountToCustomer(account, user));
        });
        this.dataSourceCheck = this.customerCheckingAccounts;
        this.dataSourceSav = this.customerSavingsAccounts;
      });
    });
  }

  setBankAccountToCustomer(account: AccountResponse, user: UserResponse) {
    if (account.userId === user.userId) {
      if (account.accountType === 'Checking') {
        user.checkingAccount = account;
        this.customerCheckingAccounts.push(user);
      } else if (account.accountType === 'Savings') {
        user.savingsAccount = account;
        this.customerSavingsAccounts.push(user);
      }
    }
  }
}
