import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material';
import { TranslateService } from '@ngx-translate/core';

import { IEmailVariable } from './iemail-variable';
import { EmailVariableService } from './email-variable.service';
import { Router, ActivatedRoute } from '@angular/router';
import { EmailVariableNewComponent } from './email-variable-new.component';
import {
	BaseListComponent,
	Globals,
	IListColumn,
	listColumnType,
	PickerDialogService,
	ErrorService,
	operatorType, ISearchField, ConfirmDialogComponent, ServiceUtils
} from 'projects/fast-code-core/src/public_api';// 'fastCodeCore';
import { EmailVariablTypeService } from './email-variable.type.service';
import { EmailTemplateService } from '../email-template.service';

@Component({
	selector: 'app-email-variable-list',
	templateUrl: './email-variable-list.component.html',
	styleUrls: ['./email-variable-list.component.scss']
})
export class EmailVariableListComponent extends BaseListComponent<IEmailVariable> implements OnInit {

	title: string = this.translate.instant('EMAIL-EDITOR.EMAIL-VARIABLE.TITLE');

	columns: IListColumn[] = [
		{
			column: 'propertyName',
			label: this.translate.instant('EMAIL-EDITOR.EMAIL-VARIABLE.FIELDS.PROPERTY-NAME'),
			sort: true,
			filter: true,
			type: listColumnType.String
		},
		{
			column: 'propertyType',
			label: this.translate.instant('EMAIL-EDITOR.EMAIL-VARIABLE.FIELDS.PROPERTY-TYPE'),
			sort: true,
			filter: true,
			type: listColumnType.String
		},
		{
			column: 'defaultValue',
			label: this.translate.instant('EMAIL-EDITOR.EMAIL-VARIABLE.FIELDS.PROPERTY-TYPE'),
			sort: true,
			filter: true,
			type: listColumnType.String
		},
		{
			column: 'actions',
			label: this.translate.instant('EMAIL-GENERAL.ACTIONS.ACTIONS'),
			sort: false,
			filter: false,
			type: listColumnType.String
		}
	];

	selectedColumns = this.columns;
	displayedColumns: string[] = this.columns.map((obj) => { return obj.column });


	entityName: string = 'EmailVariable';
	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public emailvariableService: EmailVariableService,
		public variableTypedataService: EmailVariablTypeService,
		public errorService: ErrorService,
		private translate: TranslateService,
		public emailService: EmailTemplateService,
	) {
		super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, emailvariableService, errorService)
	}

	ngOnInit() {
		this.setAssociation();
		this.primaryKeys = ["id"];
		super.ngOnInit();
	}
	setAssociation() {

		this.associations = [
		];
	}

	addNew() {
		super.addNew(EmailVariableNewComponent);
	}

	delete(item: IEmailVariable): void {
		const search: ISearchField = {
			fieldName: 'contentJson',
			searchValue: '{{' + item.propertyName + '}}',
			operator: operatorType.Contains
		};
		const searches = [search];
		this.emailService.getAll(searches).subscribe(templates => {
			const emailTemplates = templates;
			if (emailTemplates.length > 0) {
				let templates = null;
				emailTemplates.forEach(template => {
					if (templates)
						templates = templates + ', ' + template.templateName;
					else
						templates = template.templateName;
				});
				this.deleteDialogRef = this.dialog.open(ConfirmDialogComponent, {
					disableClose: true,
					data: {
						title: this.translate.instant('EMAIL-EDITOR.INFORMATION'),
						action: this.translate.instant('EMAIL-GENERAL.ACTIONS.OK'),
						message: this.translate.instant('EMAIL-EDITOR.EMAIL-VARIABLE.ERROR.DELETE-WITH-TEMPLATE', {propertyName: item.propertyName, tempaltes: templates})
					}
				});
			} else {
				super.delete(item);
			}
		});
	}

	/**
	 * Calls service method to delete item.
	 * @param item Item to be deleted.
	 */
	deleteItem(item: IEmailVariable) {
		let id = ServiceUtils.encodeIdByObject(item, this.primaryKeys);
		this.dataService.delete(id).subscribe(result => {
			if (result) {
				alert(this.translate.instant('EMAIL-EDITOR.DATA-SOURCE.ERRORS.DELETE'));
			} else {
				let r = result;
				const index: number = this.items.findIndex(x => ServiceUtils.encodeIdByObject(x, this.primaryKeys) == id);
				if (index !== -1) {
					this.items.splice(index, 1);
					this.items = [...this.items];
					this.changeDetectorRefs.detectChanges();
				}
			}
		}, error => {
		});
	}

}
