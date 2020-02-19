import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, FormBuilder, NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../services/user-service/user.service';
import { RepositoryService } from '../services/repository-service/repository.service';
import { AreaService } from '../services/areas-service/area.service';
import { MagazineService } from '../services/magazine-service/magazine.service';

@Component({
  selector: 'app-add-coauthor',
  templateUrl: './add-coauthor.component.html',
  styleUrls: ['./add-coauthor.component.css']
})
export class AddCoauthorComponent implements OnInit {
  private formFieldsDto = null;
  private formFields = [];
  // tslint:disable-next-line:variable-name
  private processInstance = '';
  private enumValues = [];
  private areas = [];
  private processId = '';
  private correct = false;
  private more = false;
  // tslint:disable-next-line:max-line-length
  constructor(private route: ActivatedRoute, private magazineService: MagazineService, private areaService: AreaService, private repositoryService: RepositoryService) {
    this.route.params.subscribe( params => {this.processId = params.process_id; });
    const x = magazineService.getFillText(this.processId);
    x.subscribe(
      res => {
        console.log(res);
        console.log('AddingCoauthorsProcess');
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
    console.log('Id procesa je '+ this.processId);
    // tslint:disable-next-line:forin
    for (const property in value) {
      console.log(value[property]);
      if ( property == 'more') {
         this.more = value[property];
      }
      o.push({fieldId : property, fieldValue : value[property]});
    }
    console.log(o);
    const x = this.magazineService.addingCA(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        console.log( res );
        if (res == false) {
          window.location.href = 'fillCoauthor/' + this.processId;
        } else if (res == true && this.more == true) {
          window.location.href = 'addCoauthor/' + this.processId;
        } else {
          window.location.href = '';
        }
        console.log('Zavrseno popunjavanje co-authora');
      },
      err => {
        console.log('Error occured');
      }
    );
    }

}
