import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportFormComponent } from './report-form/report-form.component';
import { MatCardModule } from '@angular/material/card';
import { FlexLayoutModule } from '@angular/flex-layout';



@NgModule({
  declarations: [
    ReportFormComponent
  ],
  imports: [
    CommonModule,
    FlexLayoutModule,
    MatCardModule
  ]
})
export class ReportsModule { }
