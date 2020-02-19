import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user-service/user.service';
import { RepositoryService } from '../services/repository-service/repository.service';
import { ActivatedRoute } from '@angular/router';
import { MagazineService } from '../services/magazine-service/magazine.service';

@Component({
  selector: 'app-check-theme',
  templateUrl: './check-theme.component.html',
  styleUrls: ['./check-theme.component.css']
})
export class CheckThemeComponent implements OnInit {
  private formFieldsDto = null;
  private formFields = [];
  private taskId = '';

  // tslint:disable-next-line:max-line-length
  constructor(private magazineService: MagazineService, private userService: UserService, private repositoryService: RepositoryService, private route: ActivatedRoute) { 
    this.route.params.subscribe( params => {this.taskId = params.task_id; });
    const x = repositoryService.loadTaskTheme(this.taskId);

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
    const x = this.magazineService.approveTheme(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        console.log('Zavrsen approve');
        window.location.href = '/homepage';
      },
      err => {
        console.log('Error occured');
      }
    );
  }
}
