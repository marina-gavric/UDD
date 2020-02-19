import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class MagazineService {

  constructor(private httpClient: HttpClient, private http: Http) { }

  updateMagazine(magazine, taskId) {
    console.log('update magazine');
    console.log(magazine);
    return this.httpClient.post('http://localhost:8080/magazine/update/'.concat(taskId), magazine) as Observable<any>;
  }
  approveMagazine(form, taskId) {
    return this.httpClient.post('http://localhost:8080/magazine/approveMagazine/'.concat(taskId), form) as Observable<any>;

  }
  checkFormat(form, taskId) {
    return this.httpClient.post('http://localhost:8080/magazine/checkFormat/'.concat(taskId), form) as Observable<any>;
   }
  approveTheme(form, taskId) {
    console.log('approve theme');
    console.log(form);
    return this.httpClient.post('http://localhost:8080/magazine/approveTheme/'.concat(taskId), form) as Observable<any>;
  }
  getFillText(processId: string) {
    return this.httpClient.get('http://localhost:8080/magazine/getTextForm/'.concat(processId)) as Observable<any>;
  }
  getRevText(processId: string) {
    return this.httpClient.get('http://localhost:8080/magazine/getRevForm/'.concat(processId)) as Observable<any>;
  }
  createText(data, taskId) {
    return this.httpClient.post('http://localhost:8080/magazine/saveText/'.concat(taskId), data) as Observable<any>;
  }
  changePdf(data, taskId) {
    return this.httpClient.post('http://localhost:8080/magazine/changeText/'.concat(taskId), data) as Observable<any>;
  }
  addingCA(data, taskId) {
    return this.httpClient.post('http://localhost:8080/magazine/addCA/'.concat(taskId), data) as Observable<any>;
  }
  fillCA(data, taskId) {
    return this.httpClient.post('http://localhost:8080/magazine/fillCA/'.concat(taskId), data) as Observable<any>;
  }
  saveRev(data, taskId) {
    return this.httpClient.post('http://localhost:8080/magazine/saveRev/'.concat(taskId), data) as Observable<any>;
  }
  savePayment(data, taskId) {
    return this.httpClient.post('http://localhost:8080/magazine/savePay/'.concat(taskId), data) as Observable<any>;
  }
  chooseMagazine(data, taskId) {
    console.log('choose magazine');
    return this.httpClient.post('http://localhost:8080/magazine/choose/'.concat(taskId), data) as Observable<any>;
  }

  startTextProcess() {
    return this.httpClient.get('http://localhost:8080/magazine/startTextProcess') as Observable<any>;
  }
  uploadFile(uploadFile: File) {
    const formData: FormData = new FormData();
    formData.append('File', uploadFile);
    return this.http.post('http://localhost:8080/magazine/addFile/', formData)as Observable<any>;
  }
}
