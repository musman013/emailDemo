import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import SwaggerUI from 'swagger-ui';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-swagger',
  templateUrl: './swagger.component.html',
  styleUrls: ['./swagger.component.css']
})
export class SwaggerComponent implements OnInit, AfterViewInit {
  ngAfterViewInit(): void {
    SwaggerUI({
      domNode: this.el.nativeElement,
      url: environment.apiUrl + "/v3/api-docs",
      deepLinking: true,
      presets: [
        SwaggerUI.presets.apis,
      ],
    })
  }

  @ViewChild('swagger', { static: false }) el;
  constructor() {

  }

  ngOnInit() {
  }

}
