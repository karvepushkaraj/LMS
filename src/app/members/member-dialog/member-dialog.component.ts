import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Member } from '../MemberModel';

@Component({
  selector: 'app-member-dialog',
  templateUrl: './member-dialog.component.html',
  styleUrls: ['./member-dialog.component.scss']
})
export class MemberDialogComponent implements OnInit {

  style = "text-success";

  constructor(
    private dialogRef: MatDialogRef<MemberDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Member
  ) {}

  ngOnInit(): void {
    if(this.data.status=="EXPIRED"){
      this.style = "text-danger";
    }
  }

  closeDialog(){
    this.dialogRef.close();
  }

}
