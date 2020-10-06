import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroupDirective, NgForm, Validators } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { LoginDTO } from '../DTO/LoginDTO';
import { RegisterQrcodeComponent } from '../register-qrcode/register-qrcode.component';
import { HomeService } from '../services/home.service';
import { LoginService } from '../services/login.service';
import { RegisterService } from '../services/register.service';

export class HomeErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}
@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  constructor(private login: LoginService, private router: Router, private dialog: MatDialog, private home: HomeService, private register: RegisterService) { }
  user: LoginDTO = this.login.getUserLoggedIn();
  authStatus: string = this.login.getUserAuthConsent()? 'true': 'false';
  retrievedImageUrl:string;
  changePass:boolean=false;
  qrAvailable:boolean=false;
  passChange:boolean=false;
  newPassword:string;
  matcher = new HomeErrorStateMatcher();
  ngOnInit(): void {
    if(!this.login.isLoggedIn){
      this.router.navigateByUrl("/login");
    }
  }
  newPasswordFormControl = new FormControl('', [
    Validators.required,
    Validators.pattern('[A-Z][a-zA-Z0-9@#]*'),
    Validators.minLength(6)
  ])
  flipPasswordField() {
    this.changePass=!this.changePass;
  }
  changePassword(){
    if(this.newPasswordFormControl.valid){
      this.home.changePassword(this.user.userName,this.user.role,this.newPassword).subscribe(
        e => {
          this.changePass=false;
          this.passChange=true;
          setTimeout(() => {
            this.passChange=false;
          }, 1000);
        }
      );
    }
  }
  showQR() {
    setTimeout(() => {
      this.dialog.open(RegisterQrcodeComponent, {
        data: {url: this.retrievedImageUrl, backToLoginValue: false},
        backdropClass: 'dialogBackground',
        panelClass: 'foregroundDialog'
      }).afterClosed().subscribe();
    }, 500);
  }
  hideQR() {
    this.retrievedImageUrl='';
    this.qrAvailable=false;
  }
  openQR() {
    this.register.generateAuth(this.user.userName,this.user.role).subscribe(
      success => {
        this.retrievedImageUrl = success.response;
        setTimeout(() => {
          this.qrAvailable = true;
        }, 100);
      }
    );
  }
  changeAuthStatus(){
    this.home.changeAuthStatus(this.user.userName,this.user.role).subscribe(
      e => {
        this.user.authConsent=e.valid;
        if(e.valid){
          this.authStatus="true";
          this.openQR();
        }else {
          this.authStatus="false";
        }
      }
    )
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
