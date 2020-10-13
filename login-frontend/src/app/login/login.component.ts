import { Component, OnInit } from '@angular/core';
import { LoginCredentialsDTO } from '../DTO/LoginCredentialsDTO';
import { roles } from '../roles';
import {FormControl, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';
import { MatDialog } from '@angular/material/dialog';
import { LoginOtpComponent } from '../login-otp/login-otp.component';
import { LoginService } from '../services/login.service';
import { Router } from '@angular/router';

export class LoginErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  userNameFormControl = new FormControl('', [
    Validators.required
  ]);
  passwordFormControl = new FormControl('', [
    Validators.required
  ]);
  roleFormControl = new FormControl('', Validators.required);
  rolesAvailable:string[] = roles;
  user: LoginCredentialsDTO = {
    userName: '',
    password: '',
    role: '',
    authConsent: true
  }
  matcher = new LoginErrorStateMatcher();
  constructor(private dialog: MatDialog, private login: LoginService, private router: Router) { }

  loggedIn:boolean=false;
  loginError:boolean=false;
  loginMessage:string='';
  ngOnInit(): void {
  }
  authenticate() {
    if(this.userNameFormControl.valid && this.passwordFormControl.valid && this.roleFormControl.valid){
      this.login.login(this.user).subscribe(
        e => {
          this.loginError=false;
          this.loggedIn=true;
          this.loginMessage="Login Successful!";
          if(e.authConsent){
            setTimeout(() => {
              this.dialog.open(LoginOtpComponent, {
                data: {user : e, login: this.login},
                backdropClass: 'dialogBackground',
                panelClass: 'foregroundDialog'
              }).afterClosed().subscribe(result => {
                if(result.event == 'Valid'){
                  this.login.setUserLoggedIn(e);
                }
              });
            }, 500);
          }else{
            //redirect to home.
            this.login.setUserLoggedIn(e);

            this.router.navigateByUrl("/home");
          }
        },
        x => {
          this.loginError=true;
          this.loggedIn=false;
          this.loginMessage="Login Failed : "+x.message;
        }
      )
      
    }else {
      this.loggedIn=false;
      this.loginError=true;
      this.loginMessage='Incomplete Details!';
    }
    
    setTimeout(() => {
      this.loginError=false;
      this.loggedIn=false;
    }, 1000);
  }

}
