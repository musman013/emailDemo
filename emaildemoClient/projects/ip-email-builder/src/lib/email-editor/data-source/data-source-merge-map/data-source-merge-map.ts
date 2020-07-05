import { Component, OnInit, Inject } from '@angular/core';
import { DialogeService } from '../Services/dialoge.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { GenericApiService } from 'projects/fast-code-core/src/public_api';
import { DataSourceService } from '../Services/data-source.service';

@Component({
    selector: 'data-source-merge-map',
    templateUrl: './data-source-merge-map.html',
    styleUrls: ['./data-source-merge-map.scss']
})
export class DataSourceMergeMap implements OnInit{

    dataToPreview: any[];
    displayedColumns: any[] = ['Merge Field', 'Meta List','Already Mapped'];
    title: string = "Merge Field & DataSource  Mapping";
    tableSource: any;
    totalMergeField:number=0;
    mappedMergeField:number=0;
    constructor(
        public _dialoge : DialogeService,
        @Inject(MAT_DIALOG_DATA) public data: any,
        public dataService: DataSourceService
    ) { 
        console.log(data);
        
    }

    ngOnInit(): void {
        this.data.data.forEach(element => {
            let obj = [];
            this.totalMergeField=element.totalMergeField;
            this.mappedMergeField=element.mappedMergeField;
            if(element.alreadyMappedList) {
                element.alreadyMappedList.forEach(ele => {
                    obj.push(ele.id);
                });
                element.dataSourceMetaList = element.dataSourceMetaList.filter(res=>{
                    return !obj.includes(res.id);
                })
            }
        });
     }

    onCancel() {
        this._dialoge.onCancel();
    }

    dropDownValueChanged(event, element) {
        let obj = {};
        element.dataSourceMetaList = element.dataSourceMetaList.filter(res=>{
            obj = event.value;
            return res != event.value;
        })
        if(element.alreadyMappedList){
            element.alreadyMappedList.push(obj);
        } else {
            element.alreadyMappedList = [];
            element.alreadyMappedList.push(obj);
        }
    }

    removeAlreadyAddedMap(event,element) {
        console.log(event);
        let obj = {};
        element.alreadyMappedList = element.alreadyMappedList.filter(res=>{
            obj = event;
            return res != event;
        })
        if(element.dataSourceMetaList){
            element.dataSourceMetaList.push(obj);
        } else {
            // element.dataSourceMetaList = [];
            element.dataSourceMetaList.push(obj);
        }
    }

    save() {
        let url = '/email/mapping/create'
        let data=this.getDataToSend();
        if(data && data.length>0)
            {
        this.dataService.post(url, data).subscribe(res=>{
            this._dialoge.onCancel();
        })
            }
    else
        {
            alert("No combination exist");
        }
    }

    getDataToSend() : any[] {
        let tempArr = [];
        this.data.data.forEach(element => {
            if(element.alreadyMappedList) {
                element.alreadyMappedList.forEach(ele => {
                    let obj = {};
                    obj['emailTemplateId'] = this.data.emailTemplateId;
                    obj['mergeFieldId'] = element.mergeField.id;
                    obj['dataSourceId'] = ele.dataSourceEntity.id;
                    obj['dataSourceMetaId'] = ele.id;
                    tempArr.push(obj);   
                }); 
            }
        });
        return tempArr;
    }
}
