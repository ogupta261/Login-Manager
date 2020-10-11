import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGaurd } from './auth-gaurd/auth-gaurd.service';
import { HomepageComponent } from './homepage/homepage.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';


const routes: Routes = [
  {component: LoginComponent,path: "login"},
  {component: RegisterComponent,path: "register"},
  {component: HomepageComponent,path: "home", canActivate: [AuthGaurd]},
  {path: "",redirectTo: "/login",pathMatch: "full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
