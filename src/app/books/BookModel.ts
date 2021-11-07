export class Book {
    titleId:number;
    sectionId:string;
    title:string;
    author:string;
    publication:string;
    bookCopies:BookCopy[];

    constructor(
        titleId:number,
        sectionId:string,
        title:string,
        author:string,
        publication:string,
        bookCopies:BookCopy[]
    ){
        this.titleId = titleId;
        this.sectionId = sectionId;
        this.title = title;
        this.author = author;
        this.publication = publication;
        this.bookCopies = bookCopies;
    }
}

export class BookCopy {
    copyId:number;
    price:number;
    purchaseDate:Date;
    member:number;

    constructor(
        copyId:number,
        price:number,
        purchaseDate:Date,
        member:number
        ){
            this.copyId = copyId;
            this.price = price;
            this.purchaseDate = purchaseDate;
            this.member = member;
        }
}