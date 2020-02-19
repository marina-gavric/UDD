import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { AuthService } from './services/auth-service/auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    public helper = new JwtHelperService();

    constructor(private authService: AuthService, private router: Router) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // add authorization header with jwt token if available
        const user = JSON.parse(localStorage.getItem('currentUser'));
        if ( user != null) {
            console.log('Neko je ulogovan');
            if (user.accessToken) {
                const isExpired = this.helper.isTokenExpired(user.accessToken);

                if (isExpired) {
                    alert('Please log in yout session is over.');
                    localStorage.removeItem('currentUser');
                    window.location.href = 'login';
                } else {
                    if (user && user.accessToken) {
                        console.log('setting token in header');
                        request = request.clone({
                            setHeaders: {
                                Authorization: `Bearer ${user.accessToken}`
                            }
                        });
                        }
                    }
                }
        }
        return next.handle(request);
    }
}
