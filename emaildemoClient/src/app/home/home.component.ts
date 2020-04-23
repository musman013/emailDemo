import { Component } from '@angular/core';
import { Router} from "@angular/router";
import { first } from 'rxjs/operators';
 
@Component({ 
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

	constructor(
		public router: Router,
  ) { }
 
	goToDashboard(){
    this.router.navigate(['dashboard']);
  }
}
