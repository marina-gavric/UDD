import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckThemeComponent } from './check-theme.component';

describe('CheckThemeComponent', () => {
  let component: CheckThemeComponent;
  let fixture: ComponentFixture<CheckThemeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CheckThemeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckThemeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
