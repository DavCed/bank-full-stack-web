import { Component, OnInit } from '@angular/core';
import {
  UntypedFormControl,
  UntypedFormGroup,
  Validators,
} from '@angular/forms';
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
  public hasMaxAccounts: boolean = false;
  private customerId: string | null = '';

  public accountForm: UntypedFormGroup;
  public accountMessage: string = '';
  public accountTypes = [
    { value: 'C', viewValue: 'Checking' },
    { value: 'S', viewValue: 'Savings' },
  ];

  public transactionForm: UntypedFormGroup;
  public transactionMessage: string = '';
  public transactionTypes = [
    { value: 'W', viewValue: 'Withdraw' },
    { value: 'D', viewValue: 'Deposit' },
  ];

  public dataSourceAccounts: AccountResponse[] = [];
  public dataHeaders: string[] = [
    'Account Type',
    'Account Number',
    'Routing Number',
    'Balance',
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
          this.dataSourceAccounts = accounts;
          this.checkBankAccounts(this.dataSourceAccounts);
        });
    });
  }

  generateAccountForm() {
    return new UntypedFormGroup({
      accountId: new UntypedFormControl(null),
      userId: new UntypedFormControl(this.customerId),
      balance: new UntypedFormControl(null, Validators.required),
      accountNumber: new UntypedFormControl(null),
      routingNumber: new UntypedFormControl(null),
      accountType: new UntypedFormControl(null, Validators.required),
    });
  }

  generateTransactionForm() {
    return new UntypedFormGroup({
      amount: new UntypedFormControl(null, Validators.required),
      transactionType: new UntypedFormControl(null, Validators.required),
      accountNumber: new UntypedFormControl(null, Validators.required),
    });
  }

  setBankAccount(account: AccountResponse) {
    if (account.accountType === 'Checking')
      this.customerLoggedIn.checkingAccount = account;
    else if (account.accountType === 'Savings')
      this.customerLoggedIn.savingsAccount = account;
  }

  checkBankAccounts(accounts: AccountResponse[]) {
    if (accounts.length !== 2) {
      if (accounts.length === 1) {
        this.accountTypes = [
          accounts[0].accountType === 'Checking'
            ? { value: 'S', viewValue: 'Savings' }
            : { value: 'C', viewValue: 'Checking' },
        ];
      }
    } else this.hasMaxAccounts = true;
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
        .updateBankAccount(this.transactionForm.value)
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
