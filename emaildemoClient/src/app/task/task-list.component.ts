import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

import { ITask } from './itask';
import { TaskService } from './task.service';
import { Router, ActivatedRoute } from '@angular/router';
import { TaskNewComponent } from './task-new.component';
import { BaseListComponent, Globals, IListColumn, listColumnType, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';

import { AppsService } from '../apps/apps.service';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss']
})
export class TaskListComponent extends BaseListComponent<ITask> implements OnInit {

	title:string = "Task";
	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public dataService: TaskService,
		public errorService: ErrorService,
		public appsService: AppsService,
	) { 
		super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, dataService, errorService)
  }

	ngOnInit() {
		this.entityName = 'Task';
		this.setAssociation();
		this.setColumns();
		this.primaryKeys = [ "id",  ]
		super.ngOnInit();
	}
  
  
	setAssociation(){
  	
		this.associations = [
			{
				column: [
                      {
					  	key: 'appId',
					  	value: undefined,
					  	referencedkey: 'id'
					  },
					  
				],
				isParent: false,
				descriptiveField: 'appsDescriptiveField',
				referencedDescriptiveField: 'name',
				service: this.appsService,
				associatedObj: undefined,
				table: 'apps',
				type: 'ManyToOne'
			},
		];
	}
  
  	setColumns(){
  		this.columns = [
    		{
				column: 'description',
				label: 'description',
				sort: true,
				filter: true,
				type: listColumnType.String
			},
    		{
				column: 'id',
				label: 'id',
				sort: false,
				filter: false,
				type: listColumnType.Number
			},
    		{
				column: 'status',
				label: 'status',
				sort: true,
				filter: true,
				type: listColumnType.String
			},
    		{
				column: 'type',
				label: 'type',
				sort: true,
				filter: true,
				type: listColumnType.String
			},
			{
	  			column: 'Apps',
				label: 'Apps',
				sort: false,
				filter: false,
				type: listColumnType.String
	  		},
		  	{
				column: 'actions',
				label: 'Actions',
				sort: false,
				filter: false,
				type: listColumnType.String
			}
		];
		this.selectedColumns = this.columns;
		this.displayedColumns = this.columns.map((obj) => { return obj.column });
  	}
	addNew() {
		super.addNew(TaskNewComponent);
	}
  
}
