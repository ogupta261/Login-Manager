import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { RegistrationDataDTO } from '../DTO/RegistrationDataDTO';
import { RegisterComponent } from '../register/register.component';
import { RegisterService } from '../services/register.service';

@Component({
  selector: 'app-register-qrcode',
  templateUrl: './register-qrcode.component.html',
  styleUrls: ['./register-qrcode.component.css']
})
export class RegisterQrcodeComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<RegisterComponent>,
      @Inject(MAT_DIALOG_DATA) public data, private register: RegisterService, private router:Router) { }

  ngOnInit(): void {
  }
  onNoClick(): void {
    this.dialogRef.close();
  }
  
  QRurl: string = this.data.url;
  backToLoginNeeded: boolean = this.data.backToLoginValue;
  close() {
    this.dialogRef.close();
  }
  login(){
    this.register.backToLogin();
    this.router.navigateByUrl('/login');
    this.dialogRef.close();
  }

}
