import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FillCoauthorComponent } from './fill-coauthor.component';

describe('FillCoauthorComponent', () => {
  let component: FillCoauthorComponent;
  let fixture: ComponentFixture<FillCoauthorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FillCoauthorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FillCoauthorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
