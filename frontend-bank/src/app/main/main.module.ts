import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login-page/login.component';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { LandingComponent } from './landing-page/landing.component';
import { MainRoutingModule } from './main-routing.module';
import { MainComponent } from './main.component';
import { MatTableModule } from '@angular/material/table';
import { RegisterComponent } from './register-page/register.component';
import { CustomerShowComponent } from './customer-show/customer-show.component';
import { EmployeeShowComponent } from './employee-show/employee-show.component';

@NgModule({
  declarations: [
    LoginComponent,
    LandingComponent,
    MainComponent,
    RegisterComponent,
    CustomerShowComponent,
    EmployeeShowComponent,
  ],
  imports: [
    CommonModule,
    MainRoutingModule,
    FlexLayoutModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatIconModule,
    MatButtonModule,
    MatSelectModule,
    MatTableModule,
  ],
})
export class MainModule {}
