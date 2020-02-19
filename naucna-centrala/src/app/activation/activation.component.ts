import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user-service/user.service';
import { RepositoryService } from '../services/repository-service/repository.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-activation',
  templateUrl: './activation.component.html',
  styleUrls: ['./activation.component.css']
})
export class ActivationComponent implements OnInit { 
  private formFieldsDto = null;
  private formFields = [];
  private processId = '';
  private username = '';
  constructor(private userService: UserService, private repositoryService: RepositoryService, private route: ActivatedRoute) { 
    this.route.params.subscribe( params => {this.processId = params.process_id; this.username = params.username; });
    const x = repositoryService.getTask(this.processId);
    x.subscribe(
      res => {
        console.log(res);
        console.log('Ispis rezultata');
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        console.log(this.formFields);
      },
      err => {
        console.log('Error occured');
      }
    );


 }

  ngOnInit() {
  }
  onSubmit(value, form) {
    console.log(form);
    console.log(value);
    const o = new Array();
    console.log('U onSubmitu je');

    // tslint:disable-next-line:forin
    for (const property in value) {
        o.push({fieldId : property, fieldValue : value[property]});
        console.log('niz za slanje izgleda');
        console.log(o);
     }

    console.log(o);
    let x = this.userService.activateUser(o, this.formFieldsDto.taskId, this.username);
    console.log('Pre subscribe');
    x.subscribe(
      res => {
        console.log(res);
        alert('You did activation!');
        window.location.href = 'login';
      },
      err => {
        console.log('Error occured');
      }
    );
  }
}
