import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, FormBuilder, NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../services/user-service/user.service';
import { RepositoryService } from '../services/repository-service/repository.service';
import { AreaService } from '../services/areas-service/area.service';
import { MagazineService } from '../services/magazine-service/magazine.service';

@Component({
  selector: 'app-fill-coauthor',
  templateUrl: './fill-coauthor.component.html',
  styleUrls: ['./fill-coauthor.component.css']
})
export class FillCoauthorComponent implements OnInit {
  private formFieldsDto = null;
  private formFields = [];
  // tslint:disable-next-line:variable-name
  private processInstance = '';
  private enumValues = [];
  private areas = [];
  private processId = '';
  // tslint:disable-next-line:max-line-length
  constructor(private route: ActivatedRoute, private magazineService: MagazineService, private areaService: AreaService, private repositoryService: RepositoryService) { 
    this.route.params.subscribe( params => {this.processId = params.process_id; });
    const x = magazineService.getFillText(this.processId);
    x.subscribe(
      res => {
        console.log(res);
        console.log('FillingCoauthorsProcess');
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        console.log(this.formFields);
        this.processInstance = res.processInstanceId;
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
    const x = this.magazineService.fillCA(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        if (res == true) {
          window.location.href = 'addCoauthor/' + this.processId;
        }
        
       // window.location.href = '';
      },
      err => {
        console.log('Error occured');
      }
    );
    }

}
