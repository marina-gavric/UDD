import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, FormBuilder, NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../services/user-service/user.service';
import { RepositoryService } from '../services/repository-service/repository.service';
import { AreaService } from '../services/areas-service/area.service';
import { MagazineService } from '../services/magazine-service/magazine.service';

@Component({
  selector: 'app-select-reviewer',
  templateUrl: './select-reviewer.component.html',
  styleUrls: ['./select-reviewer.component.css']
})
export class SelectReviewerComponent implements OnInit {
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
    const x = magazineService.getRevText(this.processId);
    x.subscribe(
      res => {
        console.log(res);
        console.log('SelectReviewerProcess');
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        console.log(this.formFields);
        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) => {
          if ( field!='undefined') { 
            console.log('Nije undefines');
            if ( field.type.name === 'enum') {
              this.enumValues = Object.keys(field.type.values);
            }
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
        if ( property == 'listrev') {
          o.push({fieldId : property, categories : value[property]});
        } else {
          o.push({fieldId : property, fieldValue : value[property]});
        }
    }
    console.log(o);
    const x = this.magazineService.saveRev(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {
          window.location.href = '';
      },
      err => {
        console.log('Error occured');
      }
    );
    }


}
/*  <div *ngFor="let field of formFields">                  
                  <mat-form-field *ngIf="field.type.name=='string'">
                      <input placeholder={{field.label}} type="text" matInput name={{field.id}} ngModel  *ngIf="field.type.name=='string'" >
                  </mat-form-field>  
                    </div>
                    <label *ngIf="field.type.name=='enum'">Select reviewers:</label>
                   
                    <mat-select name="{{field.id}}"   ngModel *ngIf="field.type.name=='enum'">
                            <mat-option *ngFor="let mag of enumValues" value={{mag}}>
                                {{field.type.values[mag]}}
                            </mat-option>
                    </mat-select>
             */