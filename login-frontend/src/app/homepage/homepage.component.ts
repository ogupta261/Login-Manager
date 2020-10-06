import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { userInfo } from 'os';
import { LoginDTO } from '../DTO/LoginDTO';
import { LoginService } from '../services/login.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  constructor(private login: LoginService, private router: Router) { }
  user: LoginDTO = this.login.getUserLoggedIn();
  authStatus: string = this.login.getUserAuthConsent()? 'true': 'false';
  ngOnInit(): void {
    if(!this.login.isLoggedIn){
      this.router.navigateByUrl("/login");
    }
  }
  logout(){
    this.user.authConsent=false;
    this.user.lastLoginDate=new Date();
    this.user.role="-1";
    this.user.userName="-1";
    this.login.setUserLoggedIn(this.user);
    this.router.navigateByUrl("/login");
  }
}
