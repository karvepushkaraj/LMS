import { Component, OnInit, Inject } from '@angular/core';
import { SubscriptionPackage } from '../SubscriptionPackageModel';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-package-dialog',
  templateUrl: './package-dialog.component.html',
  styleUrls: ['./package-dialog.component.scss']
})
export class PackageDialogComponent implements OnInit {

  constructor(
    private dialogRef: MatDialogRef<PackageDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: SubscriptionPackage,
    private http: HttpClient
  ) {}

  ngOnInit(): void {}

  closeDialog(flag:boolean){
    this.dialogRef.close(flag);
  }

  deletePackage():void{
    const packageUrl:string = "http://localhost:8080/api/v1/lms/package/"+this.data.packageId;
    this.http.delete(packageUrl,{ responseType: 'text' }).subscribe(
      data=>{
        this.closeDialog(true);
      },
      error=>{
        console.log(error.error);
        this.closeDialog(false);
      }
    )
  }

}
