
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IApps } from './iapps';
import { GenericApiService } from '../../../projects/fast-code-core/src/public_api';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AppsService extends GenericApiService<IApps> { 
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "apps");
  }
  
  
}
