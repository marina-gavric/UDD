import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Router } from '@angular/router';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loggedUser: any;

  constructor(private http: HttpClient, private router: Router) { }

  login(user) {
    return this.http.post('http://localhost:8080/auth/login', user)
    .pipe(
        map( user => {
            this.loggedUser = user;
            if ( user ) {
                localStorage.setItem('currentUser', JSON.stringify(user));
            }
            return user;
        })
    );
  }

  logout() {
      return this.http.post('http://localhost:8080/auth/logout', null).subscribe(
          success => {
              localStorage.removeItem('currentUser');
              window.location.href = 'login';
                    }, error => alert('Error while trying to logout.')
      );
  }

  getLoggedUser() {
      return localStorage.getItem('currentUser');
  }
  getUser() {
    return this.http.get('http://localhost:8080/auth/getUser') as Observable<any>;
  }

}
