import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckFormatComponent } from './check-format.component';

describe('CheckFormatComponent', () => {
  let component: CheckFormatComponent;
  let fixture: ComponentFixture<CheckFormatComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CheckFormatComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckFormatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
