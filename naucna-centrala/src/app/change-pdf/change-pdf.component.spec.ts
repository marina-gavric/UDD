import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangePdfComponent } from './change-pdf.component';

describe('ChangePdfComponent', () => {
  let component: ChangePdfComponent;
  let fixture: ComponentFixture<ChangePdfComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChangePdfComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangePdfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
