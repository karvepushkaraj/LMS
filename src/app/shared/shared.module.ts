import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { SideNavBarComponent } from './components/side-nav-bar/side-nav-bar.component';
import { FormsModule } from '@angular/forms';
import { SubHeaderComponent } from './components/sub-header/sub-header.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AppRoutingModule } from '../app-routing.module';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [
    FooterComponent,
    HeaderComponent,
    SideNavBarComponent,
    SubHeaderComponent,
    ConfirmDialogComponent
  ],
  imports: [
    CommonModule,
    FlexLayoutModule,
    FontAwesomeModule,
    AppRoutingModule,
    MatDialogModule,
    MatButtonModule
  ],
  exports: [
    FooterComponent,
    HeaderComponent,
    SideNavBarComponent,
    SubHeaderComponent,
    ConfirmDialogComponent,
    CommonModule,
    FormsModule
  ],
  providers: [
  ]
})
export class SharedModule { }
