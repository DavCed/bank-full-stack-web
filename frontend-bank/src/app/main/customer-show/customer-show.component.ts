import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { AccountResponse } from 'src/app/model/account.interface';
import { UserResponse } from 'src/app/model/user.interface';
import { AccountService } from 'src/app/service/account.service';
import { UserService } from 'src/app/service/user.service';
import { AccountTypes } from '../../enum/account-types.enum';
import { FormTypes } from '../../enum/form-types.enum';
import { HeaderTypes } from '../../enum/header-types.enum';
import { StatusTypes } from '../../enum/status-types.enum';
import { TransactionTypes } from '../../enum/transaction-types.enum';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'app-customer-show',
  templateUrl: './customer-show.component.html',
  styleUrls: ['./customer-show.component.scss'],
})
export class CustomerShowComponent implements OnInit {
  public customerLoggedIn!: BehaviorSubject<UserResponse>;
  private customerId!: string | null;
  public showTransactionForm!: boolean;
  public showRegisterForm!: boolean;

  public readonly headers = HeaderTypes;

  public accountForm: FormGroup;
  public accountMessage!: string;
  public registerAccountTypes: { value: string; viewValue: AccountTypes }[] = [
    { value: 'C', viewValue: AccountTypes.C },
    { value: 'S', viewValue: AccountTypes.S },
  ];

  public transactionForm: FormGroup;
  public transactionMessage!: string;
  public transactionTypes: { value: string; viewValue: TransactionTypes }[] = [
    { value: 'W', viewValue: TransactionTypes.W },
    { value: 'D', viewValue: TransactionTypes.D },
    { value: 'T', viewValue: TransactionTypes.T },
  ];
  public transactionAccountTypes: { value: string; viewValue: AccountTypes }[] =
    [
      { value: 'C', viewValue: AccountTypes.C },
      { value: 'S', viewValue: AccountTypes.S },
    ];

  public dataSourceAccounts: AccountResponse[] = [];
  public dataHeaders: HeaderTypes[] = [
    HeaderTypes.AT,
    HeaderTypes.AN,
    HeaderTypes.RN,
    HeaderTypes.BA,
    HeaderTypes.AS,
  ];

  constructor(
    private accountService: AccountService,
    private activeRoute: ActivatedRoute,
    private userService: UserService,
    private notifier: NotifierService
  ) {
    this.activeRoute.paramMap.subscribe(
      (param) => (this.customerId = param.get('id'))
    );
    this.accountForm = this.generateAccountForm();
    this.transactionForm = this.generateTransactionForm();
  }

  ngOnInit(): void {
    this.userService.fetchUserById$(this.customerId).subscribe((customer) => {
      this.customerLoggedIn = new BehaviorSubject<UserResponse>(customer);
      this.accountService
        .fetchBankAccountsByUserId$(this.customerId)
        .subscribe((accounts) => {
          accounts.map((account) => this.setBankAccount(account));
          this.dataSourceAccounts = accounts;
          this.checkBankAccounts(
            this.dataSourceAccounts.filter(
              (account) => account.accountStatus === StatusTypes.A
            ),
            this.dataSourceAccounts.filter(
              (account) => account.accountStatus === StatusTypes.P
            )
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
    if (account.accountStatus === StatusTypes.A) {
      if (account.accountType === AccountTypes.C)
        this.customerLoggedIn.value.checkingAccount = account;
      else if (account.accountType === AccountTypes.S)
        this.customerLoggedIn.value.savingsAccount = account;
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
          FormTypes.T
        );
      this.transactionTypes = this.setTransactionTypes();
    }
    if (
      (pendingAccounts.length === 0 || pendingAccounts.length === 1) &&
      approvedAccounts.length < 2
    ) {
      if (pendingAccounts.length === 0 && approvedAccounts.length === 1) {
        this.showRegisterForm = true;
        this.registerAccountTypes = this.setAccountTypes(
          approvedAccounts,
          FormTypes.R
        );
      } else if (
        pendingAccounts.length === 1 &&
        approvedAccounts.length === 0
      ) {
        this.showRegisterForm = true;
        this.registerAccountTypes = this.setAccountTypes(
          pendingAccounts,
          FormTypes.R
        );
      } else if (pendingAccounts.length === 0 && approvedAccounts.length === 0)
        this.showRegisterForm = true;
    }
  }

  setTransactionTypes() {
    return this.customerLoggedIn.value.checkingAccount !== undefined &&
      this.customerLoggedIn.value.savingsAccount !== undefined
      ? [
          { value: 'W', viewValue: TransactionTypes.W },
          { value: 'D', viewValue: TransactionTypes.D },
          { value: 'T', viewValue: TransactionTypes.T },
        ]
      : [
          { value: 'W', viewValue: TransactionTypes.W },
          { value: 'D', viewValue: TransactionTypes.D },
        ];
  }

  setAccountTypes(accounts: AccountResponse[], formType: string) {
    return formType === FormTypes.T
      ? [
          accounts[0].accountType === AccountTypes.C
            ? { value: 'C', viewValue: AccountTypes.C }
            : { value: 'S', viewValue: AccountTypes.S },
        ]
      : [
          accounts[0].accountType === AccountTypes.S
            ? { value: 'C', viewValue: AccountTypes.C }
            : { value: 'S', viewValue: AccountTypes.S },
        ];
  }

  submitBankAccount() {
    if (this.accountForm.valid) {
      this.accountService.saveBankAccount$(this.accountForm.value).subscribe(
        (account) => this.notifier.notify('success', account.message),
        (errorResponse) => {
          this.notifier.notify('error', errorResponse.error.message);
          this.accountForm.reset();
        },
        () => {
          if (this.dataSourceAccounts.length >= 1)
            this.showRegisterForm = false;
          this.ngOnInit();
          this.accountForm.reset();
        }
      );
    } else
      this.notifier.notify(
        'warning',
        'Please enter account details to register....'
      );
  }

  submitBankTransaction() {
    if (this.transactionForm.valid) {
      this.transactionForm.controls['accountNumber'].setValue(
        this.transactionForm.controls['accountNumber'].value === 'C'
          ? this.customerLoggedIn.value.checkingAccount.accountNumber
          : this.customerLoggedIn.value.savingsAccount.accountNumber
      );
      this.accountService
        .updateBankAccountBalance$(this.transactionForm.value)
        .subscribe(
          (account) => {
            this.notifier.notify('success', account.message);
            this.setBankAccount(account);
          },
          (errorResponse) => {
            this.notifier.notify('error', errorResponse.error.message);
            this.transactionForm.reset();
          },
          () => {
            this.ngOnInit();
            this.transactionForm.reset();
          }
        );
    } else
      this.notifier.notify(
        'warning',
        'Please enter transaction details to send....'
      );
  }
}
