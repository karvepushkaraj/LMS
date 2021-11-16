import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BookFormComponent } from './books/book-form/book-form.component';
import { MemberFormComponent } from './members/member-form/member-form.component';
import { PackageFormComponent } from './packages/package-form/package-form.component';
import { ReportFormComponent } from './reports/report-form/report-form.component';
import { SectionFormComponent } from './sections/section-form/section-form.component';
import { TransactionFormComponent } from './transactions/transaction-form/transaction-form.component';

const routes: Routes = [
  { path: '', component: ReportFormComponent },
  { path: 'members', component: MemberFormComponent },
  { path: 'sections', component: SectionFormComponent },
  { path: 'books', component: BookFormComponent },
  { path: 'packages', component: PackageFormComponent },
  { path: 'transactions', component: TransactionFormComponent },
  { path: 'reports', component: ReportFormComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
