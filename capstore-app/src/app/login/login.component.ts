import { Component, OnInit } from '@angular/core';
import { User } from '../Model/User.model';
import { CapstoreService } from '../service/capstore.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  password;
  email;
  errorMessage;
  check;
  role;
  c = 0;
  confirmUser;
  constructor(
    private _capstoreService: CapstoreService,
    private router: Router
  ) {}

  ngOnInit(): void {}
  onSubmit(form) {
    this._capstoreService.login(this.email, this.password, this.role).subscribe(
      (data) => {
        this.storeData(data);
      },
      (error) => {
        if (error instanceof HttpErrorResponse) {
          alert(error.error);
        }
      }
    );
  }

  storeData(data) {
    if (this.role == 'Customer') {
      this._capstoreService.custDetails.push(data);
      this._capstoreService.email = this.email;
      this.router.navigate(['homepage']);
    } else {
      this._capstoreService.merchDetails.push(data);
      this._capstoreService.email = this.email;
      this.router.navigate(['home']);
    }
  }
}
