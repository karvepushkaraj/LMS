import { DatePipe, UpperCasePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { LibrarySection } from 'src/app/sections/LibrarySectionModel';
import { ConfirmDialogComponent } from 'src/app/shared/confirm-dialog/confirm-dialog.component';
import { LibrarySectionServiceService } from 'src/app/shared/services/library-section-service.service';

@Component({
  selector: 'app-new-book-form',
  templateUrl: './new-book-form.component.html',
  styleUrls: ['./new-book-form.component.scss']
})
export class NewBookFormComponent implements OnInit {

  libSections:LibrarySection[] = [];
  bookUrl:string = "http://localhost:8080/api/v1/lms/books/";
  bookCopyFlag:boolean = false;

  constructor(
    private http: HttpClient,
    private upperCasePipe: UpperCasePipe,
    private datePipe: DatePipe,
    private dialog: MatDialog,
    private libSecService:LibrarySectionServiceService,
  ) {}

  ngOnInit(): void {
    this.libSecService.getLibrarySections().subscribe(data=>this.libSections=data,error=>console.log(error.error));
    this.updateForm();
  }

  addBookForm = new FormGroup({
    sectionId: new FormControl('',Validators.required),
    bookTitle: new FormGroup({
      titleId: new FormControl('',Validators.required),
      title: new FormControl('',Validators.required),
      author: new FormControl(''),
      publication: new FormControl(''),
    }),
    bookCopy: new FormGroup({
      price: new FormControl(''),
      purchaseDate: new FormControl('')
    })
  });

  get sectionId():any{
    return this.addBookForm.get('sectionId');
  }

  get titleId():any{
    return this.addBookForm.get('bookTitle')?.get('titleId');
  }

  get title():any{
    return this.addBookForm.get('bookTitle')?.get('title');
  }

  get author():any{
    return this.addBookForm.get('bookTitle')?.get('author');
  }

  get publication():any{
    return this.addBookForm.get('bookTitle')?.get('publication');
  }

  get price():any{
    return this.addBookForm.get('bookCopy')?.get('price');
  }

  get purchaseDate():any{
    return this.addBookForm.get('bookCopy')?.get('purchaseDate');
  }

  toUpper():void{
    let form = this.addBookForm.get('bookTitle');
    form?.get('title')?.setValue(this.upperCasePipe.transform(form?.get('title')?.value));
    form?.get('author')?.setValue(this.upperCasePipe.transform(form?.get('author')?.value));
    form?.get('publication')?.setValue(this.upperCasePipe.transform(form?.get('publication')?.value));
  }

  updateForm():void{
    this.addBookForm.reset();
    this.addBookForm.get('bookCopy')?.get('purchaseDate')?.setValue(new Date());
    if(this.bookCopyFlag){
      this.addBookForm.get('sectionId')?.disable();
      this.addBookForm.get('bookTitle')?.get('titleId')?.enable();
      this.addBookForm.get('bookTitle')?.get('title')?.disable();
      this.addBookForm.get('bookTitle')?.get('author')?.disable();
      this.addBookForm.get('bookTitle')?.get('publication')?.disable();
    }
    else{
      this.addBookForm.get('sectionId')?.enable();
      this.addBookForm.get('bookTitle')?.get('titleId')?.disable();
      this.addBookForm.get('bookTitle')?.get('title')?.enable();
      this.addBookForm.get('bookTitle')?.get('author')?.enable();
      this.addBookForm.get('bookTitle')?.get('publication')?.enable();
    }
  }

  addBook():void{ 
    if(this.addBookForm.invalid){
      return;
    }
    const body = this.addBookForm.value;
    body.bookCopy.purchaseDate = this.datePipe.transform(body.bookCopy.purchaseDate,'dd-MM-yyyy');
    this.http.post(this.bookUrl,body,{ responseType: 'text' }).subscribe(
      data=>{
        this.dialog.open(ConfirmDialogComponent,{
          width: '500px',
          data : {
            errorFlag : false,
            title : "Transaction Successful",
            content : data
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

}
