import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user-service/user.service';
import { RepositoryService } from '../services/repository-service/repository.service';
import { ActivatedRoute } from '@angular/router';
import { MagazineService } from '../services/magazine-service/magazine.service';

@Component({
  selector: 'app-checking-magazine',
  templateUrl: './checking-magazine.component.html',
  styleUrls: ['./checking-magazine.component.css']
})
export class CheckingMagazineComponent implements OnInit {
  private formFieldsDto = null;
  private formFields = [];
  private taskId = '';
  
  // tslint:disable-next-line:max-line-length
  constructor(private userService: UserService, private magazineService:MagazineService, private repositoryService: RepositoryService, private route: ActivatedRoute) {
    this.route.params.subscribe( params => {this.taskId = params.task_id; });
    const x = repositoryService.loadTask(this.taskId);

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
        console.log('property');
        console.log(property);
        console.log('value[property] je');
        console.log(value[property]);
        o.push({fieldId : property, fieldValue : value[property]});
        console.log('niz za slanje izgleda');
        console.log(o);
     }

    console.log(o);
    let x = this.magazineService.approveMagazine(o, this.formFieldsDto.taskId);
    console.log('Pre subscribe');
    x.subscribe(
      res => {
        console.log(res);
        alert('You did checking!');
        window.location.href = '';
      },
      err => {
        console.log('Error occured');
      }
    );
  }

}
