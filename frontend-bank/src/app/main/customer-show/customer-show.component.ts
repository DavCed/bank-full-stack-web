import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AccountResponse } from 'src/app/model/account.interface';
import { AccountService } from 'src/app/service/account.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-customer-show',
  templateUrl: './customer-show.component.html',
  styleUrls: ['./customer-show.component.scss'],
})
export class CustomerShowComponent implements OnInit {
  private customerLoggedIn: any;
  public showTransactionForm: boolean = false;
  public showRegisterForm: boolean = false;
  private customerId: string | null = '';

  public accountForm: FormGroup;
  public accountMessage: string = '';
  public registerAccountTypes = [
    { value: 'C', viewValue: 'Checking' },
    { value: 'S', viewValue: 'Savings' },
  ];

  public transactionForm: FormGroup;
  public transactionMessage: string = '';
  public transactionTypes = [
    { value: 'W', viewValue: 'Withdraw' },
    { value: 'D', viewValue: 'Deposit' },
  ];
  public transactionAccountTypes = [
    { value: 'C', viewValue: 'Checking' },
    { value: 'S', viewValue: 'Savings' },
  ];

  public dataSourceApprovedAccounts: AccountResponse[] = [];
  public dataSourcePendingAccounts: AccountResponse[] = [];
  public dataHeaders1: string[] = [
    'Account Type',
    'Account Number',
    'Routing Number',
    'Balance',
  ];
  public dataHeaders2: string[] = [
    'Account Type',
    'Account Number',
    'Routing Number',
    'Balance',
    'Account Status',
  ];

  constructor(
    private accountService: AccountService,
    private activeRoute: ActivatedRoute,
    private userService: UserService
  ) {
    this.activeRoute.paramMap.subscribe(
      (param) => (this.customerId = param.get('id'))
    );
    this.accountForm = this.generateAccountForm();
    this.transactionForm = this.generateTransactionForm();
  }

  ngOnInit(): void {
    this.userService.fetchUserById(this.customerId).subscribe((customer) => {
      this.customerLoggedIn = customer;
      this.accountService
        .fetchBankAccountsByUserId(this.customerId)
        .subscribe((accounts) => {
          accounts.forEach((account) => this.setBankAccount(account));
          this.dataSourceApprovedAccounts = accounts.filter(
            (account) => account.accountStatus === 'Approved'
          );
          this.dataSourcePendingAccounts = accounts.filter(
            (account) => account.accountStatus === 'Pending'
          );
          this.checkBankAccounts(
            this.dataSourceApprovedAccounts,
            this.dataSourcePendingAccounts
          );
        });
    });
  }

  generateAccountForm() {
    return new FormGroup({
      accountId: new FormControl(null),
      userId: new FormControl(this.customerId),
      balance: new FormControl(null, Validators.required),
      accountNumber: new FormControl(null),
      routingNumber: new FormControl(null),
      accountType: new FormControl(null, Validators.required),
      accountStatus: new FormControl(null),
    });
  }

  generateTransactionForm() {
    return new FormGroup({
      amount: new FormControl(null, Validators.required),
      transactionType: new FormControl(null, Validators.required),
      accountNumber: new FormControl(null, Validators.required),
    });
  }

  setBankAccount(account: AccountResponse) {
    if (account.accountStatus === 'Approved') {
      if (account.accountType === 'Checking')
        this.customerLoggedIn.checkingAccount = account;
      else if (account.accountType === 'Savings')
        this.customerLoggedIn.savingsAccount = account;
    }
  }

  checkBankAccounts(
    approvedAccounts: AccountResponse[],
    pendingAccounts: AccountResponse[]
  ) {
    if (approvedAccounts.length > 0) {
      this.showTransactionForm = true;
      if (approvedAccounts.length === 1)
        this.transactionAccountTypes = this.setAccountTypes(
          approvedAccounts,
          'Transaction'
        );
    }
    if (
      (pendingAccounts.length === 0 || pendingAccounts.length === 1) &&
      approvedAccounts.length < 2
    ) {
      if (pendingAccounts.length === 0 && approvedAccounts.length === 1) {
        this.showRegisterForm = true;
        this.registerAccountTypes = this.setAccountTypes(
          approvedAccounts,
          'Register'
        );
      } else if (
        pendingAccounts.length === 1 &&
        approvedAccounts.length === 0
      ) {
        this.showRegisterForm = true;
        this.registerAccountTypes = this.setAccountTypes(
          pendingAccounts,
          'Register'
        );
      } else if (pendingAccounts.length === 0 && approvedAccounts.length === 0)
        this.showRegisterForm = true;
    }
  }

  setAccountTypes(accounts: AccountResponse[], formType: string) {
    return formType === 'Transaction'
      ? [
          accounts[0].accountType === 'Checking'
            ? { value: 'C', viewValue: 'Checking' }
            : { value: 'S', viewValue: 'Savings' },
        ]
      : [
          accounts[0].accountType === 'Savings'
            ? { value: 'C', viewValue: 'Checking' }
            : { value: 'S', viewValue: 'Savings' },
        ];
  }

  submitBankAccount() {
    if (this.accountForm.valid) {
      this.accountService.saveBankAccount(this.accountForm.value).subscribe(
        (account) => (this.accountMessage = account.message),
        (errorResponse) => {
          this.accountMessage = errorResponse.error.message;
          this.accountForm.reset();
        },
        () => {
          setTimeout(() => {
            this.accountMessage = '';
            window.location.reload();
          }, 2000);
        }
      );
    } else this.accountMessage = 'Please enter account details to register....';
  }

  submitBankTransaction() {
    if (this.transactionForm.valid) {
      this.transactionForm.controls['accountNumber'].setValue(
        this.transactionForm.controls['accountNumber'].value === 'C'
          ? this.customerLoggedIn.checkingAccount.accountNumber
          : this.customerLoggedIn.savingsAccount.accountNumber
      );
      this.accountService
        .updateBankAccountBalance(this.transactionForm.value)
        .subscribe(
          (account) => {
            this.setBankAccount(account);
            this.transactionMessage = account.message;
          },
          (errorResponse) => {
            this.transactionMessage = errorResponse.error.message;
            this.transactionForm.reset();
          },
          () => {
            setTimeout(() => {
              this.transactionMessage = '';
              window.location.reload();
            }, 2000);
          }
        );
    } else
      this.transactionMessage = 'Please enter transaction details to send....';
  }
}
