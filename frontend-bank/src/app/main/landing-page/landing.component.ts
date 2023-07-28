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
  private id: any;
  public showCustomerTable: boolean = false;

  constructor(
    private activeRoute: ActivatedRoute,
    private userService: UserService
  ) {
    this.activeRoute.paramMap.subscribe((param) => (this.id = param.get('id')));
  }

  ngOnInit() {
    this.userService.getUserById(this.id).subscribe((user) => {
      this.name = user.name;
      this.email = user.email;
      this.userType = user.userType === 'E' ? 'Employee' : 'Customer';
      if (this.userType === 'Customer') this.showCustomerTable = true;
    });
  }
}
