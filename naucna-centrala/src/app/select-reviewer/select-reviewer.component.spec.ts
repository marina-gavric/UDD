import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectReviewerComponent } from './select-reviewer.component';

describe('SelectReviewerComponent', () => {
  let component: SelectReviewerComponent;
  let fixture: ComponentFixture<SelectReviewerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectReviewerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectReviewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
