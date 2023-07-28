import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './landing-page/landing.component';
import { LoginComponent } from './login-page/login.component';
import { MainComponent } from './main.component';
import { RegisterComponent } from './register-page/register.component';

const routes: Routes = [
  {
    path: '',
    component: MainComponent,
    children: [
      {
        path: 'login',
        component: LoginComponent,
      },
      {
        path: 'landing-page/:id',
        component: LandingComponent,
      },
      {
        path: 'register',
        component: RegisterComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MainRoutingModule {}
