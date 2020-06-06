import { Component, OnInit } from '@angular/core';
import { User } from '../Model/User.model';
import { ConfirmEqualValidatorDirective } from '../Shared/confirm-equal-validator.directive';
import { CapstoreService } from '../service/capstore.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent implements OnInit {
  userDetails: User = new User();
  password;
  role;
  flag = true;
  constructor(
    private _capstoreService: CapstoreService,
    private router: Router
  ) {}
  validateUser() {
    this.userDetails.role = this.role;
    if (this.role == 'Customer') {
      this._capstoreService.registerCustomer(this.userDetails).subscribe(
        (data) => {
          console.log(data);
          if (this.flag == true) {
            alert('Customer Registered Successfully !!!');
            this.router.navigate(['']);
          }
        },
        (error) => {
          if (error instanceof HttpErrorResponse) {
            alert(error.error);
            this.flag = false;
          }
        }
      );
    } else if (this.role == 'Merchant' || this.role == 'Third-Party Merchant') {
      console.log(this.userDetails);
      this._capstoreService.registerMerchant(this.userDetails).subscribe(
        (data) => {
          console.log(data);
          if (this.flag == true) {
            alert('Merchant Registered Successfully !!!');
            this.router.navigate(['']);
          }
        },
        (error) => {
          if (error instanceof HttpErrorResponse) {
            alert(error.error);
            this.flag = false;
          }
        }
      );
    }
  }

  ngOnInit(): void {}
}
