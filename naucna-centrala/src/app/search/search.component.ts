import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repository-service/repository.service';
import { Field } from '../model/Field';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  private field: Field;

  constructor(private repositoryService: RepositoryService) { 
    this.field = new Field();
  }

  ngOnInit() {
  }
  onSubmit(){
    console.log('pritisnutSubmit');
    console.log(this.field);
    console.log(this.field.field);
  }
}
