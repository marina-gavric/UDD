import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user-service/user.service';
import { RepositoryService } from '../services/repository-service/repository.service';
import { MagazineService } from '../services/magazine-service/magazine.service';
import { FormControl, FormGroup, Validators, FormBuilder, NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-pay',
  templateUrl: './pay.component.html',
  styleUrls: ['./pay.component.css']
})
export class PayComponent implements OnInit {
  private formFieldsDto = null;
  private formFields = [];
  // tslint:disable-next-line:variable-name
  private processInstance = '';
  private enumValues = [];
  private areas = [];
  private processId = '';
  constructor(private route: ActivatedRoute, private magazineService: MagazineService, private repositoryService: RepositoryService) {
    this.route.params.subscribe( params => {this.processId = params.process_id; });
    const x = magazineService.getFillText(this.processId);
    x.subscribe(
      res => {
        console.log(res);
        console.log('SelectReviewerProcess');
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
    console.log('Pritisnut submit paying');

    // tslint:disable-next-line:forin
    for (const property in value) {
      console.log('vrednost je ' + value[property]);
      o.push({fieldId : property, fieldValue : value[property]});
    }
    console.log(o);
    const x = this.magazineService.savePayment(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
        if ( res == false ) {
          window.location.href = '';
        } else {
          window.location.href = 'fillText/' + this.processInstance;
        }
      },
      err => {
        console.log('Error occured');
      }
    );
    }


}
