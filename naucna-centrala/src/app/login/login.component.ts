import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, FormBuilder, NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { User } from '../model/User';
import { UserService } from '../services/user-service/user.service';
import { AuthService } from '../services/auth-service/auth.service';
import { map } from 'rxjs/operators';
import { send } from 'q';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm = this.formBuilder.group({username: ['', Validators.required],
                                      password: ['', [Validators.required,
                                                    Validators.minLength(2),
                                                    Validators.maxLength(50)]] });

  loginError = '';
  sendUser: User = new User();
  loggedUser: any;

  // tslint:disable-next-line:max-line-length
  constructor(private authService: AuthService, private route: ActivatedRoute, private formBuilder: FormBuilder, private userService: UserService) { }

  ngOnInit() {
  }
  login2(submittedForm: FormGroup) {
    const username = submittedForm.get('username').value;
    const password = submittedForm.get('password').value;
    this.sendUser.username = username;
    this.sendUser.password = password;
    console.log('pritisnut login');
    this.authService.login(this.sendUser).subscribe(
      (success) => {
        this.authService.getUser().subscribe(
          (user: any) => {
            if (user.role == 'ADMIN' ) {
                console.log('admin je');
            }
          }
        );
        window.location.href = '/homepage';
      },
      (error) => {
        alert("Podaci koje ste uneli nisu ispravni, pokusajte ponovo.");
      }
    )
  }
  login(submittedForm: FormGroup) {
    const username = submittedForm.get('username').value;
    const password = submittedForm.get('password').value;
    this.sendUser.username = username;
    this.sendUser.password = password;
    console.log('u login btn je  ' + username + ', ' + password);
    let x = this.userService.loginUser(this.sendUser);
    console.log('Pre subscribe');
    x.subscribe(
      res => {
        console.log(res);
        alert('Successfully logged in!');
        sessionStorage.setItem('loggedUser', JSON.stringify(res));
        window.location.href = '/homepage';
      },
      err => {
        console.log('Username or password are not correct!');
      }
    );
  }
}
