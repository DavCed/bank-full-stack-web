import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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

  constructor(private activeRoute: ActivatedRoute, private userService: UserService) {
    this.activeRoute.paramMap.subscribe(param => this.id = param.get('id'));
  }

  ngOnInit(): void {
    this.userService.loadUserProfileById(this.id).subscribe(user => {
      this.userLoggedIn = user;
      this.name = this.userLoggedIn.firstName + ' ' + this.userLoggedIn.lastName;
      this.email = this.userLoggedIn.email;
      this.userLoggedIn.userType == 'E' ? this.userType = 'Employee' : this.userType = 'Customer';
    });
  }
}