import { Component, OnInit } from '@angular/core';
import {
  UntypedFormControl,
  UntypedFormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Credentials } from '../../model/user.interface';
import { UserService } from '../../service/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  public userForm: UntypedFormGroup;
  private credentials: Credentials = {
    email: '',
    password: '',
  };

  public btnText1: string;
  public btnText2: string;
  public message: any;
  private isSuccess: boolean;

  public displayLogin: boolean;
  public userTypes = [
    { value: 'E', viewValue: 'Employee' },
    { value: 'C', viewValue: 'Customer' },
  ];

  constructor(private userService: UserService, private router: Router) {
    this.userForm = this.generateForm();
    this.displayLogin = true;
    this.btnText1 = 'Login';
    this.btnText2 = 'Register Now';
    this.isSuccess = false;
  }

  ngOnInit(): void {}

  generateForm() {
    return new UntypedFormGroup({
      firstName: new UntypedFormControl('', [
        Validators.required,
        Validators.maxLength(26),
      ]),
      lastName: new UntypedFormControl('', [
        Validators.required,
        Validators.maxLength(26),
      ]),
      email: new UntypedFormControl('', [
        Validators.required,
        Validators.pattern('[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,4}$'),
      ]),
      password: new UntypedFormControl('', [
        Validators.required,
        Validators.minLength(8),
      ]),
      phoneNumber: new UntypedFormControl('', [
        Validators.minLength(10),
        Validators.pattern('[0-9]{3}-[0-9]{3}-[0-9]{4}'),
      ]),
      userType: new UntypedFormControl(null, [Validators.required]),
    });
  }

  changeBtnText(btnText1: string, btnText2: string) {
    this.btnText1 = btnText1;
    this.btnText2 = btnText2;
    this.message = '';
  }

  changeComponent() {
    this.btnText2 === 'Register Now'
      ? (this.displayLogin = false)
      : (this.displayLogin = true);
    this.displayLogin
      ? this.changeBtnText('Login', 'Register Now')
      : this.changeBtnText('Register', 'Login Now');
    this.userForm.reset();
  }

  submitForm(userForm: any) {
    this.displayLogin
      ? this.attemptToLogin(userForm)
      : this.attemptToSignUp(userForm);
  }

  showSuccessOrError(): string {
    return this.isSuccess ? 'green' : 'red';
  }

  attemptToLogin(userForm: any) {
    this.credentials = {
      email: userForm.email,
      password: userForm.password,
    };
    this.userService.logUserIn(this.credentials).subscribe((user) => {
      this.message = user.message;
      if (user.message.includes('success')) {
        this.isSuccess = true;
        setTimeout(() => {
          this.router.navigate([`landing-page/${user.userId}`]);
        }, 1000);
      }
    });
  }

  attemptToSignUp(userForm: any) {
    this.userService.saveUser(userForm).subscribe((user) => {
      this.message = user.message;
      if (user.message.includes('open')) {
        this.isSuccess = true;
        setTimeout(() => {
          this.displayLogin = true;
          this.message = '';
          this.userForm.get('email')?.reset();
          this.userForm.get('password')?.reset();
        }, 1000);
      }
    });
    this.changeComponent();
  }
}
