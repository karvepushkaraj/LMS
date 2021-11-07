import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SubscriptionPackage } from 'src/app/packages/SubscriptionPackageModel';

@Injectable({
  providedIn: 'root'
})
export class SubpkgServiceService {

  constructor(private http: HttpClient) { }

  getSubscriptionPackages():Observable<SubscriptionPackage[]>{
    const query = "http://localhost:8080/api/v1/lms/package/?all=true";
    return this.http.get<SubscriptionPackage[]>(query);
  }

}
