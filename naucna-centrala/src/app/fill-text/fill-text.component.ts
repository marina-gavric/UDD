import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, FormBuilder, NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../services/user-service/user.service';
import { RepositoryService } from '../services/repository-service/repository.service';
import { AreaService } from '../services/areas-service/area.service';
import { MagazineService } from '../services/magazine-service/magazine.service';


@Component({
  selector: 'app-fill-text',
  templateUrl: './fill-text.component.html',
  styleUrls: ['./fill-text.component.css']
})
export class FillTextComponent implements OnInit {
  private formFieldsDto = null;
  private formFields = [];
  // tslint:disable-next-line:variable-name
  private processInstance = '';
  private enumValues = [];
  private areas = [];
  private processId = '';
  private more = false;
  private fileUpload: File;
  private o = new Array();
  private choseF = false;
  // tslint:disable-next-line:max-line-length
  constructor(private route: ActivatedRoute, private magazineService: MagazineService, private areaService: AreaService, private repositoryService: RepositoryService) { 
    this.route.params.subscribe( params => {this.processId = params.process_id; });
    const x = magazineService.getFillText(this.processId);
    x.subscribe(
      res => {
        console.log(res);
        console.log('GetTextProcess');
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        console.log(this.formFields);

        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) => {
          if ( field.type.name === 'enum') {
            this.areas = Object.keys(field.type.values);
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
  fileUploads(event) {
    this.choseF = true;
    this.fileUpload = event.target.files[0];
    console.log(this.fileUpload);
  }
  onSubmit(value, form) {
    var validForm = true;
    if (this.choseF==false) {
      validForm = false;
    }
    // tslint:disable-next-line:forin
    for (const property in value) {
      if (property === 'summary') {
        if ( value[property] === '') {
          validForm = false;
          break;
        }
      }
      if (property === 'keywords') {
        if ( value[property] === '') {
          validForm = false;
          break;
        }      }
      if (property === 'title') {
        if ( value[property] === '') {
          validForm = false;
          break;
        }      }
      if (property === 'areas') {
            if ( value[property] === '') {
              validForm = false;
              break;
            }
      }
    }
    if ( validForm == true) {
      this.magazineService.uploadFile(this.fileUpload).subscribe(
        data => {
          console.log('vratili se ');
          console.log('povratna ' + data.text());
          this.o.push({fieldId : 'file', fieldValue : data.text()});
          console.log(form);
          console.log(value);
          console.log('Pritisnut submit');
          this.getForm(value, form);
        });
    } else {
      alert('You need to fill out all the fields');
    }
    }
    getForm(value, form) {
      console.log('gett forme');
      // tslint:disable-next-line:forin
      for (const property in value) {
        if (property == 'num') {
          this.more = value[property];
        }
        if (property != 'file') {
          this.o.push({fieldId : property, fieldValue : value[property]});
        }
      }
      console.log(this.o);
      const x = this.magazineService.createText(this.o, this.formFieldsDto.taskId);
      x.subscribe(
        res => {
          console.log(res);
          if (this.more) {
            window.location.href = 'addCoauthor/' + this.processId;
           } else {
            window.location.href = '';
          }
        },
        err => {
          console.log('Error occured');
        }
      );
    }

}
