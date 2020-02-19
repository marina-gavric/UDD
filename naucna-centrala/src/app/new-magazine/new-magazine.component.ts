import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, FormBuilder, NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../services/user-service/user.service';
import { RepositoryService } from '../services/repository-service/repository.service';
import { AreaService } from '../services/areas-service/area.service';
import { MagazineService } from '../services/magazine-service/magazine.service';
@Component({
  selector: 'app-new-magazine',
  templateUrl: './new-magazine.component.html',
  styleUrls: ['./new-magazine.component.css']
})
export class NewMagazineComponent implements OnInit {
  

  private formFieldsDto = null;
  private formFields = [];
  // tslint:disable-next-line:variable-name
  private processInstance = '';
  private enumValues = [];
  private categories = [];

  constructor(private magazineService: MagazineService, private areaService: AreaService, private repositoryService: RepositoryService) { 
    const x = repositoryService.startMagazineProcess();

    x.subscribe(
      res => {
        console.log(res);
        console.log('Ispis rezultata');
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        console.log(this.formFields);

        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) => {
          if ( field.type.name === 'enum') {
            this.enumValues = Object.keys(field.type.values);
          }
        });
        this.areaService.loadAreas().subscribe(
          res => {
            console.log('Ispis kategorija');
            console.log(res);
            this.categories = res;
           }
        );
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
    console.log('ispisivanje');

    // tslint:disable-next-line:forin
    for (const property in value) {
     if (property != 'areas' ) {
      o.push({fieldId : property, fieldValue : value[property]});
      } else {
       console.log('areas je ');
       console.log(value[property]);
       o.push({fieldId : property, categories : value[property]});

     }
    }
    let x = this.repositoryService.createMagazine(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        console.log(res);
        alert('You created new magazine successfully!');
        window.location.href = 'fillMagazine/' + this.processInstance;
      },
      err => {
        console.log('Error occured');
      }
    );
  }
}
