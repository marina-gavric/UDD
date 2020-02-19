import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user-service/user.service';
import { RepositoryService } from '../services/repository-service/repository.service';
import { AreaService } from '../services/areas-service/area.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  // tslint:disable-next-line:variable-name
  private repeated_password = '';
  private categories = [];
  private formFieldsDto = null;
  private formFields = [];
  // tslint:disable-next-line:variable-name
  private choosen_category = -1;
  private processInstance = '';
  private enumValues = [];
  private tasks = [];
  private reviewerChecked = false;

  constructor(private userService: UserService, private areaService: AreaService, private repositoryService: RepositoryService) {
    const x = repositoryService.startProcess();

    x.subscribe(
      res => {
        console.log(res);
        console.log('Ispis rezultata');
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        console.log(this.formFields);

        this.processInstance = res.processInstanceId;
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
    const o = new Array();
    // tslint:disable-next-line:forin
    for (const property in value) {
     console.log('property');
     console.log(property);
     console.log('value[property] je');
     console.log(value[property]);
     if (property != 'branches' ) {
      o.push({fieldId : property, fieldValue : value[property]});
      } else {
       console.log('branches je ');
       console.log(value[property]);
       o.push({fieldId : property, categories : value[property]});

     }
     console.log('niz za slanje izgleda');
     console.log(o);
     // tslint:disable-next-line:comment-format
     //fieldId da li je name,surname,password...
     // tslint:disable-next-line:comment-format
     //value[property] za name je marina...
    }

    console.log(o);
    let x = this.userService.registerUser(o, this.formFieldsDto.taskId);
    console.log('Pre subscribe');
    x.subscribe(
      res => {
        console.log(res);
        alert('You registered successfully!');
        window.location.href = 'login';
      },
      err => {
        console.log('Error occured');
      }
    );
  }


}
