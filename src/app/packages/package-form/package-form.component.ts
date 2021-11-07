import { UpperCasePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, FormArray } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { LibrarySection } from 'src/app/sections/LibrarySectionModel';
import { LibrarySectionServiceService } from 'src/app/shared/services/library-section-service.service';
import { SubpkgServiceService } from 'src/app/shared/services/subpkg-service.service';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';
import { PackageDialogComponent } from '../package-dialog/package-dialog.component';
import { SubscriptionPackage } from '../SubscriptionPackageModel';


@Component({
  selector: 'app-package-form',
  templateUrl: './package-form.component.html',
  styleUrls: ['./package-form.component.scss']
})
export class PackageFormComponent implements OnInit {

  subPkg:SubscriptionPackage[] = [];
  libSections:LibrarySection[] = [];
  packageUrl:string = "http://localhost:8080/api/v1/lms/package/";
  displayedColumns: string[] = ["packageId","packageName","fees"];

  constructor(
    private http: HttpClient,
    private upperCasePipe: UpperCasePipe,
    private dialog: MatDialog,
    private libSecService:LibrarySectionServiceService,
    private subPkgService:SubpkgServiceService
    ) {}

  ngOnInit(): void {
    this.libSecService.getLibrarySections().subscribe(data=>this.libSections=data,error=>console.log(error.error));
    this.subPkgService.getSubscriptionPackages().subscribe(data=>this.subPkg=data,error=>console.log(error.error));
  }

  addPackageForm = new FormGroup({
    package : new FormGroup({
      packageName : new FormControl('',Validators.required),
      fees : new FormControl('',[Validators.required, Validators.min(1)])
    }),
    sections : new FormArray([
      new FormGroup({
        sectionId : new FormControl('', Validators.required),
        noOfBooks : new FormControl('1',[Validators.required, Validators.min(1)])
      })
    ])
  });

  get packageName():any { 
    return this.addPackageForm.get('package')?.get('packageName');
  }

  get fees():any { 
    return this.addPackageForm.get('package')?.get('fees');
  }

  get section():any { 
    return this.addPackageForm.get('section');
  }

  get noOfBooks():any { 
    return this.addPackageForm.get('noOfBooks');
  }

  get sections():FormArray {
    return this.addPackageForm.get('sections') as FormArray;
  }

  toUpper():void{
    const form = this.addPackageForm.get('package');
    form?.get('packageName')?.setValue(this.upperCasePipe.transform(form?.get('packageName')?.value));
  }

  addSection():void{
    this.sections.push(
      new FormGroup({
        sectionId : new FormControl('', Validators.required),
        noOfBooks : new FormControl('1',[Validators.required, Validators.min(1)])
      })
    );
  }

  removeSection(index : number): void{
    if(this.sections.length>1)
      this.sections.removeAt(index);
  }

  show(index:number):void{
    const dialogRef = this.dialog.open(PackageDialogComponent,{
      data : this.subPkg[index]
    });
    dialogRef.afterClosed().subscribe(result => {
      if(result){
        this.subPkgService.getSubscriptionPackages().subscribe(data=>this.subPkg=data,error=>console.log(error.error));
      }
    });
  }

  createPackage():void{
    if(this.addPackageForm.invalid){
      return;
    }
    console.log(this.addPackageForm.value);
    this.http.post(this.packageUrl,this.addPackageForm.value,{ responseType: 'text' }).subscribe(
      data=>{
        this.dialog.open(ConfirmDialogComponent,{
          width: '500px',
          data : {
            errorFlag : false,
            title : "Transaction Successful",
            content : "Subscription Package Created Successfully"
          }
        });
        this.subPkgService.getSubscriptionPackages().subscribe(data=>this.subPkg=data,error=>console.log(error.error));
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
