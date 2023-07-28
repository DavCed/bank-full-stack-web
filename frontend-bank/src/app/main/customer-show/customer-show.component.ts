import { Component, OnInit } from '@angular/core';
import {
  UntypedFormControl,
  UntypedFormGroup,
  Validators,
} from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AccountResponse, Transaction } from 'src/app/model/account.interface';
import { AccountService } from 'src/app/service/account.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-customer-show',
  templateUrl: './customer-show.component.html',
  styleUrls: ['./customer-show.component.scss'],
})
export class CustomerShowComponent implements OnInit {
  private customerLoggedIn: any;
  public hasMaxAccounts: any;
  private id: any;

  public accountForm: UntypedFormGroup;
  public accountMessage: string = '';
  public accountTypes = [
    { value: 'C', viewValue: 'Checking' },
    { value: 'S', viewValue: 'Savings' },
  ];

  public transactionForm: UntypedFormGroup;
  public transactionMessage: string = '';
  private transactionObj: Transaction = {
    account: '',
    amount: 0,
    transactionType: '',
  };
  public transactionList = [
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
    this.activeRoute.paramMap.subscribe((param) => (this.id = param.get('id')));
    this.accountForm = this.generateAccountForm();
    this.transactionForm = this.generateTransactionForm();
  }

  ngOnInit(): void {
    this.userService.getUserById(this.id).subscribe((customer) => {
      this.customerLoggedIn = customer;
      this.accountService
        .getAccountsByUserId(this.id)
        .subscribe((customerAccounts) => {
          customerAccounts.map((account) => {
            switch (account.accountType) {
              case 'Checking':
                this.customerLoggedIn.checkingAccount = account;
                break;
              case 'Savings':
                this.customerLoggedIn.savingsAccount = account;
                break;
            }
          });
          this.dataSourceAccounts = customerAccounts;
          this.hasMaxAccounts =
            this.dataSourceAccounts.length === 2 ? true : false;
        });
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
      transactionType: new UntypedFormControl(null, Validators.required),
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
      transactionForm.transactionType !== null &&
      transactionForm.account !== null
    ) {
      this.transactionObj = {
        account:
          transactionForm.account === 'C'
            ? this.customerLoggedIn.checkingAccount.accountNumber.toString()
            : this.customerLoggedIn.savingsAccount.accountNumber.toString(),
        amount: transactionForm.amount,
        transactionType: transactionForm.transactionType,
      };
      this.accountService
        .updateAccount(this.transactionObj)
        .subscribe((account) => {
          switch (account.accountType) {
            case 'Checking':
              this.customerLoggedIn.checkingAccount = account;
              this.transactionMessage =
                this.customerLoggedIn.checkingAccount.message;
              break;
            case 'Savings':
              this.customerLoggedIn.savingsAccount = account;
              this.transactionMessage =
                this.customerLoggedIn.savingsAccount.message;
              break;
          }
        });
      setTimeout(() => {
        this.transactionMessage = '';
        window.location.reload();
      }, 2000);
    }
  }
}
