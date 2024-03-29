import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Credentials, User, UserResponse } from '../model/user.interface';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  public saveUser$ = (user: User) =>
    <Observable<UserResponse>>(
      this.http.post<UserResponse>(`${environment.usersApiUrl}/addUser`, user)
    );

  public fetchUserById$ = (id: string | null) =>
    <Observable<UserResponse>>(
      this.http.get<UserResponse>(`${environment.usersApiUrl}/getUser/${id}`)
    );

  public fetchAllUsers$ = () =>
    <Observable<UserResponse[]>>(
      this.http.get<UserResponse[]>(`${environment.usersApiUrl}/getAllUsers`)
    );

  public validateUser$ = (credentials: Credentials) =>
    <Observable<UserResponse>>(
      this.http.post<UserResponse>(
        `${environment.usersApiUrl}/checkUser`,
        credentials
      )
    );
}
