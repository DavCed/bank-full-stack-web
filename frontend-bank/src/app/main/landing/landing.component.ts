import { Component, OnInit } from '@angular/core';
import {
  UntypedFormControl,
  UntypedFormGroup,
  Validators,
} from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AccountResponse, Transaction } from 'src/app/model/account.interface';
import { UserResponse } from 'src/app/model/user.interface';
import { AccountService } from 'src/app/service/account.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss'],
})
export class LandingComponent implements OnInit {
  private userLoggedIn: any;
  public userType: string = '';
  public name: string = '';
  public email: string = '';
  private id: any;

  public transactionForm: UntypedFormGroup;
  public accountForm: UntypedFormGroup;
  public hasMaxAccounts: any;

  public btnText1: string = '';
  public btnText2: string = '';
  public accountMessage: string = '';
  public transactionMessage: string = '';

  private userCheckAcc: UserResponse[] = [];
  private userSavAcc: UserResponse[] = [];
  private accounts: AccountResponse[] = [];

  public displayCustomerTable: any;
  public displayCustomerForms: any;
  public displayedColumns1: string[] = [
    'Account Type',
    'Account Number',
    'Routing Number',
    'Balance',
  ];
  public displayedColumns2: string[] = [
    'Name',
    'Email',
    'Account Number',
    'Routing Number',
    'Balance',
    'Actions',
  ];
  public dataSource1: AccountResponse[] = [];
  public dataSource2: UserResponse[] = [];
  public dataSource3: UserResponse[] = [];

  public transactionList = [
    { value: 'W', viewValue: 'Withdraw' },
    { value: 'D', viewValue: 'Deposit' },
  ];
  public accountTypes = [
    { value: 'C', viewValue: 'Checking' },
    { value: 'S', viewValue: 'Savings' },
  ];

  constructor(
    private activeRoute: ActivatedRoute,
    private userService: UserService,
    private accountService: AccountService
  ) {
    this.activeRoute.paramMap.subscribe((param) => (this.id = param.get('id')));
    this.transactionForm = this.generateTransactionForm();
    this.accountForm = this.generateAccountForm();
  }

  ngOnInit() {
    this.userService.loadUserProfileById(this.id).subscribe((user) => {
      this.userLoggedIn = user;
      this.name = user.name;
      this.email = user.email;
      switch (user.userType) {
        case 'E':
          this.userType = 'Employee';
          this.getEmployeeAccounts();
          break;
        case 'C':
          this.userType = 'Customer';
          this.getCustomerAccounts();
          break;
      }
    });
  }

  getCustomerAccounts() {
    this.displayCustomerForms = true;
    this.displayCustomerTable = true;
    this.btnText1 = 'Register';
    this.btnText2 = 'Complete';
    this.accountService.getAccountsByUserId(this.id).subscribe((response) => {
      response.map((account) => {
        switch (account.accountType) {
          case 'Checking':
            this.userLoggedIn.checkingAccount = account;
            break;
          case 'Savings':
            this.userLoggedIn.savingsAccount = account;
            break;
        }
      });
      this.dataSource1 = response;
      this.hasMaxAccounts = this.dataSource1.length == 2 ? true : false;
    });
  }

  getEmployeeAccounts() {
    this.displayCustomerForms = false;
    this.displayCustomerTable = false;
    this.accountService
      .getAllAccounts()
      .subscribe((response) => (this.accounts = response));
    this.userService.loadAllUsers().subscribe((response) => {
      response.map((user) => {
        this.accounts.map((account) => {
          if (user.userId == account.userId) {
            switch (account.accountType) {
              case 'Checking':
                user.checkingAccount = account;
                this.userCheckAcc.push(user);
                break;
              case 'Savings':
                user.savingsAccount = account;
                this.userSavAcc.push(user);
                break;
            }
          }
        });
      });
      this.dataSource2 = this.userCheckAcc;
      this.dataSource3 = this.userSavAcc;
    });
  }

  generateAccountForm() {
    return new UntypedFormGroup({
      userId: new UntypedFormControl(this.id),
      balance: new UntypedFormControl(null, Validators.required),
      accountNumber: new UntypedFormControl(0),
      routingNumber: new UntypedFormControl(0),
      accountType: new UntypedFormControl(null, Validators.required),
    });
  }

  generateTransactionForm() {
    return new UntypedFormGroup({
      amount: new UntypedFormControl(null, Validators.required),
      transaction: new UntypedFormControl(null, Validators.required),
      account: new UntypedFormControl(null, Validators.required),
    });
  }

  submitAccount(accountForm: AccountResponse) {
    accountForm.userId = this.id;
    accountForm.message = '';
    this.accountService
      .saveAccount(accountForm)
      .subscribe((response) => (this.accountMessage = response.message));
    setTimeout(() => {
      this.accountMessage = '';
      window.location.reload();
    }, 2000);
  }

  submitTransaction(transactionForm: Transaction) {
    if (
      transactionForm.amount !== null &&
      transactionForm.transaction !== null &&
      transactionForm.account !== null
    ) {
      this.accountService
        .updateAccount([
          transactionForm.account === 'C'
            ? this.userLoggedIn.checkingAccount.accountNumber.toString()
            : this.userLoggedIn.savingsAccount.accountNumber.toString(),
          transactionForm.amount.toString(),
          transactionForm.transaction.toString(),
        ])
        .subscribe((account) => {
          this.userLoggedIn.checkingAccount = account;
          this.transactionMessage = this.userLoggedIn.checkingAccount.message;
        });
      setTimeout(() => {
        this.transactionMessage = '';
        window.location.reload();
      }, 2000);
    }
  }

  approveAccount() {}

  denyAccount() {}
}
