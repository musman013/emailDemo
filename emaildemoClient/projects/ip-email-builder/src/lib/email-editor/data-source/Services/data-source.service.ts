import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
//import {GenericApiService} from '../core/generic-api.service';

//import { ILibraryRootConfg } from '../../interfaces';
//import { GenericApiService,ILibraryRootConfg } from 'fastCodeCore'; //from 'projects/fast-code-core/src/lib/common/core/generic-api.service';
import { GenericApiService, ILibraryRootConfg } from 'projects/fast-code-core/src/public_api';// 'fastCodeCore';
import { BehaviorSubject, Observable } from "rxjs";
import { IP_CONFIG } from '../../../tokens';
import { IDataSource } from "projects/ip-email-builder/src/lib/email-editor/data-source/Models/IDataSource";
import { map, catchError } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class DataSourceService extends GenericApiService<IDataSource>{
  urlPath;
  private tableClose: BehaviorSubject<boolean> = new BehaviorSubject(false);
  readonly tableClose$ = this.tableClose.asObservable();

  private dataToAppend = new BehaviorSubject<string>('');
  readonly dataToSend$ = this.dataToAppend.asObservable();

  private previewTableData: BehaviorSubject<Object> = new BehaviorSubject({});
  readonly previewTableData$ = this.previewTableData.asObservable();

  constructor(private httpclient: HttpClient, @Inject(IP_CONFIG) config: ILibraryRootConfg) {
    super(httpclient, { apiUrl: config.apiPath }, "datasource");
  }

  changetableClose(data:boolean) {
    this.tableClose.next(data);
  }

  setTableContent(data:any) {
    this.previewTableData.next(data);
  }
  
  public changeData(str:string) {
    this.dataToAppend.next(str);
  }

  public previewData(query):Observable<any>{ 
    let url = this.url + '/preview/table?query='+encodeURI(query);
    return this.httpclient.get<any>(url, {}).pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError));
  }
  
}
