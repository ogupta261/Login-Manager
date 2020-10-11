import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AngularMaterialModule } from './material-module';
import { RegisterComponent } from './register/register.component';
import { SplashScreenComponent } from './splash-screen/splash-screen.component';
import { LoginOtpComponent } from './login-otp/login-otp.component';
import { RegisterQrcodeComponent } from './register-qrcode/register-qrcode.component';
import { LoginService } from './services/login.service';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HomepageComponent } from './homepage/homepage.component';
import { HttpErrorInterceptor } from './http-error.interceptor';
import { AuthGaurd } from './auth-gaurd/auth-gaurd.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    SplashScreenComponent,
    LoginOtpComponent,
    RegisterQrcodeComponent,
    HomepageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    AngularMaterialModule,
    HttpClientModule
  ],
  providers: [LoginService, AuthGaurd,{
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
