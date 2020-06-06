import { Component, OnInit } from '@angular/core';
import { CapstoreService } from '../service/capstore.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { User } from '../Model/User.model';

@Component({
  selector: 'app-forgotpassword',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'],
})
export class ForgotPasswordComponent implements OnInit {
  email;
  submitted = false;
  message = 'Your password has been sent. Please check your e-mail.';
  constructor(
    private _capstoreService: CapstoreService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  onSubmit(form) {
    this._capstoreService.forgotPassword(this.email).subscribe(
      (data: User) => {
        console.log(data);
        this.email = ' ';
        this.submitted = true;
      },
      (error) => {
        if (error instanceof HttpErrorResponse) {
          alert(error.error);
        }
      }
    );
  }
}
