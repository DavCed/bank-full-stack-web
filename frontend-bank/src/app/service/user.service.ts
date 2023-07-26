import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Credentials, User, UserResponse } from '../model/user.interface';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  saveUser(user: User): Observable<UserResponse> {
    return this.http.post<UserResponse>(
      `${environment.usersBackEndUrl}/addUser`,
      user
    );
  }

  logUserIn(credentials: Credentials): Observable<UserResponse> {
    return this.http.post<UserResponse>(
      `${environment.usersBackEndUrl}/checkUser`,
      credentials
    );
  }

  loadUserProfileById(id: number): Observable<UserResponse> {
    return this.http.get<UserResponse>(
      `${environment.usersBackEndUrl}/getUser/${id}`
    );
  }

  loadAllUsers(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(
      `${environment.usersBackEndUrl}/getUsers`
    );
  }
}
