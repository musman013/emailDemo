import { Component, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { Router, Event } from '@angular/router';
import { MatSidenav, MatSidenavContent } from '@angular/material';

import { FastCodeCoreTranslateUiService, Globals } from 'projects/fast-code-core/src/public_api';
import { EmailBuilderTranslateUiService } from 'projects/ip-email-builder/src/public_api';
import { Entities } from './entities'

@Component({
	selector: 'app-main-nav',
	templateUrl: './main-nav.component.html',
	styleUrls: ['./main-nav.component.scss']
})
export class MainNavComponent {	
	@ViewChild("drawer", { static: false }) drawer: MatSidenav;
	@ViewChild("navContent", { static: false }) navContent: MatSidenavContent;
	
	appName: string = 'emaildemo';
	selectedLanguage: string;
	entityList = Entities;

	hasTaskAppPermission: boolean = false;
	hasAdminAppPermission: boolean = false;

	isSmallDevice$: Observable<boolean>;
	isMediumDevice$: Observable<boolean>;
	isCurrentRootRoute: boolean = true;
	
	
	constructor(
		private router: Router,
		public translate: TranslateService,
		public Global: Globals,
    private fastCodeCoreTranslateUiService: FastCodeCoreTranslateUiService,

    private emailBuilderTranslateUiService: EmailBuilderTranslateUiService,
	) {

		this.isSmallDevice$ = Global.isSmallDevice$;
		this.isMediumDevice$ = Global.isMediumDevice$;

		this.router.events.subscribe((event: Event) => {
			this.isCurrentRootRoute = (this.router.url == '/') ? true : false;
		});
		
		this.selectedLanguage = localStorage.getItem('selectedLanguage');
	}

	switchLanguage(language: string) {
	  if(this.translate.translations[language]){
      this.translate.use(language);
    }else{
      this.translate.use(language).subscribe(() => {
        this.fastCodeCoreTranslateUiService.init(language);

        this.emailBuilderTranslateUiService.init(language);
      });
    }
    localStorage.setItem('selectedLanguage', language);
    this.selectedLanguage = language;
	}
	
	
}