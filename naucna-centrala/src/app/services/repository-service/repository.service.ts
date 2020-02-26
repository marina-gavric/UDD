import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';
import { Field } from 'src/app/model/Field';
import { TextDTO } from 'src/app/model/TextDTO';

@Injectable({
  providedIn: 'root'
})
export class RepositoryService {
  categories = [];
  languages = [];
  books = [];

  constructor(private httpClient: HttpClient, private http: Http) { }

  startProcess() {
    return this.httpClient.get('http://localhost:8080/register/startProcess') as Observable<any>;
  }
  startMagazineProcess() {
    return this.httpClient.get('http://localhost:8080/magazine/startMagProcess') as Observable<any>;
  }
  createMagazine(data, taskId) {
    return this.httpClient.post('http://localhost:8080/magazine/create/'.concat(taskId), data) as Observable<any>;
  }

  getTask(processId: string) {
    return this.httpClient.get('http://localhost:8080/register/nextTask/'.concat(processId)) as Observable<any>;
  }
  getTaskMag(processId: string) {
    return this.httpClient.get('http://localhost:8080/magazine/nextTaskMag/'.concat(processId)) as Observable<any>;
  }
  getTasks(processInstance: string) {

    return this.httpClient.get('http://localhost:8080/register/get/tasks/'.concat(processInstance)) as Observable<any>;
  }
  getTasksOfUser(username: string) {
    console.log('u gettasksofuser ' + username);
    return this.httpClient.get('http://localhost:8080/register/getTasksUser/'.concat(username)) as Observable<any>;
  }
  claimTask(taskId) {
    return this.httpClient.post('http://localhost:8080/register/tasks/claim/'.concat(taskId), null) as Observable<any>;
  }
  loadTask(taskId) {
    return this.httpClient.get('http://localhost:8080/register/loadTask/'.concat(taskId)) as Observable<any>;
  }
  loadReviewers(processId) {
    return this.httpClient.get('http://localhost:8080/magazine/loadRev/'.concat(processId)) as Observable<any>;
  }
  loadReviewersScience(processId) {
    return this.httpClient.get('http://localhost:8080/magazine/loadRevScience/'.concat(processId)) as Observable<any>;
  }
  loadReviewersLocation(processId) {
    return this.httpClient.get('http://localhost:8080/magazine/loadRevLocation/'.concat(processId)) as Observable<any>;
  }
  loadReviewersMoreLikeThis(processId) {
    return this.httpClient.get('http://localhost:8080/magazine/loadRevMore/'.concat(processId)) as Observable<any>;
  }
  loadTaskTheme(taskId) {
    return this.httpClient.get('http://localhost:8080/magazine/loadTaskTheme/'.concat(taskId)) as Observable<any>;
  }
  findLocation(adresa: string) {
    console.log('adresa: ' + adresa);
    return this.httpClient.get('https://nominatim.openstreetmap.org/search?q=%20"+' + adresa + '+"%20&format=json');
  }


  completeTask(task) {
    console.log(task.name);
    console.log(task.taskId);
    if (task.name === 'ApproveRevisor') {
      window.location.href = 'approveReviewer/' + task.taskId;
      } else if (task.name === 'CheckingTheme') {
        console.log('checking theme');
        window.location.href = 'checkTheme/' + task.taskId;
      } else if (task.name === 'CheckFormat') {
        console.log('checking format');
        window.location.href = 'checkFormat/' + task.taskId;
      } else if (task.name === 'ReenterText') {
        console.log('checking format');
        window.location.href = 'changePdf/' + task.taskId;
      } else {
        window.location.href = 'checkingMagazine/' + task.taskId;
      }
   //  return this.httpClient.post('http://localhost:8080/register/loadTask/'.concat(taskId), null) as Observable<any>;
  }
  searchTexts(searchFields) {
    return this.httpClient.post('http://localhost:8080/magazine/search', searchFields) as Observable<any>;
  }
  chooseReviewers(data, processId) {
    return this.httpClient.post('http://localhost:8080/magazine/chooseReviewers/'.concat(processId), data) as Observable<any>;
  }

}
