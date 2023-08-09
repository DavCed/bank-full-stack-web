import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../service/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  public loginForm: FormGroup;
  public loginBtn: string = 'Login';
  public registerBtn: string = 'Sign up';
  public message!: string;
  private isSuccess!: boolean;

  constructor(private userService: UserService, private router: Router) {
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

  showSuccessOrError(): string {
    return this.isSuccess ? 'green' : 'red';
  }

  attemptToLogin() {
    if (this.loginForm.valid) {
      this.userService.validateUser$(this.loginForm.value).subscribe(
        (user) => {
          this.message = user.message;
          this.isSuccess = true;
          setTimeout(
            () => this.router.navigate([`landing-page/${user.userId}`]),
            1000
          );
        },
        (errorResponse) => {
          this.message = errorResponse.error.message;
          this.isSuccess = false;
          this.loginForm.reset();
        }
      );
    } else this.message = 'Please enter credentials to log in....';
  }
}
