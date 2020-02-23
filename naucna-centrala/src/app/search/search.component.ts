import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repository-service/repository.service';
import { Field } from '../model/Field';
import { TextDTO } from '../model/TextDTO';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  private field: Field;
  conditions: Field[] = new Array();
  result = [];
  searchDone = false;
  constructor(private repositoryService: RepositoryService) { 
    this.field = new Field();
  }

  ngOnInit() {
  }
  onSubmit() {
    console.log('pritisnutSubmit, uslovi su ' + this.conditions);
    // tslint:disable-next-line:prefer-const
    let d = this.repositoryService.searchTexts(this.conditions);
    d.subscribe(
      data => {
        console.log(data);
        this.result = data;
        console.log(this.result);
        this.searchDone = true;
       },
       err => {
         console.log('there is an error');
       }
    );
  }
  addCondition() {
    console.log('add condition');
    console.log(this.field);
    const f: Field = new Field();
    f.field = this.field.field;
    f.operator = this.field.operator;
    f.text = this.field.text;
    f.type = this.field.type;
    this.field = new Field();
    this.conditions.push(f);
    console.log(this.conditions);
  }
  searchAgain() {
    console.log('u searchAgain');
    this.field = new Field();
    this.conditions = new Array();
    this.searchDone = false;
  }
}
