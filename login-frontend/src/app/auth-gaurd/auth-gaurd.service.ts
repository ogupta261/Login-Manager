import {CanActivate, Router} from "@angular/router"
import { Injectable } from '@angular/core';
import { LoginService } from '../services/login.service';

@Injectable()
export class AuthGaurd implements CanActivate {

    constructor(private login: LoginService, private router:Router){
        
    }

    canActivate(): boolean{
        if(this.login.isLoggedIn()){
            return true;
        }else{
            this.router.navigateByUrl("/login");
        }
    }

}