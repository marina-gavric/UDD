import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectReviewersComponent } from './select-reviewers.component';

describe('SelectReviewersComponent', () => {
  let component: SelectReviewersComponent;
  let fixture: ComponentFixture<SelectReviewersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectReviewersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectReviewersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
