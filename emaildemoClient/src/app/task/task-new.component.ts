import { Component, OnInit, Inject } from '@angular/core';
import { TaskService } from './task.service';
import { ITask } from './itask';

import { ActivatedRoute,Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { first } from 'rxjs/operators';
import { Globals, BaseNewComponent, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { AppsService } from '../apps/apps.service';
import { DatePipe } from "@angular/common";
import { EmailFileService } from "projects/ip-email-builder/src/lib/email-editor/email-file.service";
import { FastCodeCoreService } from "projects/fast-code-core/src/lib/fast-code-core.service";

@Component({
  selector: 'app-task-new',
  templateUrl: './task-new.component.html',
  styleUrls: ['./task-new.component.scss'],
  providers:[DatePipe]
})
export class TaskNewComponent extends BaseNewComponent<ITask> implements OnInit {
  
    title:string = "New Task";
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<TaskNewComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		public global: Globals,
		public pickerDialogService: PickerDialogService,
		public dataService: TaskService,
		public errorService: ErrorService,
		public appsService: AppsService,
		public datePipe: DatePipe) {
		super(formBuilder, router, route, dialog, dialogRef, data, global, pickerDialogService, dataService, errorService,datePipe);
	}
 
	ngOnInit() {
		this.entityName = 'Task';
		this.setAssociations();
		super.ngOnInit();
    this.setForm();
		this.checkPassedData();
		this.setPickerSearchListener();
  }
 		
	setForm(){
 		this.itemForm = this.formBuilder.group({
      description: [''],
      status: [''],
      type: [''],
      appId: [''],
      appsDescriptiveField : [''],
    });
	}
	 
	setAssociations(){
  	
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
				table: 'apps',
				type: 'ManyToOne',
				service: this.appsService,
				descriptiveField: 'appsDescriptiveField',
				referencedDescriptiveField: 'name',
		    
			},
		];
		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});

	}  
    
}
