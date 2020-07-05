import { Injectable, Inject } from '@angular/core';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'projects/fast-code-core/src/lib/common/components/confirm-dialog/confirm-dialog.component';

@Injectable({
    providedIn : 'root'
})
export class DialogeService {
    isMediumDeviceOrLess: boolean;
    dialogRef: MatDialogRef<any>;
    deleteDialogRef: MatDialogRef<ConfirmDialogComponent>;
    mediumDeviceOrLessDialogSize: string = "100%";
    largerDeviceDialogWidthSize: string = "85%";
    largerDeviceDialogHeightSize: string = "85%";

    constructor(
        public dialog: MatDialog,
        // @Inject(MAT_DIALOG_DATA) public data: any,
    ) {
        
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

    onCancel() {
        this.dialogRef.close(null);
    }

    confirmDialoge(data:any) {
      let tempData = {
        confirmationType: "confirm"
      }
      data = {...tempData,...data};
      this.dialogRef = this.dialog.open(ConfirmDialogComponent,{
        disableClose: true,
        data: data
      })
    }
}