import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { AccountResponse } from 'src/app/model/account.interface';
import { UserResponse } from 'src/app/model/user.interface';
import { AccountService } from 'src/app/service/account.service';
import { UserService } from 'src/app/service/user.service';
import { AccountTypes } from '../../enum/account-types.enum';
import { HeaderTypes } from '../../enum/header-types.enum';
import { StatusTypes } from '../../enum/status-types.enum';

@Component({
  selector: 'app-employee-show',
  templateUrl: './employee-show.component.html',
  styleUrls: ['./employee-show.component.scss'],
})
export class EmployeeShowComponent implements OnInit {
  public actionForm: FormGroup;
  public readonly status = StatusTypes;
  public readonly headers = HeaderTypes;

  public customerCheckingAccounts: UserResponse[] = [];
  public customerSavingsAccounts: UserResponse[] = [];

  public dataSourceCheckingAccounts: UserResponse[] = [];
  public dataSourceSavingsAccounts: UserResponse[] = [];
  public dataHeaders: string[] = [
    HeaderTypes.NA,
    HeaderTypes.AN,
    HeaderTypes.RN,
    HeaderTypes.BA,
    HeaderTypes.AC,
  ];

  constructor(
    private userService: UserService,
    private accountService: AccountService
  ) {
    this.actionForm = this.generateActionForm();
  }

  ngOnInit(): void {
    this.accountService.fetchAllBankAccounts$().subscribe((accounts) => {
      this.userService.fetchAllUsers$().subscribe((users) => {
        accounts.forEach((account) => {
          users.forEach((user) => this.setBankAccountToCustomer(account, user));
        });
        this.dataSourceCheckingAccounts = this.customerCheckingAccounts;
        this.dataSourceSavingsAccounts = this.customerSavingsAccounts;
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
      if (account.accountType === AccountTypes.C) {
        user.checkingAccount = account;
        this.customerCheckingAccounts.push(user);
      } else if (account.accountType === AccountTypes.S) {
        user.savingsAccount = account;
        this.customerSavingsAccounts.push(user);
      }
    }
  }

  changeBankAccountStatus(
    customer: UserResponse,
    account: AccountResponse,
    statusType: StatusTypes
  ) {
    this.actionForm.controls['accountNumber'].setValue(account.accountNumber);
    this.actionForm.controls['accountStatus'].setValue(
      statusType === StatusTypes.A ? 'A' : 'D'
    );
    this.actionForm.controls['userId'].setValue(customer.userId);
    this.accountService
      .updateBankAccountStatus$(this.actionForm.value)
      .subscribe(
        (account) => {
          if (statusType === StatusTypes.A)
            this.setBankAccountToCustomer(account, customer);
        },
        (errorResponse) => console.log(errorResponse),
        () => {
          this.customerCheckingAccounts = [];
          this.customerSavingsAccounts = [];
          this.ngOnInit();
          this.actionForm.reset();
        }
      );
  }
}
