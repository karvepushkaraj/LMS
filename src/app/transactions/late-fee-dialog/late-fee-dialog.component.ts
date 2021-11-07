import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-late-fee-dialog',
  templateUrl: './late-fee-dialog.component.html',
  styleUrls: ['./late-fee-dialog.component.scss']
})
export class LateFeeDialogComponent implements OnInit {

  constructor(
    private dialogRef: MatDialogRef<LateFeeDialogComponent>
  ) { }

  ngOnInit(): void {
  }

  lateFeeForm = new FormGroup({
    lateFees: new FormControl('20', Validators.required),
    receiptNumber: new FormControl('', Validators.required)
  });

  get lateFees():any{
    return this.lateFeeForm.get('lateFees');
  }

  get receiptNumber():any{
    return this.lateFeeForm.get('receiptNumber');
  }

  closeDialog():void{
    this.dialogRef.close();
  }

  payFees():void{
    if(this.lateFeeForm.invalid){
      return;
    }
    this.dialogRef.close(this.lateFeeForm.value);
  }
}
