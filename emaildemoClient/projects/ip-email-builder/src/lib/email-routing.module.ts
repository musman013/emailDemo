import { Routes, RouterModule } from "@angular/router";
import { NgModule } from "@angular/core";
//import { EmailTemplateListComponent,TemplateEditorComponent,EmailVariableNewComponent,EmailVariableListComponent,
 //   EmailVariableDetailComponent } from "./email-editor/index"; EmailListComponent
 //import { EmailListComponent } from "./email-editor/email-list.component";
 import { EmailTemplateListComponent } from "./email-editor/email-template-list.component";
 import { TemplateEditorComponent } from "./email-editor/template-editor.component";
 import { EmailVariableListComponent } from "./email-editor/email-variable/email-variable-list.component";
 import { EmailVariableNewComponent } from "./email-editor/email-variable/email-variable-new.component";
 import { EmailVariableDetailComponent } from "./email-editor/email-variable/email-variable-detail.component";
 import { DataSourceListComponent } from "projects/ip-email-builder/src/lib/email-editor/data-source/data-source-list/data-source-list.component";
 import { DataSourceDetailComponent } from "projects/ip-email-builder/src/lib/email-editor/data-source/data-source-detail/data-source-detail.component";

export const EmailRoutes: Routes = [
    { path: 'emailtemplates', component: EmailTemplateListComponent },
    { path: 'emailtemplates/:id', component: TemplateEditorComponent },
    { path: 'emailtemplate', component: TemplateEditorComponent },
   // { path: 'templateeditor', component: TemplateEditorComponent },
   // { path: 'email', component: TemplateEditorComponent },
    { path: 'emailvariables', component: EmailVariableListComponent  },
     { path: 'emailvariable', component: EmailVariableNewComponent   },
     { path: 'emailvariables/:id', component: EmailVariableDetailComponent },
      { path: 'datasources', component: DataSourceListComponent  },
      { path: 'datasources/:id', component: DataSourceDetailComponent  },
    
    ];
/*@NgModule({
imports: [RouterModule.forChild(EmailRoutes)],
exports: [RouterModule]
})
export class EmailRoutingModule { }*/
