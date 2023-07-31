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
      `${environment.usersApiUrl}/addUser`,
      user
    );
  }

  fetchUserById(id: string | null): Observable<UserResponse> {
    return this.http.get<UserResponse>(
      `${environment.usersApiUrl}/getUser/${id}`
    );
  }

  fetchAllUsers(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(
      `${environment.usersApiUrl}/getAllUsers`
    );
  }

  validateUser(credentials: Credentials): Observable<UserResponse> {
    return this.http.post<UserResponse>(
      `${environment.usersApiUrl}/checkUser`,
      credentials
    );
  }
}
