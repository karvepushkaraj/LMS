import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/shared/confirm-dialog/confirm-dialog.component';
import { LateFeeDialogComponent } from '../late-fee-dialog/late-fee-dialog.component';

@Component({
  selector: 'app-transaction-form',
  templateUrl: './transaction-form.component.html',
  styleUrls: ['./transaction-form.component.scss']
})
export class TransactionFormComponent implements OnInit {

  bookIdFlag:boolean = false;
  memberIdFlag:boolean = false;

  constructor(
    private http: HttpClient,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
  }

  bookTransactionForm = new FormGroup({
    bookId: new FormControl('',Validators.required),
    memberId: new FormControl('',Validators.required),
    lateFee: new FormGroup({
      lateFees: new FormControl(''),
      receiptNumber: new FormControl('')
    })
  });

  get bookId():any{
    return this.bookTransactionForm.get('bookId');
  }

  get memberId():any{
    return this.bookTransactionForm.get('memberId');
  }

  issueBook():void{
    if(this.bookTransactionForm.invalid){
      return;
    }
    const url = "http://localhost:8080/api/v1/lms/issue";
    this.http.post(url,this.bookTransactionForm.value,{ responseType: 'text' }).subscribe(
      data=>{
        this.dialog.open(ConfirmDialogComponent,{
          width: '500px',
          data : {
            errorFlag : false,
            title : "Transaction Successful",
            content : "Book Issued Successfully"
          }
        });
        this.bookTransactionForm.reset();
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
        this.bookTransactionForm.reset();
      }
    );
  }

  returnBook():void{
    if(this.bookTransactionForm.invalid){
      return;
    }
    const url = "http://localhost:8080/api/v1/lms/return";
    const errorMsg = "Late Fee should be Rs.20";
    this.http.post(url,this.bookTransactionForm.value,{ responseType: 'text' }).subscribe(
      data=>{
        this.dialog.open(ConfirmDialogComponent,{
          width: '500px',
          data : {
            errorFlag : false,
            title : "Transaction Successful",
            content : "Book Returned Successfully"
          }
        });
        this.bookTransactionForm.reset();
      },
      error=>{
        console.log(error.error);
        const message:string = JSON.parse(error.error).message;
        if(message.match(errorMsg)){
          const dialogRef = this.dialog.open(LateFeeDialogComponent);
          dialogRef.afterClosed().subscribe(
            data=>{
              if(data==null){
                this.dialog.open(ConfirmDialogComponent,{
                  width: '500px',
                  data : {
                    errorFlag : true,
                    title : "Oops...",
                    content : "Transaction Failed. Late Fees not paid."
                  }
                });
              }
              else{
                this.bookTransactionForm.get('lateFee')?.get('lateFees')?.setValue(data.lateFees);
                this.bookTransactionForm.get('lateFee')?.get('receiptNumber')?.setValue(data.receiptNumber);
                this.returnBook();
              }
            }
          );
        }
        else{
          this.dialog.open(ConfirmDialogComponent,{
            width: '500px',
            data : {
              errorFlag : true,
              title : "Oops...",
              content : message
            }
          });
          this.bookTransactionForm.reset();
        }
      }
    );
  }

}
