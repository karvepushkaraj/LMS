import { UpperCasePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-new-member-form',
  templateUrl: './new-member-form.component.html',
  styleUrls: ['./new-member-form.component.scss']
})
export class NewMemberFormComponent implements OnInit {

  memberUrl:string = "http://localhost:8080/api/v1/lms/member/";
  requestType:string="add";
  addFormFlag:boolean = true;
  deleteFormFlag:boolean = false;

  constructor(
    private http: HttpClient,
    private upperCasePipe: UpperCasePipe,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
  }

  addMemberForm = new FormGroup({
      member : new FormGroup({
        name : new FormControl('',Validators.required),
        mobileNumber : new FormControl('',Validators.required),
        emailId : new FormControl('',Validators.email)
      }),
      deposite : new FormGroup({
        deposite : new FormControl('500',Validators.required),
        receiptNumber : new FormControl('',Validators.required)
      })
    });
  
  deleteMemberForm = new FormGroup({
    memberId : new FormControl('',Validators.required),
    deposite : new FormGroup({
      deposite : new FormControl('500',Validators.required),
      receiptNumber : new FormControl('',Validators.required)
    })
  });

  get memberId():any {
    return this.deleteMemberForm.get('memberId');
  }

  get name():any {
    return this.addMemberForm.get('member')?.get('name');
  }

  get mobileNumber():any {
    return this.addMemberForm.get('member')?.get('mobileNumber');
  }

  get emailId():any {
    return this.addMemberForm.get('member')?.get('emailId');
  }

  get deposite():any {
    if(this.addFormFlag){
      return this.addMemberForm.get('deposite')?.get('deposite');
    }
    return this.deleteMemberForm.get('deposite')?.get('deposite');
  }

  get receiptNumber():any {
    if(this.addFormFlag){
      return this.addMemberForm.get('deposite')?.get('receiptNumber');
    }
    return this.deleteMemberForm.get('deposite')?.get('receiptNumber');
  }

  setRequestType():void{
    this.reset();
    if(this.requestType=="add"){
      this.addFormFlag = true;
    }
    else{
      this.deleteFormFlag = true;
    }
  }

  toUpper(form:FormGroup):void{
    form.get('member')?.get('name')?.setValue(this.upperCasePipe.transform(form.get('member')?.get('name')?.value));
  }

  addMember():any {
    console.log("Add Member Form Submitted");
    if(this.addMemberForm.invalid){
      return;
    }
    const url = this.memberUrl + "new";
    this.http.post(url,this.addMemberForm.value,{ responseType: 'text' }).subscribe(
      data=>{
        this.dialog.open(ConfirmDialogComponent,{
          width: '500px',
          data : {
            errorFlag : false,
            title : "Transaction Successful",
            content : data
          }
        });
      },
      error=>{
        console.log(error.error);
        this.dialog.open(ConfirmDialogComponent,{
          width: '500px',
          data : {
            errorFlag : true,
            title : "Oops...",
            content : JSON.parse(error.error).message
          }
        });
      }
    );
  }

  deleteMember():any {
    if(this.deleteMemberForm.invalid){
      return;
    }
    const url = this.memberUrl + "delete";
    this.http.post(url,this.deleteMemberForm.value,{ responseType: 'text' }).subscribe(
      data=>{
        this.dialog.open(ConfirmDialogComponent,{
          width: '500px',
          data : {
            errorFlag : false,
            title : "Transaction Successful",
            content : "Member Deactivated Successfully"
          }
        })
      },
      error=>{
        console.log(error.error);
        this.dialog.open(ConfirmDialogComponent,{
          width: '500px',
          data : {
            errorFlag : true,
            title : "Oops...",
            content : JSON.parse(error.error).message
          }
        });
      }
    );
  }

  reset():void{
    this.addFormFlag = false;
    this.deleteFormFlag = false;
    this.addMemberForm.reset();
    this.deleteMemberForm.reset();
    this.addMemberForm.get('deposite')?.get('deposite')?.setValue('500');
    this.deleteMemberForm.get('deposite')?.get('deposite')?.setValue('500');
  }

}
