import { NgModule, Component } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LoginComponent} from './login/login.component';
import{SignupComponent} from './signup/signup.component';
import { AuthGuard } from './guards/auth.guard';


const routes: Routes = [
  {path:'',component:LoginComponent},
  {path:'signup',component:SignupComponent,},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const RoutingComponents=[LoginComponent,SignupComponent];
