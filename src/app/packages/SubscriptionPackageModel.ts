export class SubscriptionPackage {
    public packageId:number;
    packageName:string;
    fees:number;
    sections:PackageSection[];

    constructor(packageId:number,
        packageName:string,
        fees:number,
        sections:PackageSection[]){
            this.packageId = packageId;
            this.packageName = packageName;
            this.fees = fees;
            this.sections = sections;
    }

}

export class PackageSection {
    sectionId:string;
    noOfBooks:number;

    constructor(sectionId:string,
        noOfBooks:number){
            this.sectionId = sectionId;
            this.noOfBooks = noOfBooks;
    }
}