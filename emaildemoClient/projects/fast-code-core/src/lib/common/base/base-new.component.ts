import { Component, OnInit, Inject, HostListener } from '@angular/core';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';

import { GenericApiService } from '../core/generic-api.service';
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, FormGroup } from '@angular/forms';
import { first } from 'rxjs/operators';
import { of as observableOf, Observable } from 'rxjs';

import { Globals } from '../../globals';

import { IAssociationEntry } from '../core/iassociationentry';
import { PickerDialogService } from '../../common/components/picker/picker-dialog.service';
import { ISearchField, operatorType } from '../../common/components/list-filters/ISearchCriteria';
import { IGlobalPermissionService } from '../core/iglobal-permission.service';

import { CanDeactivateGuard } from '../core/can-deactivate.guard';
import { ErrorService } from '../core/error.service';
import { PropertyType } from "projects/ip-email-builder/src/lib/email-editor/email-variable/property-type";
import { DatePipe } from "@angular/common";
import { FastCodeCoreService } from "projects/fast-code-core/src/lib/fast-code-core.service";
import { EmailVariableService } from "projects/ip-email-builder/src/lib/email-editor/email-variable/email-variable.service";
import { ConfirmDialogComponent } from '../components/confirm-dialog/confirm-dialog.component';
@Component({

  template: '',
  providers: [DatePipe]


})
export class BaseNewComponent<E> implements OnInit, CanDeactivateGuard {

  // @HostListener allows us to also guard against browser refresh, close, etc.
  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    // returning true will navigate without confirmation
    // returning false will show a confirm dialog before navigating away
    if (this.itemForm.touched) {
      return false
    }
    return true;
  }

  itemForm: FormGroup;
  loading = false;
  submitted = false;
  title: string = "title";

  pickerDialogRef: MatDialogRef<any>;

  associations: IAssociationEntry[];
  parentAssociations: IAssociationEntry[];

  entityName: string = "";
  IsReadPermission: Boolean = true;
  IsCreatePermission: Boolean = true;
  IsUpdatePermission: Boolean = true;
  IsDeletePermission: Boolean = true;
  globalPermissionService: IGlobalPermissionService;

  isMediumDeviceOrLess: boolean;
  dialogRef: MatDialogRef<any>;
  deleteDialogRef: MatDialogRef<ConfirmDialogComponent>;
  mediumDeviceOrLessDialogSize: string = "100%";
  largerDeviceDialogWidthSize: string = "85%";
  largerDeviceDialogHeightSize: string = "85%";

  errorMessage = '';

  	selectedVariableType: any;
    selectedDropDownValue: any;
    listData:string[]=[];
    fileIds:number[]=[];
    attatchment: { 
      myFile?:File;
      url?:any;
    }[] = [];

  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public global: Globals,
    public pickerDialogService: PickerDialogService,
    public dataService: GenericApiService<E>,
    public errorService: ErrorService,
    public datePipe: DatePipe,
    ) { }

  setPermissions = () => {

    if (this.globalPermissionService) {
      let entityName = this.entityName.startsWith("I") ? this.entityName.substr(1) : this.entityName;
      this.IsCreatePermission = this.globalPermissionService.hasPermissionOnEntity(entityName, "CREATE");
      if (this.IsCreatePermission) {
        this.IsReadPermission = true;
        this.IsDeletePermission = true;
        this.IsUpdatePermission = true;
      } else {
        this.IsDeletePermission = this.globalPermissionService.hasPermissionOnEntity(entityName, "DELETE");
        this.IsUpdatePermission = this.globalPermissionService.hasPermissionOnEntity(entityName, "UPDATE");
        this.IsReadPermission = (this.IsDeletePermission || this.IsUpdatePermission) ? true : this.globalPermissionService.hasPermissionOnEntity(entityName, "READ");
      }
    }
    //});
  }
  ngOnInit() {
    // this.setPermissions();
    this.manageScreenResizing();
  }

  manageScreenResizing() {
    this.global.isMediumDeviceOrLess$.subscribe(value => {
      this.isMediumDeviceOrLess = value;
      if (this.dialogRef)
        this.dialogRef.updateSize(value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
          value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize);
    });
  }

  onSubmit() {
    // stop here if form is invalid
    let check=true;
    //doing so for images
    if (this.itemForm.invalid) {
      return;
    }

    this.submitted = true;
    this.loading = true;
    switch(this.selectedVariableType)
		{
      case PropertyType.DATE:
		  this.itemForm.controls.defaultValue.setValue(this.datePipe.transform(this.itemForm.controls.defaultValue.value, this.selectedDropDownValue));
      break;
      case PropertyType.LIST:
      if(this.listData && this.listData.length>0)
        {
          this.itemForm.controls.defaultValue.setValue(this.listData.join(','));
        }
      break;
      case PropertyType.IMAGE:
      case PropertyType.CLICKABLE_IMAGE:
      case PropertyType.LIST_OF_IMAGES:
      check=false;
      this.saveAttachments();
      //need to check this code
      break;
    }
    if(check)
      {
    this.dataService.create(this.itemForm.getRawValue())
      .pipe(first())
      .subscribe(
        data => {
          // this.alertService.success('Registration successful', true);
          // this.router.navigate(['/users']);
          this.dialogRef.close(data);
        },
        error => {
          this.errorService.showError("Error Occured while updating");
          this.loading = false;
          this.dialogRef.close(null)

        });
      }
  }
  onCancel(): void {
    this.dialogRef.close(null);
  }

  addNew(component) {
      this.openDialog(component, null);
      return;
  }

  openDialog(component, data) {
    this.dialogRef = this.dialog.open(component, {
      disableClose: true,
      height: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize,
      width: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
      maxWidth: "none",
      panelClass: 'fc-modal-dialog',
      data: data
    });
    this.dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // this.getItems();
      }
    });
  }

  selectAssociation(association: IAssociationEntry) {
    this.initializePickerPageInfo();
    association.data = [];
    association.service.getAll(association.searchValue, this.currentPickerPage * this.pickerPageSize, this.pickerPageSize).subscribe(items => {
      this.initializePickerPageInfo(); // resetting the picker page info in case callback order is messed up
      this.isLoadingPickerResults = false;
      association.data = items;
      this.updatePickerPageInfo(items);
    },
      error => {
        this.errorMessage = <any>error;
        this.errorService.showError("An error occured while fetching results");
      }
    );
  }
  isLoadingPickerResults = true;

  currentPickerPage: number;
  pickerPageSize: number;
  lastProcessedOffsetPicker: number;
  hasMoreRecordsPicker: boolean;

  searchValuePicker: ISearchField[] = [];
  pickerItemsObservable: Observable<any>;

  initializePickerPageInfo() {
    this.hasMoreRecordsPicker = true;
    this.pickerPageSize = 30;
    this.lastProcessedOffsetPicker = -1;
    this.currentPickerPage = 0;
  }

  //manage pages for virtual scrolling
  updatePickerPageInfo(data) {
    if (data.length > 0) {
      this.currentPickerPage++;
      this.lastProcessedOffsetPicker += data.length;
    }
    else {
      this.hasMoreRecordsPicker = false;
    }
  }

  onPickerScroll(association: IAssociationEntry) {
    if (!this.isLoadingPickerResults && this.hasMoreRecordsPicker && this.lastProcessedOffsetPicker < association.data.length) {
      this.isLoadingPickerResults = true;
      association.service.getAll(association.searchValue, this.currentPickerPage * this.pickerPageSize, this.pickerPageSize).subscribe(
        items => {
          this.isLoadingPickerResults = false;
          association.data = association.data.concat(items);
          this.updatePickerPageInfo(items);
        },
        error => {
          this.errorMessage = <any>error;
          this.errorService.showError("An error occured while fetching more results");
        }
      );
    }
  }

  onPickerSearch(searchValue: string, association: IAssociationEntry) {

    let searchField: ISearchField = {
      fieldName: association.referencedDescriptiveField,
      operator: operatorType.Contains,
      searchValue: searchValue ? searchValue : ""
    }
    association.searchValue = [searchField];
    this.selectAssociation(association);
  }

  setPickerSearchListener() {
    this.associations.forEach(association => {
      if (!association.isParent) {
        this.itemForm.get(association.descriptiveField).valueChanges.subscribe(value => this.onPickerSearch(value, association));
      }
    })
  }

  onAssociationOptionSelected(event: MatAutocompleteSelectedEvent, association: IAssociationEntry) {
    let selectedOption = event.option.value;
    association.column.forEach(col => {
      this.itemForm.get(col.key).setValue(selectedOption[col.referencedkey]);
    });
    this.itemForm.get(association.descriptiveField).setValue(selectedOption[association.referencedDescriptiveField]);
  }


  checkPassedData() {
    if (this.data) {
      this.itemForm.patchValue(this.data);
    }
  }

  saveAttachments()
  {
    if (this.attatchment && this.attatchment.length > 0) {
      this.attatchment.forEach(data => {

        if (data.myFile.name ) {
          const fileMetadata = {
            name: data.myFile.name, summary: data.myFile.name
          };
          this.dataService.createFileMetadata(fileMetadata).subscribe(res => {
            console.log("response is",res);
            this.dataService.uploadFile(res.id, data.myFile).subscribe(res2=>{
              this.fileIds.push(res.id);
            this.itemForm.controls.defaultValue.setValue(this.fileIds.join(','));
            });
            

          });
        }
      });
    }

        setTimeout(() =>this.dataService.create(this.itemForm.getRawValue())
      .pipe(first())
      .subscribe(
        data => {
          // this.alertService.success('Registration successful', true);
          // this.router.navigate(['/users']);
          this.dialogRef.close(data);
        },
        error => {
          this.errorService.showError("Error Occured while updating");
          this.loading = false;
          this.dialogRef.close(null)

        }), 3000);

  }

}