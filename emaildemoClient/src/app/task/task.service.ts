
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ITask } from './itask';
import { GenericApiService } from '../../../projects/fast-code-core/src/public_api';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TaskService extends GenericApiService<ITask> { 
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "task");
  }
  
  
}
