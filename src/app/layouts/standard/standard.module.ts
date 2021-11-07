import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StandardComponent } from './standard.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'src/app/shared/shared.module';
import { MatSidenavModule } from '@angular/material/sidenav';
import { SectionsModule } from 'src/app/sections/sections.module';
import { PackagesModule } from 'src/app/packages/packages.module';
import { MembersModule } from 'src/app/members/members.module';
import { BooksModule } from 'src/app/books/books.module';
import { TransactionsModule } from 'src/app/transactions/transactions.module';

@NgModule({
  declarations: [
    StandardComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    FlexLayoutModule,
    MatSidenavModule,
    SectionsModule,
    PackagesModule,
    MembersModule,
    BooksModule,
    TransactionsModule
  ],
  exports: [
    StandardComponent
  ]
})
export class StandardModule { }
