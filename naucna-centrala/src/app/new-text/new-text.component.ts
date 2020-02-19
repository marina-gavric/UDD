import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, FormBuilder, NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../services/user-service/user.service';
import { RepositoryService } from '../services/repository-service/repository.service';
import { AreaService } from '../services/areas-service/area.service';
import { MagazineService } from '../services/magazine-service/magazine.service';

@Component({
  selector: 'app-new-text',
  templateUrl: './new-text.component.html',
  styleUrls: ['./new-text.component.css']
})
export class NewTextComponent implements OnInit {
  private formFieldsDto = null;
  private formFields = [];
  // tslint:disable-next-line:variable-name
  private processInstance = '';
  private enumValues = [];
  private magazines = [];

  constructor(private magazineService: MagazineService, private areaService: AreaService, private repositoryService: RepositoryService) {
    const x = magazineService.startTextProcess();
    x.subscribe(
      res => {
        console.log(res);
        console.log('StartTextProcess');
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        console.log(this.formFields);

        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) => {
          if ( field.type.name === 'enum') {
            this.magazines = Object.keys(field.type.values);
          }
        });
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
    console.log('Pritisnut submit');

    // tslint:disable-next-line:forin
    for (const property in value) {
      o.push({fieldId : property, fieldValue : value[property]});
    }
    console.log(o);
    const x = this.magazineService.chooseMagazine(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        console.log(res);
        if (res==true) {
          console.log('ne treba da plati');
          window.location.href = 'fillText/' + this.processInstance;
        } else {
          console.log('treba da plati');
          window.location.href = 'pay/' + this.processInstance;
                }
      },
      err => {
        console.log('Error occured');
      }
    );
    }

}
