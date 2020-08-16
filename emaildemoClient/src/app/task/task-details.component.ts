import { Component, OnInit } from '@angular/core';
import { ActivatedRoute,Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { first } from 'rxjs/operators';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';

import { TaskService } from './task.service';
import { ITask } from './itask';
import { BaseDetailsComponent, Globals, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';

import { AppsService } from '../apps/apps.service';
import { DatePipe } from "@angular/common";


@Component({
  selector: 'app-task-details',
  templateUrl: './task-details.component.html',
  styleUrls: ['./task-details.component.scss']
})
export class TaskDetailsComponent extends BaseDetailsComponent<ITask> implements OnInit {
	title:string='Task';
	parentUrl:string='task';
	//roles: IRole[];  
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public global: Globals,
		public dataService: TaskService,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public appsService: AppsService,
		public datePipe:DatePipe
	) {
		super(formBuilder, router, route, dialog, global, pickerDialogService, dataService, errorService);
  }

	ngOnInit() {
		this.entityName = 'Task';
		this.setAssociations();
		super.ngOnInit();
		this.setForm();
    this.getItem();
    this.setPickerSearchListener();
  }
  
  setForm(){
    this.itemForm = this.formBuilder.group({
      description: [''],
      id: [{value: '', disabled: true}, Validators.required],
      status: [''],
      type: [''],
      appId: [''],
      appsDescriptiveField : [''],
      
    });
      
  }
  
  onItemFetched(item: ITask) {
    this.item = item;
		this.itemForm.patchValue(item);
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
		
		this.childAssociations = this.associations.filter(association => {
			return (association.isParent);
		});

		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});
	}
}
