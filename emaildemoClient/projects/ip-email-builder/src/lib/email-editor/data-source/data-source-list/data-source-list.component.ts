import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { TranslateService } from '@ngx-translate/core';
import {
	BaseListComponent,
	Globals,
	IListColumn,
	listColumnType,
	PickerDialogService,
	ErrorService,
	operatorType, ISearchField, ConfirmDialogComponent, ServiceUtils
} from "projects/fast-code-core/src/public_api";
import { Router, ActivatedRoute } from "@angular/router";
import { EmailTemplateService } from "projects/ip-email-builder/src/lib/email-editor/email-template.service";
import { EmailVariablTypeService } from "projects/ip-email-builder/src/lib/email-editor/email-variable/email-variable.type.service";
import { EmailVariableService } from "projects/ip-email-builder/src/lib/email-editor/email-variable/email-variable.service";
import { EmailVariableNewComponent } from "projects/ip-email-builder/src/lib/email-editor/email-variable/email-variable-new.component";
import { IEmailVariable } from "projects/ip-email-builder/src/lib/email-editor/email-variable/iemail-variable";
import { IDataSource } from "projects/ip-email-builder/src/lib/email-editor/data-source/Models/IDataSource";
import { DataSourceService } from "projects/ip-email-builder/src/lib/email-editor/data-source/Services/data-source.service";
import { DataSourceNewComponent } from "projects/ip-email-builder/src/lib/email-editor/data-source/data-source-new/data-source-new.component";
import { DataSourceTableComponent } from '../data-source-table/data-source-table';
import { DialogeService } from '../Services/dialoge.service';

@Component({
	selector: 'lib-data-source-list',
	templateUrl: './data-source-list.component.html',
	styleUrls: ['./data-source-list.component.css']
})
export class DataSourceListComponent extends BaseListComponent<IDataSource> implements OnInit {


	title: string = "Data source";

	columns: IListColumn[] = [
		{
			column: 'name',
			label: this.translate.instant('EMAIL-EDITOR.DATA-SOURCE.FIELDS.NAME'),
			sort: true,
			filter: true,
			type: listColumnType.String
		},
		{
			column: 'emailTemplate',
			label: this.translate.instant('EMAIL-EDITOR.DATA-SOURCE.FIELDS.EMAIL-TEMPLATE'),
			sort: true,
			filter: true,
			type: listColumnType.String
		},
		{
			column: 'sqlQuery',
			label: this.translate.instant('EMAIL-EDITOR.DATA-SOURCE.FIELDS.SQL-QUERY'),
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


	entityName: string = 'DataSource';
	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public dataSourceService: DataSourceService,
		public variableTypedataService: EmailVariablTypeService,
		public errorService: ErrorService,
		private translate: TranslateService,
		public emailService: EmailTemplateService,
		public _dialog: DialogeService
	) {
		super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, dataSourceService, errorService);

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
		super.addNew(DataSourceNewComponent);
	}

	delete(item: IDataSource): void {
		let url = `/datasource/getAllMappedForMergeField/${item.id}`
		this.dataSourceService.get(url).subscribe(res => {
			console.log(res);
			if (res.fields == 'NORECORD') {
				super.delete(item);
			} else {
				let data = {
					message: `This datasource is mapped with ${res.fields} columns.Please remove mappings to delete this datasource.`,
					title: "Information",
					action: "OK"
				}
				this._dialog.confirmDialoge(data);
			}
		})

	}

	/**
	 * Calls service method to delete item.
	 * @param item Item to be deleted.
	 */
	deleteItem(item: IDataSource) {
		let id = ServiceUtils.encodeIdByObject(item, this.primaryKeys);
		this.dataService.delete(id).subscribe(result => {
			console.log("result is", result);
			let check = true;
			if (result) {
				alert("Datasource is already binded, Can Not delete");
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
