import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewMemberFormComponent } from './new-member-form.component';

describe('NewMemberFormComponent', () => {
  let component: NewMemberFormComponent;
  let fixture: ComponentFixture<NewMemberFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewMemberFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewMemberFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
