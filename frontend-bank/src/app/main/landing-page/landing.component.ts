import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss'],
})
export class LandingComponent implements OnInit {
  public userType: string = '';
  public name: string = '';
  public email: string = '';
  private customerId: string | null = '';
  public showCustomerTable: boolean = false;

  constructor(
    private activeRoute: ActivatedRoute,
    private userService: UserService
  ) {
    this.activeRoute.paramMap.subscribe(
      (param) => (this.customerId = param.get('id'))
    );
  }

  ngOnInit() {
    this.userService.fetchUserById(this.customerId).subscribe((user) => {
      this.name = user.name;
      this.email = user.email;
      if (user.userType === 'C') {
        this.userType = 'Customer';
        this.showCustomerTable = true;
      } else this.userType = 'Employee';
    });
  }
}
