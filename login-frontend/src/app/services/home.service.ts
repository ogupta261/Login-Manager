import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ValidationDTO } from '../DTO/ValidationDTO';
import { LoginService } from './login.service';

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  constructor(private http: HttpClient) { }

  changePassword(userName:string, role:string, newPassword:string){
    let body = new HttpParams();
    body = body.set('userName', userName);
    body = body.set('role', role);
    body = body.set('newPassword', newPassword);
    return this.http.post<ValidationDTO>('http://localhost:8090/changePassword',body);
  }

  changeAuthStatus(userName:string, role:string){
    let body = new HttpParams();
    body = body.set('userName', userName);
    body = body.set('role', role);
    return this.http.post<ValidationDTO>('http://localhost:8090/change2FAStatus',body);
  }
}
