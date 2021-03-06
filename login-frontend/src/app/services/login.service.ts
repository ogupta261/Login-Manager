import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginDTO } from '../DTO/LoginDTO';
import { ValidationDTO } from '../DTO/ValidationDTO';
import { LoginCredentialsDTO } from '../DTO/LoginCredentialsDTO';
import { ValidationCodeDTO } from '../DTO/ValidationCodeDTO';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  userLoggedIn: LoginDTO ={
    authConsent: null,
    lastLoginDate: null,
    role: null,
    userName: null
  };
  constructor(private http: HttpClient) { }

  ngOnInit():void {
    this.userLoggedIn={
      authConsent: null,
      lastLoginDate: null,
      role: null,
      userName: null
    };
  }

  isLoggedIn(){
    if(this.userLoggedIn.userName==null){
      return false;
    }
    return true;
  }
  getUserLoggedIn(){
    if(this.isLoggedIn()){
      return this.userLoggedIn;
    }
  }
  getUserUserName(){
    return this.userLoggedIn.userName;
  }
  getUserAuthConsent(){
    return this.userLoggedIn.authConsent;
  }
  getUserRole() {
    return this.userLoggedIn.role;
  }

  setUserLoggedIn(user: LoginDTO){
    this.userLoggedIn.authConsent = user.authConsent;
    this.userLoggedIn.lastLoginDate = user.lastLoginDate;
    this.userLoggedIn.role = user.role;
    this.userLoggedIn.userName = user.userName;
  }

  login(user: LoginCredentialsDTO){
    return this.http.post<LoginDTO>('http://localhost:8090/login',user);
  }

  validateOTP(otp: ValidationCodeDTO){
    return this.http.post<ValidationDTO>('http://localhost:8090/validate/key',otp);
  }
}
