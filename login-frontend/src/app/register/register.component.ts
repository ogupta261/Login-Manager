import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroupDirective, NgForm, Validators } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { RegisterQrcodeComponent } from '../register-qrcode/register-qrcode.component';
import { RegistrationDTO } from '../RegistrationDTO';
import { RegisterService } from '../services/register.service';

export class LoginErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  nameFormControl = new FormControl('',[
    Validators.required,
    Validators.pattern('[A-Z][a-zA-Z ]*')
  ])
  userNameFormControl = new FormControl('', [
    Validators.required,
    Validators.pattern('[a-zA-Z][a-zA-Z0-9]*')
  ])
  passwordFormControl = new FormControl('', [
    Validators.required,
    Validators.pattern('[A-Z][a-zA-Z0-9@#]*'),
    Validators.minLength(6)
  ])
  emailFormControl = new FormControl('', [
    Validators.required,
    Validators.email
  ])
  phoneNoFormControl = new FormControl('', [
    Validators.required,
    Validators.pattern('[6789][0-9]*'),
    Validators.minLength(10),
    Validators.maxLength(10)
  ])
  user: RegistrationDTO = {
    name: '',
    userName: '',
    password: '',
    email: '',
    phoneNo: '',
    role: 'customer',
    authConsent: false
  }

  retrievedImageUrl:string;
  matcher = new LoginErrorStateMatcher();
  constructor(private register: RegisterService, private dialog: MatDialog, private router: Router) { }

  ngOnInit(): void {
  }

  registerError:boolean;
  registerSuccess:boolean;
  registerMessage:string;
  qrAvailable:boolean = false;
  signUp:boolean = true;
  qrGenerate:boolean = false;

  showQR() {
    setTimeout(() => {
      this.dialog.open(RegisterQrcodeComponent, {
        data: {url: this.retrievedImageUrl},
        backdropClass: 'dialogBackground',
        panelClass: 'foregroundDialog'
      }).afterClosed().subscribe();
    }, 500);
  }

  openQR() {
    this.register.generateAuth(this.user.userName,this.user.role).subscribe(
      success => {
        this.retrievedImageUrl = success.response;
        setTimeout(() => {
          this.qrGenerate = false;
          this.qrAvailable = true;
        }, 100);
      }
    );
  }

  registerUser(){
    if(this.nameFormControl.valid && this.userNameFormControl.valid && this.emailFormControl.valid && this.passwordFormControl.valid && this.phoneNoFormControl.valid){
      this.register.register(this.user).subscribe(
        e => {
          this.registerError=false;
          this.registerSuccess=true;
          this.registerMessage="Registration Success!";
          if(this.user.authConsent){
            this.register.setNewUser(e);
            setTimeout(() => {
              this.qrGenerate = true;
              this.signUp = false;
            }, 500);
          }else {
            setTimeout(() => {
              this.registerMessage = "Redirecting To Login!";
            }, 1000);
            setTimeout(() => {
              this.registerSuccess=false;
            }, 500);
            this.router.navigateByUrl('/login');
          }
        },
        x => {
          this.registerError=true;
          this.registerSuccess=false;
          this.registerMessage=x.message;
        }
      );
    }else {
       this.registerError = true;
       this.registerSuccess = false;
       this.registerMessage="Form Incomplete!";
    }
    setTimeout(() => {
      this.registerError = false;
      this.registerSuccess = false;
    }, 1000);
  }
}
