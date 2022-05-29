import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { count, filter, map, tap } from 'rxjs';
import { Account, AccountResponse } from 'src/app/model/account.interface';
import { UserResponse } from 'src/app/model/user.interface';
import { AccountService } from 'src/app/service/account.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit {

  userLoggedIn: any;
  userType: any;
  name: any;
  email: any;
  id: any;
  transactionForm: FormGroup;
  accountForm: FormGroup;
  displayCustomerForms: any;
  hasMaxAccounts: any;
  btnText1: any;
  btnText2: any;
  accountMessage: string = '';
  users: UserResponse[] = [];
  userCheckAcc: UserResponse[] = [];
  userSavAcc: UserResponse[] = [];
  accounts: AccountResponse[] = [];
  displayCustomerTable: any;

  displayedColumns1: string[] = ['Account Type', 'Account Number', 'Routing Number', 'Balance'];
  displayedColumns2: string[] = ['Name', 'Email', 'Account Number', 'Routing Number', 'Balance', 'Actions'];
  dataSource1: AccountResponse[] = [];
  dataSource2: UserResponse[] = [];
  dataSource3: UserResponse[] = [];

  transactionList = [
    {value: 'W', viewValue: 'Withdraw'},
    {value: 'D', viewValue: 'Deposit'},
    {value: 'T', viewValue: 'Transfer'}
  ];

  accountTypes = [
    {value: 'C', viewValue: 'Checking'},
    {value: 'S', viewValue: 'Savings'}
  ];

  constructor(private activeRoute: ActivatedRoute, 
              private userService: UserService,
              private accountService: AccountService) {
    this.activeRoute.paramMap.subscribe(param => this.id = param.get('id'));
    this.transactionForm = this.generateTransactionForm();
    this.accountForm = this.generateAccountForm();
  }

  ngOnInit(): void {
    this.userService.loadUserProfileById(this.id).subscribe(user => {
      this.userLoggedIn = user;
      this.name = this.userLoggedIn.firstName + ' ' + this.userLoggedIn.lastName;
      this.email = this.userLoggedIn.email;
      this.userLoggedIn.userType == 'E' ? this.userType = 'Employee' : this.userType = 'Customer';
      this.userType == 'Customer' ? this.getCustomerAccounts() : this.getEmployeeAccounts();
    });
  }

  getCustomerAccounts(){
    this.displayCustomerForms = true;
    this.displayCustomerTable = true;
    this.btnText1 = 'Register';
    this.btnText2 = 'Complete';
    this.accountService.getAccountsByUserId(this.id).subscribe(response => {
      this.dataSource1 = response;
      this.hasMaxAccounts = this.dataSource1.length == 2 ? true : false;
    });
  }

  getEmployeeAccounts(){
    this.displayCustomerForms = false;
    this.displayCustomerTable = false;
    this.accountService.getAllAccounts().subscribe(response => this.accounts = response);
    this.userService.loadAllUsers().subscribe(response => {
      this.users = response;
      this.users.forEach(user => {
        if(user.userType == 'C'){
          this.accounts.forEach(account => {
            if(user.userId == account.userId && account.accountType == 'Checking') {
              user.checkingAccount = account;
              this.userCheckAcc.push(user);
            }
            if(user.userId == account.userId && account.accountType == 'Savings') {
              user.savingsAccount = account;
              this.userSavAcc.push(user);
            }
          });
        }
      });
      this.dataSource2 = this.userCheckAcc;
      this.dataSource3 = this.userSavAcc;
    });
  }

  generateAccountForm() {
    return new FormGroup({
      userId: new FormControl(this.id),
      balance: new FormControl(null, Validators.required),
      accountNumber: new FormControl(0),
      routingNumber: new FormControl(0),
      accountType: new FormControl(null, Validators.required)
    });
  }

  generateTransactionForm() {
    return new FormGroup({
      amount: new FormControl(null, Validators.required),
      transaction: new FormControl(null, Validators.required)
    });
  }

  submitAccount(accountForm: AccountResponse){
    accountForm.userId = this.id;
    accountForm.message = '';
    this.accountService.saveAccount(accountForm).subscribe(response => this.accountMessage = response.message);
    setTimeout(() => {
      this.accountMessage = '';
      window.location.reload();
    }, 3000);
  }

  submitTransaction(transactionForm: any){}

  approveAccount(){}

  denyAccount(){}
}