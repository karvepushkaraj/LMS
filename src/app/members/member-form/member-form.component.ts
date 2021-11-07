import { UpperCasePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/shared/confirm-dialog/confirm-dialog.component';
import { MemberDialogComponent } from '../member-dialog/member-dialog.component';
import { Member } from '../MemberModel';

@Component({
  selector: 'app-member-form',
  templateUrl: './member-form.component.html',
  styleUrls: ['./member-form.component.scss']
})
export class MemberFormComponent implements OnInit {

  memberUrl:string = "http://localhost:8080/api/v1/lms/member/";
  requestType:string="get";
  getFormFlag:boolean = true;
  updateFormFlag:boolean = false;

  constructor(
    private http: HttpClient,
    private upperCasePipe: UpperCasePipe,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
  }

  getMemberForm = new FormGroup({
    memberId : new FormControl('',Validators.required)
  });

  updateMemberForm = new FormGroup({
    memberId : new FormControl({value:'',disabled:true}),
    name : new FormControl('',Validators.required),
    mobileNumber : new FormControl('',Validators.required),
    emailId : new FormControl('',Validators.email)
  });

  get memberId():any {
    if(this.getFormFlag){
      return this.getMemberForm.get('memberId');
    }
    return this.updateMemberForm.get('memberId');
  }

  get name():any {
    return this.updateMemberForm.get('name');
  }

  get mobileNumber():any {
    return this.updateMemberForm.get('mobileNumber');
  }

  get emailId():any {
    return this.updateMemberForm.get('emailId');
  } 

  setRequestType():void{
    this.reset();
    this.getFormFlag = true;
  }

  toUpper(form:FormGroup):void{
    form.get('name')?.setValue(this.upperCasePipe.transform(form.get('name')?.value));
  }

  getMember():void {
    console.log("Get Member Form Submitted");
    if(this.getMemberForm.invalid){
      return;
    }
    const memberId = this.getMemberForm.get('memberId')?.value;
    const url = this.memberUrl + "?id=" + memberId;
    this.http.get<Member>(url).subscribe(
      data=>{
        console.log(data);
        if(this.requestType=="get"){
          this.dialog.open(MemberDialogComponent,{
            data : data
          });
        }
        else {
          this.updateMemberForm.get('memberId')?.setValue(data.memberId);
          this.updateMemberForm.get('name')?.setValue(data.name);
          this.updateMemberForm.get('mobileNumber')?.setValue(data.mobileNumber);
          this.updateMemberForm.get('emailId')?.setValue(data.emailId);
          this.getFormFlag = false;
          this.updateFormFlag = true;
        }
      },
      error=>{
        console.log(error.error);
        this.dialog.open(ConfirmDialogComponent,{
          width: '500px',
          data : {
            errorFlag : true,
            title : "Oops...",
            content : error.error.message
          }
        }); 
      }
    );
  }

  updateMember():void {
    if(this.updateMemberForm.invalid){
      return;
    }
    this.http.put(this.memberUrl,this.updateMemberForm.getRawValue(),{ responseType: 'text' }).subscribe(
      data=>{
        this.dialog.open(ConfirmDialogComponent,{
          width: '500px',
          data : {
            errorFlag : false,
            title : "Transaction Successful",
            content : "Member Updated Successfully."
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

  reset():void{
    this.getFormFlag = false;
    this.updateFormFlag = false;
    this.getMemberForm.reset();
    this.updateMemberForm.reset();
  }

}
