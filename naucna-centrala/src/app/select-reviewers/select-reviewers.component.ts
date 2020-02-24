import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, FormBuilder, NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../services/user-service/user.service';
import { RepositoryService } from '../services/repository-service/repository.service';
import { AreaService } from '../services/areas-service/area.service';
import { MagazineService } from '../services/magazine-service/magazine.service';

@Component({
  selector: 'app-select-reviewers',
  templateUrl: './select-reviewers.component.html',
  styleUrls: ['./select-reviewers.component.css']
})
export class SelectReviewersComponent implements OnInit {
  private enumValues = [];
  private result = [];

  private processId = '';
  constructor(private route: ActivatedRoute, private magazineService: MagazineService, private repositoryService: RepositoryService) { 
    this.route.params.subscribe( params => {this.processId = params.process_id; });

    let d = this.repositoryService.loadReviewers(this.processId);
    d.subscribe(
      data => {
        console.log(data);
        this.enumValues = data;
        console.log(this.enumValues);
       },
       err => {
         console.log('there is an error');
       }
    );
  }

  ngOnInit() {
  }
  onSubmit(value, form) {
   console.log('pritisnut submit');
   const o = new Array();

   // tslint:disable-next-line:forin
   for (const property in value) {
    console.log('property');
    console.log(property);
    if (property == 'lista') {
      this.result = value[property];
      console.log('rez je');
      console.log(this.result);
      o.push({fieldId : property, categories : value[property]});

    }
    console.log('value[property] je');
    console.log(value[property]);
    console.log('niz za slanje izgleda');
    let x = this.repositoryService.chooseReviewers(o, this.processId);
    console.log('Pre subscribe');
    x.subscribe(
      res => {
        console.log('Uspesno');
        window.location.href = 'login';
      },
      err => {
        console.log('Error occured');
      }
    );
  }
  }
  scienceFilter() {
    console.log('science filter');
    let d = this.repositoryService.loadReviewersScience(this.processId);
    d.subscribe(
      data => {
        console.log(data);
        this.enumValues = data;
        console.log(this.enumValues);
       },
       err => {
         console.log('there is an error');
       }
    );

  }
  locationFilter() {
    console.log('location filter');
    let d = this.repositoryService.loadReviewersLocation(this.processId);
    d.subscribe(
      data => {
        console.log(data);
        this.enumValues = data;
        console.log(this.enumValues);
       },
       err => {
         console.log('there is an error');
       }
    );
  }
  moreFilter() {
    console.log('more filter');
  }
}
