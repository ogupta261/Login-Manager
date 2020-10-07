import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistrationDataDTO } from '../DTO/RegistrationDataDTO';
import { RegistrationDTO } from '../DTO/RegistrationDTO';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  newUser: RegistrationDataDTO = {
    email:'',
    name:'',
    phoneNo:'',
    userName:''
  }
  QRurl: string;
  constructor(private http: HttpClient) { }

  register(newUser: RegistrationDTO){
    return this.http.post<RegistrationDataDTO>('http://localhost:8090/register',newUser);
  }
  generateAuth(userName:string, role:string){
    return this.http.get<any>('http://localhost:8090/generate/'+userName+'/'+role, {responseType: 'json'});
  }

  getNewUser(){
    if(this.newUser.userName!=''){
      return this.newUser;
    }
  }
  setNewUser(newUser: RegistrationDataDTO){
    this.newUser.email = newUser.email;
    this.newUser.name = newUser.name;
    this.newUser.phoneNo = newUser.phoneNo;
    this.newUser.userName = newUser.userName;
  }
  backToLogin(){
    this.newUser.email = '';
    this.newUser.name = '';
    this.newUser.phoneNo = '';
    this.newUser.userName = '';
  }
}
