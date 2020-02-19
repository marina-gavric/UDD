import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user-service/user.service';
import { RepositoryService } from '../services/repository-service/repository.service';
import { User } from '../model/User';
import { AuthService } from '../services/auth-service/auth.service';
import { UserDTO } from '../model/UserDTO';
@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {
  private formFieldsDto = null;
  private formFields = [];
  private processInstance = '';
  private tasks = [];
  private user: User;
  private type = '';
  private logged: UserDTO;
  constructor(private userService: UserService, private authService: AuthService, private repositoryService: RepositoryService) {
    const pom  = this.authService.getLoggedUser();
    console.log(pom);
    if (pom) {
      this.authService.getUser().subscribe(
        (user: any) => {
          console.log('User ima podatke');
          console.log(user);
          this.logged = user;
          this.type = user.role;
          console.log(this.logged);
          const x = this.repositoryService.getTasksOfUser(this.logged.username);
          x.subscribe(
            res => {
              console.log('ispisujemo');
              console.log(res);
              this.tasks = res;
            },
            err => {
              console.log('Error occured');
            }
          );
        }
      );
    }
   }

  ngOnInit() {

  }
  magForm() {
    window.location.href = 'newMagazine';
  }
  textForm() {
    window.location.href = 'text';
  }
  complete(taskId) {
    const x = this.repositoryService.completeTask(taskId);
    console.log('Usao u complete task');
   }
  logoutFunc() {
    alert('Logout pritisnut');
    const x =  this.authService.logout();
    console.log('Pre subscribe');

}
}
