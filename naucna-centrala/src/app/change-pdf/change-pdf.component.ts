import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repository-service/repository.service';
import { ActivatedRoute } from '@angular/router';
import { MagazineService } from '../services/magazine-service/magazine.service';

@Component({
  selector: 'app-change-pdf',
  templateUrl: './change-pdf.component.html',
  styleUrls: ['./change-pdf.component.css']
})
export class ChangePdfComponent implements OnInit {
  private formFieldsDto = null;
  private formFields = [];
  private taskId = '';
  private fileUpload: File;
  private o = new Array();

  constructor(private magazineService: MagazineService, private repositoryService: RepositoryService, private route: ActivatedRoute) {
    this.route.params.subscribe( params => {this.taskId = params.task_id; });
    console.log('In change pdf');
    const x = repositoryService.loadTask(this.taskId);
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
   fileUploads(event) {
    this.fileUpload = event.target.files[0];
    console.log(this.fileUpload);
  }
  ngOnInit() {
  }
  onSubmit(value, form) {
    this.magazineService.uploadFile(this.fileUpload).subscribe(
      data => {
        console.log('vratili se ');
        console.log('povratna ' + data.text());
        this.o.push({fieldId : 'newPdf', fieldValue : data.text()});
        console.log(form);
        console.log(value);
        console.log('Pritisnut submit');
        this.getForm(value, form);
      });
    }
    getForm(value, form) {
      console.log('gett forme');
      // tslint:disable-next-line:forin
      for (const property in value) {
        if (property != 'newPdf') {
          this.o.push({fieldId : property, fieldValue : value[property]});
        }
      }
      console.log(this.o);
      const x = this.magazineService.changePdf(this.o, this.formFieldsDto.taskId);
      x.subscribe(
        res => {
          console.log(res);
          window.location.href = '';
               },
        err => {
          console.log('Error occured');
        }
      );
    }

}
