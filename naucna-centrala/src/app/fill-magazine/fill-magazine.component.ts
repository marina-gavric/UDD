import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repository-service/repository.service';
import { AreaService } from '../services/areas-service/area.service';
import { MagazineService } from '../services/magazine-service/magazine.service';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../services/user-service/user.service';

@Component({
  selector: 'app-fill-magazine',
  templateUrl: './fill-magazine.component.html',
  styleUrls: ['./fill-magazine.component.css']
})
export class FillMagazineComponent implements OnInit {
  private formFieldsDto = null;
  private formFields = [];
  private processId = '';
  private username = '';
  private reviewers = [];
  private editors = [];
  // tslint:disable-next-line:max-line-length
  constructor(private route: ActivatedRoute, private userService: UserService, private magazineService: MagazineService, private areaService: AreaService, private repositoryService: RepositoryService) { 
    this.route.params.subscribe( params => {this.processId = params.process_id; });
    
    const x = repositoryService.getTaskMag(this.processId);
    x.subscribe(
      res => {
        console.log(res);
        console.log('Ispis rezultata');
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        console.log(this.formFields);
        this.userService.loadReviewers().subscribe(
          pom => {
            console.log('Ispis recenzenata');
            console.log(pom);
            this.reviewers = pom;
           }
        );
        this.userService.loadEditors().subscribe(
          red => {
            console.log('Ispis editora');
            console.log(red);
            this.editors = red;
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
    console.log('ispisivanje u fillMagazinu');

    // tslint:disable-next-line:forin
    for (const property in value) {
     
     if (property != 'reviewersl' && property != 'editorsl' ) {
       o.push({fieldId : property, fieldValue : value[property]});
      } else {
       o.push({fieldId : property, categories : value[property]});

     }
     console.log('niz za slanje izgleda');
     console.log(o);
    }

    let x = this.magazineService.updateMagazine(o, this.formFieldsDto.taskId);
    console.log('Pre subscribe');
    x.subscribe(
      res => {
        console.log(res);
        alert('You  have successfully updated magazine!');
        window.location.href = 'login';
      },
      err => {
        console.log('Error occured');
      }
    );
  }
}
