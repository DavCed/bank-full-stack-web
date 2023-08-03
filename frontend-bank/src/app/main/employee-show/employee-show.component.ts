import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
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
  public actionForm: FormGroup;

  public customerCheckingAccounts: UserResponse[] = [];
  public customerSavingsAccounts: UserResponse[] = [];

  public dataSourceApprovedCheckingAccounts: UserResponse[] = [];
  public dataSourceApprovedSavingsAccounts: UserResponse[] = [];
  public dataSourcePendingCheckingAccounts: UserResponse[] = [];
  public dataSourcePendingSavingsAccounts: UserResponse[] = [];
  public dataHeaders1: string[] = [
    'Name',
    'Account Number',
    'Routing Number',
    'Balance',
  ];
  public dataHeaders2: string[] = [
    'Name',
    'Account Number',
    'Routing Number',
    'Balance',
    'Actions',
  ];

  constructor(
    private userService: UserService,
    private accountService: AccountService
  ) {
    this.actionForm = this.generateActionForm();
  }

  ngOnInit(): void {
    this.accountService.fetchAllBankAccounts().subscribe((accounts) => {
      this.userService.fetchAllUsers().subscribe((users) => {
        accounts.forEach((account) => {
          users.forEach((user) => this.setBankAccountToCustomer(account, user));
        });
        this.dataSourceApprovedCheckingAccounts = this.setApprovedBankAccounts(
          this.customerCheckingAccounts,
          'Checking'
        );

        this.dataSourceApprovedSavingsAccounts = this.setApprovedBankAccounts(
          this.customerSavingsAccounts,
          'Savings'
        );
        this.dataSourcePendingCheckingAccounts = this.setPendingBankAccounts(
          this.customerCheckingAccounts,
          'Checking'
        );
        this.dataSourcePendingSavingsAccounts = this.setPendingBankAccounts(
          this.customerSavingsAccounts,
          'Savings'
        );
      });
    });
  }

  generateActionForm(): FormGroup {
    return new FormGroup({
      accountId: new FormControl(null),
      userId: new FormControl(null),
      balance: new FormControl(null),
      accountNumber: new FormControl(null),
      routingNumber: new FormControl(null),
      accountType: new FormControl(null),
      accountStatus: new FormControl(null),
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

  setApprovedBankAccounts(accounts: UserResponse[], accountType: string) {
    return accountType === 'Checking'
      ? accounts.filter(
          (customer) => customer.checkingAccount?.accountStatus === 'Approved'
        )
      : accounts.filter(
          (customer) => customer.savingsAccount?.accountStatus === 'Approved'
        );
  }

  setPendingBankAccounts(accounts: UserResponse[], accountType: string) {
    return accountType === 'Checking'
      ? accounts.filter(
          (customer) => customer.checkingAccount?.accountStatus === 'Pending'
        )
      : accounts.filter(
          (customer) => customer.savingsAccount?.accountStatus === 'Pending'
        );
  }

  approveBankAccount(customer: UserResponse, account: AccountResponse) {
    this.actionForm.controls['accountNumber'].setValue(account.accountNumber);
    this.actionForm.controls['accountStatus'].setValue('A');
    this.actionForm.controls['userId'].setValue(customer.userId);
    this.accountService
      .updateBankAccountStatus(this.actionForm.value)
      .subscribe(
        (account) => this.setBankAccountToCustomer(account, customer),
        (errorResponse) => console.log(errorResponse),
        () => {
          setTimeout(() => window.location.reload(), 2000);
        }
      );
  }

  denyBankAccount(customer: UserResponse, account: AccountResponse) {
    this.actionForm.controls['accountNumber'].setValue(account.accountNumber);
    this.actionForm.controls['accountStatus'].setValue('D');
    this.actionForm.controls['userId'].setValue(customer.userId);
    this.accountService
      .updateBankAccountStatus(this.actionForm.value)
      .subscribe(
        () => {},
        (errorResponse) => console.log(errorResponse),
        () => {
          setTimeout(() => window.location.reload(), 2000);
        }
      );
  }
}
