import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FillMagazineComponent } from './fill-magazine.component';

describe('FillMagazineComponent', () => {
  let component: FillMagazineComponent;
  let fixture: ComponentFixture<FillMagazineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FillMagazineComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FillMagazineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
