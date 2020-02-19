import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FillTextComponent } from './fill-text.component';

describe('FillTextComponent', () => {
  let component: FillTextComponent;
  let fixture: ComponentFixture<FillTextComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FillTextComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FillTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
