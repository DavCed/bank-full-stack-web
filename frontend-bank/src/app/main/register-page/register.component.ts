import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { UserService } from 'src/app/service/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  public registerForm: FormGroup;
  public registerBtn: string = 'Sign Up';
  public loginBtn: string = 'Back to Login';
  public message: string = '';
  private isSuccess: boolean = false;
  public userTypes = [
    { value: 'E', viewValue: 'Employee' },
    { value: 'C', viewValue: 'Customer' },
  ];

  constructor(private userService: UserService, private router: Router) {
    this.registerForm = this.generateRegisterForm();
  }

  generateRegisterForm() {
    return new FormGroup({
      firstName: new FormControl(null, [
        Validators.required,
        Validators.maxLength(26),
      ]),
      lastName: new FormControl(null, [
        Validators.required,
        Validators.maxLength(26),
      ]),
      email: new FormControl(null, [
        Validators.required,
        Validators.pattern('[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,4}$'),
      ]),
      password: new FormControl(null, [
        Validators.required,
        Validators.minLength(8),
      ]),
      phoneNumber: new FormControl(null, [
        Validators.minLength(10),
        Validators.pattern('[0-9]{3}-[0-9]{3}-[0-9]{4}'),
      ]),
      userType: new FormControl(null, [Validators.required]),
    });
  }

  goToLoginPage() {
    this.registerForm.reset();
    this.router.navigate(['login']);
  }

  showSuccessOrError(): string {
    return this.isSuccess ? 'green' : 'red';
  }

  attemptToSignUp() {
    if (this.registerForm.valid) {
      this.userService.saveUser(this.registerForm.value).subscribe(
        (user) => {
          this.message = user.message;
          this.isSuccess = true;
          setTimeout(() => {
            this.message = '';
          }, 2000);
        },
        (errorResponse) => {
          this.message = errorResponse.error.message;
          this.registerForm.reset();
        },
        () => this.goToLoginPage()
      );
    } else this.message = 'Please enter user details to register....';
  }
}
