import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LibrarySection } from '../../sections/LibrarySectionModel';

@Injectable({
  providedIn: 'root'
})
export class LibrarySectionServiceService {

  constructor(private http: HttpClient) {}

  getLibrarySections():Observable<LibrarySection[]>{
    const query = "http://localhost:8080/api/v1/lms/section/?all=true";
    return this.http.get<LibrarySection[]>(query);
  }

}
