import { Component, OnInit, Inject, ViewChild } from "@angular/core";

import { ActivatedRoute, Router } from "@angular/router";
import {
  FormBuilder,
  Validators,
  FormArray,
  FormControl,
} from "@angular/forms";
import {
  MatDialogRef,
  MatDialog,
  MAT_DIALOG_DATA,
} from "@angular/material/dialog";
import {
  BaseNewComponent,
  Globals,
  PickerDialogService,
  ErrorService,
} from "projects/fast-code-core/src/public_api"; // 'fastCodeCore';

import { PropertyType } from "projects/ip-email-builder/src/lib/email-editor/email-variable/property-type";

import {
  NativeDateAdapter,
  DateAdapter,
  MAT_DATE_FORMATS,
  MAT_DATE_LOCALE,
} from "@angular/material";
import { formatDate, DatePipe } from "@angular/common";
import { ValidatorsService } from "src/app/validators.service";
import { FastCodeCoreService } from "projects/fast-code-core/src/lib/fast-code-core.service";
import { DataSourceService } from "projects/ip-email-builder/src/lib/email-editor/data-source/Services/data-source.service";
import { IDataSource } from "projects/ip-email-builder/src/lib/email-editor/data-source/Models/IDataSource";
import { EmailVariablTypeService } from "projects/ip-email-builder/src/lib/email-editor/email-variable/email-variable.type.service";
import { DataSourceTableComponent } from "../data-source-table/data-source-table";
import { take } from "rxjs/operators";
import { DialogeService } from "../Services/dialoge.service";

// import * as CodeMirror from 'codemirror';
// import 'codemirror/mode/sql/sql.js';
// import { WindowRef } from './WindowRef';

@Component({
  selector: "lib-data-source-new",
  templateUrl: "./data-source-new.component.html",
  styleUrls: ["./data-source-new.component.scss"],
  providers: [DatePipe],
})
export class DataSourceNewComponent extends BaseNewComponent<IDataSource>
  implements OnInit {
  metaData: any;
  dataToPreview: any;
  emailTemplates: any[] = [];
  title: string = "Add Data Source";
  entityName: string = "DataSource";
  public previewAvailable: boolean = false;
  showMessage: boolean;
  errorMessage: any;

  @ViewChild("myEditor", { static: false }) myEditor;

  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<DataSourceNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public global: Globals,
    public pickerDialogService: PickerDialogService,
    public dataService: DataSourceService,
    public variableTypedataService: EmailVariablTypeService,
    public errorService: ErrorService,
    public datePipe: DatePipe,
    public _dialoge: DialogeService
  ) {
    super(
      formBuilder,
      router,
      route,
      dialog,
      dialogRef,
      // data,
      global,
      pickerDialogService,
      dataService,
      errorService,
      datePipe
    );

    this.dataService.tableClose$.subscribe(res=>{
      if(res) {
        this.onCancel();
        this.dataService.changetableClose(false);
      }
    })
  }

  ngOnInit() {
    this.itemForm = this.formBuilder.group({
      name: ["", [Validators.required]],
      emailTemplate: this.formBuilder.group({
        id: ["", Validators.required],
        templateName: [""],
      }),
      sqlQuery: ["", [Validators.required, ValidatorsService.sqlQuery]],
      creation: [""],
      dataSourceMetaList: this.formBuilder.array([]),
    });
    super.ngOnInit();
    this.checkPassedData();
    this.dataService.getAllTemplates().subscribe((data) => {
      this.emailTemplates = data;
    });

    this.itemForm.get("sqlQuery").valueChanges.subscribe(() => {
      this.previewAvailable = false;
    });
  }

  previewData() {
    let sqlQuery = this.itemForm.controls.sqlQuery.value;
    if (sqlQuery == "") {
      alert("Please enter any query");
      return;
    }
    this.dataService.previewData(sqlQuery).subscribe((data) => {
      if (data && data.valid) {
        this.showMessage = false;
        this.errorMessage = "";
        this.previewAvailable = true;
        this._dialoge.openDialog(DataSourceTableComponent,data);                                                                                                                                                                                                 
        this.dataToPreview = data.dataToPreview;
        this.metaData = data.metaData;
        this.addControls();
      } else {
        this.showMessage = true;
        this.errorMessage = data.message
      }
    });
  }

  addControls() {
    const arr = this.itemForm.get("dataSourceMetaList") as FormArray;
    arr.clear();
    this.metaData.forEach((element) => {
      let temp = this.formBuilder.group({
        metaColumn: new FormControl(element.metaColumn),
        metaColumnDataType: new FormControl(element.metaColumnDataType),
      });
      arr.push(temp);
    });
  }

  getChangeContent() {
    const editor = this.myEditor.codeMirror;
    const doc = editor.getDoc();
    this.searchKeyword(doc, editor, "SELECT");
    this.searchKeyword(doc, editor, "FROM");
    this.searchKeyword(doc, editor, "WHERE");
  }

  searchKeyword(doc, editor, key) {
    var cursor = doc.getSearchCursor(key, null, `caseFold: false`);

    while (cursor.findNext()) {
      editor.markText(cursor.from(), cursor.to(), { css: "color: red" });
    } 
  }
}
