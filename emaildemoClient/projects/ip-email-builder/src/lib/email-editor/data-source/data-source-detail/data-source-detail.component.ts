import { Component, OnInit, ViewChild } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, Validators, FormArray, FormControl } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import {
  BaseDetailsComponent,
  Globals,
  PickerDialogService,
  ErrorService,
} from "projects/fast-code-core/src/public_api"; // 'fastCodeCore';
import { EmailVariablTypeService } from "projects/ip-email-builder/src/lib/email-editor/email-variable/email-variable.type.service";
import { PropertyType } from "projects/ip-email-builder/src/lib/email-editor/email-variable/property-type";
import {
  NativeDateAdapter,
  DateAdapter,
  MAT_DATE_FORMATS,
  MAT_DATE_LOCALE,
} from "@angular/material";
import { formatDate, DatePipe } from "@angular/common";
import { ValidatorsService } from "src/app/validators.service";
import { EmailVariableService } from "projects/ip-email-builder/src/lib/email-editor/email-variable/email-variable.service";
import { IDataSource } from "projects/ip-email-builder/src/lib/email-editor/data-source/Models/IDataSource";
import { DataSourceService } from "projects/ip-email-builder/src/lib/email-editor/data-source/Services/data-source.service";
import { IDataSourceMeta } from "projects/ip-email-builder/src/lib/email-editor/data-source/Models/DataSourceMeta";
import { take } from "rxjs/operators";
import { DataSourceTableComponent } from "../data-source-table/data-source-table";
import { DialogeService } from "../Services/dialoge.service";

@Component({
  selector: "lib-data-source-detail",
  templateUrl: "./data-source-detail.component.html",
  styleUrls: ["./data-source-detail.component.scss"],
  providers: [DatePipe],
})
export class DataSourceDetailComponent extends BaseDetailsComponent<IDataSource> implements OnInit {
  readOnlyQuery: boolean;
  metaList: IDataSourceMeta[];

  title: string = "DataSource";
  parentUrl: string = "./datasources";
  entityName: string = "DataSource";
  metaData: any;
  dataToPreview: any;
  emailTemplates: any[] = [];
  showSave:boolean = false;

    showMessage:boolean;
  errorMessage:any;
  	displayedColumns: string[] = ['columnName','dataType']

  @ViewChild('myEditor',{static:false}) myEditor;

  public previewAvailable:boolean = false;
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public global: Globals,
    public variableTypedataService: EmailVariablTypeService,
    public pickerDialogService: PickerDialogService,
    public dataService: DataSourceService,
    public errorService: ErrorService,
    public datePipe: DatePipe,
    public _dialoge: DialogeService
  ) {
    super(
      formBuilder,
      router,
      route,
      dialog,
      global,
      pickerDialogService,
      dataService,
      errorService,
      datePipe
    );
    var u = this.route.parent.toString();
    this.dataService.tableClose$.subscribe(res=>{
      if(res) {
        this.onCancel();
        this.dataService.changetableClose(false);
      }
    })
  }

  ngOnInit() {
    super.ngOnInit();
    this.setForm();
    this.getItem();
    this.dataService.getAllTemplates().subscribe((data) => {
      this.emailTemplates = data;
  });
    	this.itemForm.get('sqlQuery').valueChanges.subscribe(()=>{
		this.previewAvailable = false;
  })
  
  }



  ngAfterViewInit():void
  {
    console.log('after init',this.myEditor);
     //this.getChangeContent();
  }

  setForm() {
    this.itemForm = this.formBuilder.group({
      id:[""],
		name: ["", [Validators.required]],
		emailTemplate: this.formBuilder.group({
		  id: ["", Validators.required],
		  templateName: [""],
		}),
	    sqlQuery: ["", [Validators.required, ValidatorsService.sqlQuery]],
		  creation: [""],
      dataSourceMetaList: this.formBuilder.array([]),
      readOnlyQuery:[""]
    });
  }

  onItemFetched(item: IDataSource) {
    this.item = item;
	this.itemForm.patchValue(this.item);
  this.metaList = this.item.metaList;
  console.log("item is ",this.item);
  this.readOnlyQuery=this.item.readOnlyQuery;

	// this.itemForm.valueChanges.subscribe(()=>{
	// 	this.showSave = true;
	// })
  }

  previewData() {
	let sqlQuery = this.itemForm.controls.sqlQuery.value;
	if(sqlQuery == "") {
		alert("Please enter any query");
		return;
	}
	this.dataService.previewData(sqlQuery).subscribe((data) => {	
		if (data && data.valid) {
       this.showMessage=false;
      this.errorMessage='';
      this.previewAvailable = true;
      this._dialoge.openDialog(DataSourceTableComponent,data)
			this.metaData = data.metaData;
      this.addControls();
      this.showSave=true;
		} else {
      //alert(data.message);
      this.showMessage=true;
      this.errorMessage=data.message;
		}
	});
  }

  addControls() {
  const arr = this.itemForm.get('dataSourceMetaList') as FormArray;
  arr.clear();
	this.metaData.forEach(element => {
		let temp = this.formBuilder.group({
			metaColumn: new FormControl(element.metaColumn),
			metaColumnDataType: new FormControl(element.metaColumnDataType)
		})
    arr.push(temp);
	});
  }


    getChangeContent()
  {
const editor = this.myEditor.codeMirror;
 
const doc = editor.getDoc();
this.searchKeyword(doc,editor,'SELECT');
this.searchKeyword(doc,editor,'FROM');
this.searchKeyword(doc,editor,'WHERE');

  }

  searchKeyword(doc,editor,key)
  {
    var cursor = doc.getSearchCursor(key,null,`caseFold: false`);
while (cursor.findNext()) {
        editor.markText(
          cursor.from(),
          cursor.to(),
          { css:"color: red"  }
        );
    }

  }

}
