import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { SubscriptionPackage } from 'src/app/packages/SubscriptionPackageModel';
import { ConfirmDialogComponent } from 'src/app/shared/confirm-dialog/confirm-dialog.component';
import { SubpkgServiceService } from 'src/app/shared/services/subpkg-service.service';

@Component({
  selector: 'app-subscription-form',
  templateUrl: './subscription-form.component.html',
  styleUrls: ['./subscription-form.component.scss']
})
export class SubscriptionFormComponent implements OnInit {

  subPkg:SubscriptionPackage[] = [];

  constructor(
    private http: HttpClient,
    private dialog: MatDialog,
    private subPkgService: SubpkgServiceService
  ) { }

  ngOnInit(): void {
    this.subPkgService.getSubscriptionPackages().subscribe(data=>this.subPkg=data,error=>console.log(error.error));
  }

  subscriptionForm = new FormGroup({
    memberId: new FormControl('',Validators.required),
    packageId: new FormControl('',Validators.required),
    subscriptionFee: new FormGroup({
      fees: new FormControl('',Validators.required),
      receiptNumber: new FormControl('',Validators.required)
    })
  });

  get memberId():any{
    return this.subscriptionForm.get('memberId');
  }

  get packageId():any{
    return this.subscriptionForm.get('packageId');
  }

  get fees():any{
    return this.subscriptionForm.get('subscriptionFee')?.get('fees');
  }

  get receiptNumber():any{
    return this.subscriptionForm.get('subscriptionFee')?.get('receiptNumber');
  }

  setFees(index:number):void{
    this.subscriptionForm.get('subscriptionFee')?.get('fees')?.setValue(this.subPkg[index].fees);
  }

  addSubscription():void{
    if(this.subscriptionForm.invalid){
      return;
    }   
    const url = "http://localhost:8080/api/v1/lms/subscribe/";
    this.http.post(url,this.subscriptionForm.value,{ responseType: 'text' }).subscribe(
      data=>{
        this.dialog.open(ConfirmDialogComponent,{
          width: '500px',
          data : {
            errorFlag : false,
            title : "Transaction Successful",
            content : "Subscription Added Successfully"
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

}
