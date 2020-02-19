import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewTextComponent } from './new-text.component';

describe('NewTextComponent', () => {
  let component: NewTextComponent;
  let fixture: ComponentFixture<NewTextComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewTextComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
