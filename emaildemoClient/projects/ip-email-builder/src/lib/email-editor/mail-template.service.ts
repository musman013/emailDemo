import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { IEmailTemplate } from './iemail-template';
import { GenericApiService } from './generic-api.service';
import { IP_CONFIG } from '../tokens';

import { ILibraryRootConfg } from 'projects/fast-code-core/src/public_api';// 'fastCodeCore';
@Injectable({
  providedIn: 'root'
})
export class MailTemplateService extends GenericApiService<IEmailTemplate> {

  constructor(private httpclient: HttpClient, @Inject(IP_CONFIG) config: ILibraryRootConfg) {
    super(httpclient, config, 'mail');
  }

}

