import { Component, OnInit } from '@angular/core';
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
    this.fetchAllAccounts();
  }

  fetchAllAccounts() {
    this.accountService.getAllAccounts().subscribe((accounts) => {
      this.userService.getAllUsers().subscribe((users) => {
        accounts.map((account) => {
          users.map((user) => {
            if (account.userId === user.userId) {
              switch (account.accountType) {
                case 'Checking':
                  user.checkingAccount = account;
                  this.customerCheckingAccounts.push(user);
                  break;
                case 'Savings':
                  user.savingsAccount = account;
                  this.customerSavingsAccounts.push(user);
                  break;
              }
            }
          });
        });
        this.dataSourceCheck = this.customerCheckingAccounts;
        this.dataSourceSav = this.customerSavingsAccounts;
      });
    });
  }
}
