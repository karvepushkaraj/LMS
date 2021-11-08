import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { LibrarySection } from '../LibrarySectionModel';
import { UpperCasePipe } from '@angular/common';

@Component({
  selector: 'app-section-form',
  templateUrl: './section-form.component.html',
  styleUrls: ['./section-form.component.scss']
})
export class SectionFormComponent implements OnInit {

  librarySection:LibrarySection = new LibrarySection("","");
  sectionUrl:string = "http://localhost:8080/api/v1/lms/section/";
  requestType:string="get";
  getFormFlag:boolean = true;
  addFormFlag:boolean = false;
  updateFormFlag:boolean = false;
  errorFlag:boolean = false;
  errorMsg: string = "";
  showOutputFlag:boolean = false;
  sucessStyle:string = "";
  sucessMsg:string = "";

  constructor(
    private http: HttpClient,
    private upperCasePipe: UpperCasePipe
    ) {}

  ngOnInit(): void {}

  getSectionForm =  new FormGroup({
    sectionId : new FormControl('',[Validators.required, Validators.pattern("[a-zA-Z0-9]{3}")])
  });

  addSectionForm = new FormGroup({
    sectionId : new FormControl('',[Validators.required, Validators.pattern("[a-zA-Z0-9]{3}")]),
    sectionName : new FormControl('',Validators.required)
  });

  updateSectionForm:FormGroup = new FormGroup({
    sectionId : new FormControl({value:'',disabled:true}),
    sectionName : new FormControl('',Validators.required)
  });

  get sectionId():any {
    if(this.getFormFlag){
      return this.getSectionForm.get('sectionId');
    }
    else if(this.addFormFlag){
      return this.addSectionForm.get('sectionId');
    }
    return this.updateSectionForm.get('sectionId');
  }

  get sectionName():any { 
    if(this.addFormFlag){
      return this.addSectionForm.get('sectionName');
    }
    return this.updateSectionForm.get('sectionName');
  }

  setRequestType():void{
    this.reset();
    if(this.requestType=="add"){
      this.addFormFlag = true;
    }
    else{
      this.getFormFlag = true;
    }
  }

  toUpper(form:FormGroup):void{
    form.get('sectionId')?.setValue(this.upperCasePipe.transform(form.get('sectionId')?.value));
    form.get('sectionName')?.setValue(this.upperCasePipe.transform(form.get('sectionName')?.value));
  }

  getSection():void{
    if(this.getSectionForm.invalid){
      this.showOutputFlag = false;
      return;
    }
    const sectionId = this.getSectionForm.get('sectionId')?.value;
    const url = this.sectionUrl + "?id=" + sectionId;
    this.http.get<LibrarySection>(url).subscribe(
      data=>{
        this.errorFlag=false;
        this.librarySection=data;
        if(this.requestType=="update"){
        this.updateSectionForm.get('sectionId')?.setValue(this.librarySection.sectionId);
        this.updateSectionForm.get('sectionName')?.setValue(this.librarySection.sectionName);
        this.getFormFlag = false;
        this.updateFormFlag=true;
        }
        else {
          this.showOutputFlag = true;
        }
      },
      error=>{
        console.log(error.error);
        this.errorFlag=true;
        this.errorMsg = error.error.message;
        this.showOutputFlag = true;
    }
    );
  }

  addSection():void{
    if(this.addSectionForm.invalid){
      this.showOutputFlag = false;
      return;
    }
    this.http.post(this.sectionUrl,this.addSectionForm.value,{ responseType: 'text' }).subscribe(
      data=>{
        this.errorFlag=false;
        this.librarySection = this.addSectionForm.value;
        this.sucessStyle = "alert alert-success";
        this.sucessMsg = "added Sucessfully";
        this.showOutputFlag=true;
      },
      error=>{
        console.log(error.error);
        this.errorFlag=true;
        this.errorMsg = JSON.parse(error.error).message;
        this.showOutputFlag=true;
      }
    );
  }

  updateSection():void{
    if(this.updateSectionForm.invalid){
      this.showOutputFlag = false;
      return;
    }
    this.librarySection.sectionName = this.updateSectionForm.get('sectionName')?.value;
    this.http.put(this.sectionUrl,this.librarySection,{ responseType: 'text' }).subscribe(
      data=>{
        this.errorFlag=false;
        this.sucessStyle = "alert alert-success";
        this.sucessMsg = "updated Sucessfully";
        this.showOutputFlag=true;
      },
      error=>{
        console.log(error.error);
        this.errorFlag=true;
        this.errorMsg = JSON.parse(error.error).message;
        this.showOutputFlag=true;
      }
    );
  }

  reset():void{
    this.getFormFlag = false;
    this.addFormFlag = false;
    this.updateFormFlag = false;
    this.showOutputFlag = false;
    this.errorFlag = false;
    this.sucessMsg = "";
    this.sucessStyle = "";
    this.getSectionForm.reset();
    this.addSectionForm.reset();
    this.updateSectionForm.reset();
  }

}
