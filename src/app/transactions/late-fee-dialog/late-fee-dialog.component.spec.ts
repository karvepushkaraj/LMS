import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LateFeeDialogComponent } from './late-fee-dialog.component';

describe('LateFeeDialogComponent', () => {
  let component: LateFeeDialogComponent;
  let fixture: ComponentFixture<LateFeeDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LateFeeDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LateFeeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
