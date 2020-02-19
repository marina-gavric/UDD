import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckingMagazineComponent } from './checking-magazine.component';

describe('CheckingMagazineComponent', () => {
  let component: CheckingMagazineComponent;
  let fixture: ComponentFixture<CheckingMagazineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CheckingMagazineComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckingMagazineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
