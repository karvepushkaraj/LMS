import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/shared/confirm-dialog/confirm-dialog.component';
import { Book, BookCopy } from '../BookModel';

@Component({
  selector: 'app-book-form',
  templateUrl: './book-form.component.html',
  styleUrls: ['./book-form.component.scss']
})
export class BookFormComponent implements OnInit {

  bookCopy:BookCopy = new BookCopy(0,0,new Date(),0);
  book:Book = new Book(0,"","","","",[]);
  bookUrl:string = "http://localhost:8080/api/v1/lms/books/";
  displayedColumns: string[] = ["copyId","price","purchaseDate","delete"];
  outputFlag:boolean = false;
  bookCopyFlag:boolean = false;

  constructor(
    private http: HttpClient,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
  }

  getBookForm = new FormGroup({
    bookId: new FormControl('',Validators.required)
  });

  get bookId():any{
    return this.getBookForm.get('bookId');
  }

  getBook():void{
    if(this.getBookForm.invalid){
      return;
    }
    const bookId:string = this.getBookForm.get('bookId')?.value.toString(10);
    if(bookId.length==4){
      this.getBookTitle(bookId)
    }
    else{
      this.getBookCopy(bookId);
    }
  }

  getBookTitle(titleId:string):void{
    const url = this.bookUrl + "?id=" + titleId;
    this.http.get<Book>(url).subscribe(
      data=>{
        this.book = data;
        this.bookCopyFlag = false;
        this.outputFlag = true;
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

  getBookCopy(bookId:string):void{
    const url = this.bookUrl + "?id=" + bookId;
    this.http.get<any>(url).subscribe(
      data=>{
        this.bookCopy = new BookCopy(data.copyId,data.price,data.purchaseDate,data.member);
        const bookTitle = data.title;
        this.book = new Book(bookTitle.titleId,data.sectionId,bookTitle.title,bookTitle.author,bookTitle.publication,[this.bookCopy]);
        this.bookCopyFlag = true;
        this.outputFlag = true;
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

  deleteBook(copyId:number):void{
    const url = this.bookUrl + this.book.titleId + copyId;
    this.http.delete(url,{ responseType: 'text' }).subscribe(
      data=>{
        const dialogRef = this.dialog.open(ConfirmDialogComponent,{
          width: '500px',
          data : {
            errorFlag : false,
            title : "Transaction Successful",
            content : "Book Deleted Successfully"
          }
        });
        dialogRef.afterClosed().subscribe(
          data=>{
          this.outputFlag = false;
          if(!this.bookCopyFlag && this.book.bookCopies.length>1){
            this.getBookTitle(this.book.titleId.toString(10));
          }
          else{
            this.outputFlag = false;
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
