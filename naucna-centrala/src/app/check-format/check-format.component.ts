import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user-service/user.service';
import { RepositoryService } from '../services/repository-service/repository.service';
import { ActivatedRoute } from '@angular/router';
import { MagazineService } from '../services/magazine-service/magazine.service';

@Component({
  selector: 'app-check-format',
  templateUrl: './check-format.component.html',
  styleUrls: ['./check-format.component.css']
})
export class CheckFormatComponent implements OnInit {
  private formFieldsDto = null;
  private formFields = [];
  private taskId = '';
  private processInstance = '';
  constructor(private magazineService: MagazineService, private repositoryService: RepositoryService, private route: ActivatedRoute) { 
    this.route.params.subscribe( params => {this.taskId = params.task_id; });
    console.log('tu eje');
    const x = repositoryService.loadTask(this.taskId);
    x.subscribe(
      res => {
        console.log(res);
        console.log('Ispis rezultata');
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;

        console.log(this.formFields);
      },
      err => {
        console.log('Error occured');
      }
    );

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
    let x = this.magazineService.checkFormat(o, this.formFieldsDto.taskId);
    console.log('Pre subscribe');
    x.subscribe(
      res => {
        if (res == true) {
          console.log('format je dobar');
          window.location.href = 'selectRev/' + this.processInstance;
        } else {
          console.log('Mora menjati format');
          window.location.href = '';

        }
        console.log('Zavrsio funkciju uspesno');
        console.log(res);
      },
      err => {
        console.log('Error occured');
      }
    );
  }

  ngOnInit() {
  }

}
