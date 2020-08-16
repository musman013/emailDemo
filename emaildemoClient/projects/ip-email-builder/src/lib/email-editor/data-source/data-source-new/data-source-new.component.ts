import { Component, OnInit, Inject, ViewChild } from "@angular/core";

import { ActivatedRoute, Router } from "@angular/router";
import {
  FormBuilder,
  Validators,
  FormArray,
  FormControl,
  FormGroup,
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
} from "projects/fast-code-core/src/public_api";
import { DatePipe } from "@angular/common";
import { ValidatorsService } from "src/app/validators.service";
import { DataSourceService } from "projects/ip-email-builder/src/lib/email-editor/data-source/Services/data-source.service";
import { IDataSource } from "projects/ip-email-builder/src/lib/email-editor/data-source/Models/IDataSource";
import { EmailVariablTypeService } from "projects/ip-email-builder/src/lib/email-editor/email-variable/email-variable.type.service";
import { DataSourceTableComponent } from "../data-source-table/data-source-table";
import { DialogService } from "../Services/dialog.service";
import { EmailTemplateService } from "../../email-template.service";
import { TranslateService } from '@ngx-translate/core';

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
  title: string = this.translate.instant('EMAIL-EDITOR.DATA-SOURCE.ADD-TITLE');
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
    public _dialog: DialogService,
    public emailTemplateService: EmailTemplateService,
    public translate: TranslateService,
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
      errorService
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
    this.emailTemplateService.getAllTemplates().subscribe((data) => {
      this.emailTemplates = data;
    });

    this.itemForm.get("sqlQuery").valueChanges.subscribe(() => {
      this.previewAvailable = false;
    });

    this.selectionChangeDetection();
  }

  selectionChangeDetection() {
    let emailTemplate:FormGroup = this.itemForm.get('emailTemplate') as FormGroup;
    emailTemplate.valueChanges.subscribe(data=>{
      console.log(data);
      if(data.id) {
        let url = `/datasource/getAlreadyMappedDatasourceForEmailTemplate/${data.id}`;
        this.dataService.get(url).subscribe(res => {
          console.log(res);
          if(res.fields == "NORECORD") {
            
          } else {
            let id:FormControl = emailTemplate.get('id') as FormControl;
            id.reset();
            this.errorService.showError(this.translate.instant('EMAIL-EDITOR.DATA-SOURCE.ERROS.ALREADY-MAPPED', {fields: res.fields}));
          }
        })
      }
    })
  }

  previewData() {
    let sqlQuery = this.itemForm.controls.sqlQuery.value;
    if (sqlQuery == "") {
      alert(this.translate.instant('EMAIL-EDITOR.DATA-SOURCE.ERRORS.NO-QUERY'));
      return;
    }
    this.dataService.previewData(sqlQuery).subscribe((data) => {
      if (data && data.valid) {
        this.showMessage = false;
        this.errorMessage = "";
        this.previewAvailable = true;
        this._dialog.openDialog(DataSourceTableComponent,data);                                                                                                                                                                                                 
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
