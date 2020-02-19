import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AreaService {

  constructor(private httpClient: HttpClient) { }

  loadAreas() {
    console.log('Usao u loadAreas');
    return this.httpClient.get('http://localhost:8080/areas/getAll') as Observable<any>;
  }

}
