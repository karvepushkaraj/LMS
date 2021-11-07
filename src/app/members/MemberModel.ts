export class Member {
    memberId:number;
    name:string;
    mobileNumber:string;
    emailId:string;
    status:string;
    enrollmentDate:Date;
    exitDate:Date;
    book:Number[];

    constructor(
        memberId:number,
        name:string,
        mobileNumber:string,
        emailId:string,
        status:string,
        enrollmentDate:Date,
        exitDate:Date,
        book:Number[]
    ){
        this.memberId = memberId;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.emailId = emailId;
        this.status = status;
        this.enrollmentDate = enrollmentDate;
        this.exitDate = exitDate;
        this.book = book;
    }
}