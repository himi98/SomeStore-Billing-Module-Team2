import { NgModule, Component } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { VerficationComponent } from './signup/verification.component';
import { HomePageComponent } from './home-page/home-page.component';
import { ProductPageComponent } from './product-page/product-page.component';
import { HomeComponent } from './home/home.component';
import { VerifyMerchantComponent } from './verify-merchant/verify-merchant.component';

const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'verify', component: VerficationComponent },
  { path: 'homepage', component: HomePageComponent },
  { path: 'productpage', component: ProductPageComponent },
  { path: 'home', component: HomeComponent },
  { path: 'verifyMerchant', component: VerifyMerchantComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
export const RoutingComponents = [
  LoginComponent,
  SignupComponent,
  VerficationComponent,
];
