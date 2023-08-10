import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../service/user.service';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  public userId!: number;
  public loginForm: FormGroup;
  public loginBtn: string = 'Login';
  public registerBtn: string = 'Sign up';

  constructor(
    private userService: UserService,
    private router: Router,
    private notifierService: NotifierService
  ) {
    this.loginForm = this.generateLoginForm();
  }

  generateLoginForm() {
    return new FormGroup({
      email: new FormControl('', [
        Validators.required,
        Validators.pattern('[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,4}$'),
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
      ]),
    });
  }

  goToRegisterPage() {
    this.loginForm.reset();
    this.router.navigate(['register']);
  }

  attemptToLogin() {
    if (this.loginForm.valid) {
      this.userService.validateUser$(this.loginForm.value).subscribe(
        (user) => {
          this.notifierService.notify('success', user.message);
          this.userId = user.userId;
        },
        (errorResponse) => {
          this.notifierService.notify('error', errorResponse.error.message);
          this.loginForm.reset();
        },
        () => this.router.navigate([`landing-page/${this.userId}`])
      );
    } else
      this.notifierService.notify(
        'warning',
        'Please enter credentials to log in....'
      );
  }
}
