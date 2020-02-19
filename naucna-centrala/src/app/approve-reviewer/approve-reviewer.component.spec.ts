import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproveReviewerComponent } from './approve-reviewer.component';

describe('ApproveReviewerComponent', () => {
  let component: ApproveReviewerComponent;
  let fixture: ComponentFixture<ApproveReviewerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApproveReviewerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproveReviewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
