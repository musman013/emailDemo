import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ISearchField } from '../../common/components/list-filters/ISearchCriteria';
import { ServiceUtils } from '../utils/serviceUtils';

import { IForRootConf } from '../../IForRootConf';
import { IEmailTemplate } from "projects/ip-email-builder/src/lib/email-editor/iemail-template";

@Injectable()
export class GenericApiService<T> {
  private url = "";
  private apiUrl = "";
  public suffix = '';
  constructor(private http: HttpClient,  private config: IForRootConf, suffix: string) {
    this.apiUrl = this.config.apiUrl;
    this.url = this.config.apiUrl + '/' + suffix;
    this.suffix = suffix;
  }

  /**
   * Fetches list of items based on
   * given criteria.
   * @param searchFields Search criteria.
   * @param offset No. of items to be skipped.
   * @param limit Maximum no. of records.
   * @param sort Field and direction information for sorting.
   * @returns Observable of items list.
   */
  public getAll(searchFields?: ISearchField[], offset?: number, limit?: number, sort?: string): Observable<T[]> {

    let params = ServiceUtils.buildQueryData(searchFields, offset, limit, sort);

    return this.http.get<T[]>(this.url, { params }).pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError));

  }

   public getAllWithoutPagination(): Observable<T[]> {
    return this.http.get<T[]>(this.url+'/list').pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError));

  }

  public getEmailVeriableByType(value : string): Observable<T[]> {
    return this.http.get<T[]>(this.url+'/getEmailVariableByTypeOrSubject',{
      params: {
        type : value
      }
    }).pipe(map((response:any)=> {
      return response;
    }), catchError(this.handleError));
  }

  /**
   * Fetches specific child's object of given parent.
   * @param childSuffix Url suffix for the child to be fetched.
   * @param id Item id of parent.
   */
  getChild(childSuffix : string, id:any) : Observable<any>{
    return this.http
      .get<T>(this.url + '/' + id + "/" + childSuffix).pipe(catchError(this.handleError));
  }

  /**
   * Fetches list of items against some
   * parent entity.
   * @param parentSuffix Url suffix of the parent entity.
   * @param parentId
   * @param searchFields Search criteria.
   * @param offset No. of items to be skipped.
   * @param limit Maximum no. of records.
   * @param sort Field and direction information for sorting.
   * @returns Observable of items list.
   */
  getAssociations(parentSuffix: string, parentId: any, searchFields?: ISearchField[], offset?: number, limit?: number, sort?: string): Observable<T[]> {

    let url = this.apiUrl + '/' + parentSuffix + '/' +  + '/' + this.suffix;
    let params = ServiceUtils.buildQueryData(searchFields, offset, limit, sort);
    return this.http.get<T[]>(url, { params }).pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError));
  }

  /**
   * Fetches item against given id.
   * @param id
   * @returns Observable of entity object.
   */
  public getById(id: any): Observable<T> {
    return this.http
      .get<T>(this.url + '/' + id).pipe(catchError(this.handleError));
  }

  public resetTemplateById(id:any): Observable<T> {
    return this.http
      .get<T>(this.url + '/reset/' + id).pipe(catchError(this.handleError));
  }
  /**
   * Calls api to create given item.
   * @param item
   * @returns Observable of created entity object.
   */
  public create(item: T): Observable<T> {
    return this.http
      .post<T>(this.url, item).pipe(catchError(this.handleError));
  }

  /**
   * Call api to update given item.
   * @param item
   * @param id
   * @returns Observable of updated entity object.
   */
  public update(item: T, id: any): Observable<T> {
    return this.http
      .put<T>(this.url + '/' + id, item).pipe(catchError(this.handleError));
  }

  /**
   * Call api to delete item agianst given id.
   * @param id
   */
  public delete(id: any): Observable<Object> {
    return this.http
      .delete(this.url + '/' + id).pipe(map(res => res), catchError(this.handleError));
  }

  /**
   * Handles Api error events.
   * @param err
   */
  protected handleError(err: HttpErrorResponse) {

    let errorMessage = '';
    if (err.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      errorMessage = 'An error occurred: ' + err.error.message;
    } else {
      console.log(err);
      errorMessage = 'Server returned code: ' + err.status + ', error message is: ' + err.message;
    }
    console.error(errorMessage);
    return throwError(errorMessage);
  }

   getAllMasterValue(masterName): Observable<string[]> {
    let url = this.config.apiUrl+'/master/getMastersByMasterName?name='+masterName ;
    return this.http.get<string[]>(url).pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError));
  }


   createFileMetadata(fileMetadata) {
     return this.http.post<any>(this.config.apiUrl + '/files', fileMetadata);

  }

  uploadFile(id, file: File) {
    if (file && file.name) {
      const fileData = new FormData();
      fileData.append('file', file);

      return this.http.put<any>(this.config.apiUrl + '/files/' + id, fileData);
    }
  }

  public getAllTemplates():Observable<IEmailTemplate[]>{
  return this.http.get<IEmailTemplate[]>(this.config.apiUrl+'/email/list', {}).pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError));
  }

  public previewData(query):Observable<any>{ 
    let url = this.config.apiUrl+'/datasource/preview/table?query='+encodeURI(query);
    return this.http.get<any>(url, {}).pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError));
  }

  public get(url:string) : Observable<any>{ 
    return this.http.get<any>(`${this.config.apiUrl}${url}`, {}).pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError));
  }

  public post(url:string,data:any) : Observable<any>{ 
    return this.http.post<any>(`${this.config.apiUrl}${url}`, data).pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError));
  }
}