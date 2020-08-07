
import { Injectable,Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {  IEmailVariable } from './iemail-variable';
//import {GenericApiService} from '../core/generic-api.service';

import { IP_CONFIG } from '../../tokens';
//import { ILibraryRootConfg } from '../../interfaces';
//import { GenericApiService,ILibraryRootConfg } from 'fastCodeCore'; //from 'projects/fast-code-core/src/lib/common/core/generic-api.service';
import { GenericApiService ,ILibraryRootConfg} from 'projects/fast-code-core/src/public_api';// 'fastCodeCore';
import { BehaviorSubject, Observable } from "rxjs";
import { map, catchError } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class EmailVariableService extends GenericApiService<IEmailVariable> { 
  urlPath;
  private dataToAppend = new BehaviorSubject<string>('');
  readonly dataToSend$ = this.dataToAppend.asObservable();
  constructor(private httpclient: HttpClient,@Inject(IP_CONFIG)  config: ILibraryRootConfg) { 
    super(httpclient,{apiUrl:config.apiPath} ,"emailvariable");
  }

  public changeData(str:string) {
    this.dataToAppend.next(str);
  }

  public getEmailVeriableByType(value : string): Observable<IEmailVariable[]> {
    return this.httpclient.get<IEmailVariable[]>(this.url+'/getEmailVariableByTypeOrSubject',{
      params: {
        type : value
      }
    }).pipe(map((response:any)=> {
      return response;
    }), catchError(this.handleError));
  }

  public getAllWithoutPagination(): Observable<IEmailVariable[]> {
    return this.httpclient.get<IEmailVariable[]>(this.url+'/list').pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError));
  }
  
}
