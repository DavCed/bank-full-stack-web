import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { UserResponse } from 'src/app/model/user.interface';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss'],
})
export class LandingComponent implements OnInit {
  public customerId!: BehaviorSubject<string | null>;
  public customerLoggedIn$!: Observable<UserResponse>;

  constructor(
    private activeRoute: ActivatedRoute,
    private userService: UserService
  ) {
    this.activeRoute.paramMap.subscribe(
      (param) =>
        (this.customerId = new BehaviorSubject<string | null>(param.get('id')))
    );
  }

  ngOnInit() {
    this.customerLoggedIn$ = this.userService
      .fetchUserById$(this.customerId.value)
      .pipe(
        map((customer) => {
          return customer;
        })
      );
  }
}
