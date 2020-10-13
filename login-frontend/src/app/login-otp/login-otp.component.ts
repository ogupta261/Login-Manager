import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroupDirective, NgForm, Validators } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { LoginDTO } from '../DTO/LoginDTO';
import { LoginComponent } from '../login/login.component';
import { LoginService } from '../services/login.service';
import { ValidationCodeDTO } from '../DTO/ValidationCodeDTO';

export class OtpErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-login-otp',
  templateUrl: './login-otp.component.html',
  styleUrls: ['./login-otp.component.css']
})
export class LoginOtpComponent implements OnInit {

  otp: ValidationCodeDTO = {
    code: null,
    userNameCommaRole: ''
  };
  otpFormControl = new FormControl('', [
    Validators.required,
    Validators.pattern('[0-9]*')
  ])
  constructor(public dialogRef: MatDialogRef<LoginComponent>,
    @Inject(MAT_DIALOG_DATA) public data, private router: Router) { }

  ngOnInit(): void {
  }
  onNoClick(): void {
    this.dialogRef.close({event: 'Invalid'});
  }
  user: LoginDTO = this.data.user;
  login: LoginService = this.data.login;
  matcher= new OtpErrorStateMatcher();
  otpValid: boolean;
  otpInvalid: boolean;
  otpMessage:string;
  onSubmit() {
    if(this.otpFormControl.valid){
      //validate OTP code. .subscribe(e=>this.dialogRef.close());
      this.otp.userNameCommaRole=this.user.userName+","+this.user.role;
      this.login.validateOTP(this.otp).subscribe(
        e => {
          if(e.valid){
            this.otpValid=true;
            this.otpInvalid=false;
            this.login.setUserLoggedIn(this.user);
            this.otpMessage="OTP is valid. Redirecting to HomePage.";
            this.dialogRef.close({event: 'Valid'});
            setTimeout( () => {
              this.router.navigateByUrl("/home");
            },1000);
          }else{
            this.otpValid=false;
            this.otpInvalid=true;
            this.otpMessage="OTP invalid. Try Again.";
          }
        }
      );
    }else {
      this.otpValid=false;
      this.otpInvalid=true;
      this.otpMessage="OTP Format incorrect!";
    }
    setTimeout(() => {
      this.otpInvalid=false;
      this.otpValid=false
    }, 1000);
  }

}
