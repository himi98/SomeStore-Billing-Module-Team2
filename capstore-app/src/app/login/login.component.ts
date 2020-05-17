import { Component, OnInit } from '@angular/core';
import {User} from '../Model/User.model';
import { CapstoreService } from '../service/capstore.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  custDetails:User[];
  password;
  merchDetails:User[];
  eMail;
  errorMessage;
  check;
  role;
  c=0;
  confirmUser;
  constructor(private _capstoreService:CapstoreService , private _router:Router) { }

  ngOnInit(): void {
    this.check=this._capstoreService.getUserDetail();
    this._capstoreService.getCustomer().subscribe(data=>this.custDetails);
    this._capstoreService.getCustomer().subscribe(data=>this.merchDetails);
    localStorage.removeItem("customer");
    localStorage.removeItem("merchant");
    this.c=0;


  }
  onSubmit(form)
  {
    if(this.role=="Customer")
    {
    console.log(this.password);
    console.log(form.value.selectedBy);

    for (let a of this.custDetails) {
      if (a.eMail== this.eMail) {
        this.confirmUser = a;
        this.c=this.c+1;
      }
      if (a.password == this.password)
        this.c=this.c+1;
    }
    if (this.c==2) {
      this._capstoreService.setCurrentCustomer(this.confirmUser);
     //this._router.navigate(['/successpage']);
     this._capstoreService.setLoggedIn(true);
     console.log("Hekko customer");
     this.errorMessage="";
     this.c=0;

    }
    else
    this.errorMessage="Invalid Credentials";
      }
  else
  {console.log(this.password);
    console.log(form.value.selectedBy);

    for (let a of this.merchDetails) {
      if (a.eMail== this.eMail) {
        this.confirmUser = a;
        this.c=this.c+1;
      }
      if (a.password == this.password)
        this.c=this.c+1;
    }
    if (this.c==2) {
      this._capstoreService.setCurrentMerchant(this.confirmUser);
     //this._router.navigate(['/successpage']);
     this._capstoreService.setLoggedIn(true);
     console.log("Hekko merchant");
     this.errorMessage="";

    }
    else
    this.errorMessage="Invalid Credentials";
      }
}

}
