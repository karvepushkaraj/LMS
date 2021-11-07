import { Component, OnInit } from '@angular/core';
import { faBook, faCube, faExchangeAlt, faReceipt, faUser, faWallet } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-side-nav-bar',
  templateUrl: './side-nav-bar.component.html',
  styleUrls: ['./side-nav-bar.component.scss']
})
export class SideNavBarComponent implements OnInit {

  transaction = faExchangeAlt;
  member = faUser;
  book = faBook;
  subscription = faWallet;
  section = faCube;
  report = faReceipt;

  constructor() { }

  ngOnInit(): void {
  }

}
