import { Component, OnInit, Output, EventEmitter, Inject } from "@angular/core";
import { MAT_DIALOG_DATA } from "@angular/material/dialog";
import { DialogeService } from "../Services/dialoge.service";
@Component({
  selector: "data-source-table",
  templateUrl: "./data-source-table.html",
})
export class DataSourceTableComponent implements OnInit {
  dataToPreview: any[];
  displayedColumns: any[];
  title: string = "Preview Table";
  tableSource: any;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public _dialoge : DialogeService,
  ) {
      this.displayedColumns = [];
      this.dataToPreview = [];
      if (this.data && Object.keys(this.data).length > 0) {
        this.tableSource = this.data;
        this.displayedColumns = this.tableSource.metaData.map(
          (obj) => obj.metaColumn
        );

        let singleArray = [];
        let singleColumn = false;
        for (let data of this.tableSource.dataToPreview) {
          if (!Array.isArray(data)) {
            let val = data;
            singleColumn = true;
            data = [];
            data[this.displayedColumns[0]] = val;
            singleArray.push(data);
          } else {
            for (let index in data) {
              data[this.displayedColumns[index]] = data[index];
            }
          }
        }

        if (singleColumn) {
          this.tableSource.dataToPreview = [];
          this.tableSource.dataToPreview = singleArray;
        }
      }
      console.log("table Source",this.tableSource);
  }

  ngOnInit() {
    // super.ngOnInit()
  }

  onCancel() {
   this._dialoge.onCancel();
  }
}
