import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCoauthorComponent } from './add-coauthor.component';

describe('AddCoauthorComponent', () => {
  let component: AddCoauthorComponent;
  let fixture: ComponentFixture<AddCoauthorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddCoauthorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddCoauthorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
