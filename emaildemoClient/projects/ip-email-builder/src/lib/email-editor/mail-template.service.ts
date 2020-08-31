import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { IEmailTemplate } from './iemail-template';
import { IP_CONFIG } from '../tokens';

import { ILibraryRootConfg, GenericApiService } from 'projects/fast-code-core/src/public_api';// 'fastCodeCore';
@Injectable({
  providedIn: 'root'
})
export class MailTemplateService extends GenericApiService<IEmailTemplate> {

  constructor(private httpclient: HttpClient, @Inject(IP_CONFIG) config: ILibraryRootConfg) {
    super(httpclient, { apiUrl: config.apiPath }, 'mail');
  }

}

