import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {EmailTemplateService} from './email-template.service';
import {MailTemplateService} from './mail-template.service';
import {EmailVariableService} from './email-variable/email-variable.service';

import {
  IPDefaultEmail, Structure, TextBlock, ImageBlock, IpBlock, IBlocks, DividerBlock
} from '../classes';
import {IpEmailBuilderService} from '../ip-email-builder.service';
//'ip-email-builder';
import {BehaviorSubject, Observable} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {IEmailTemplate} from './iemail-template';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatSelect, MatSnackBar, MatDialog } from '@angular/material';
import {TranslateService} from '@ngx-translate/core';
import {map, startWith} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {EmailFileService} from './email-file.service';
import {environment} from '../environment';
import { ConfirmDialogComponent } from "projects/ip-email-builder/src/lib/components/dialog.component";

//import { DividerBlock, IBlockState } from 'ip-email-builder/public_api';
@Component({
  selector: 'email-template-editor',
  templateUrl: './template-editor.component.html',
  styleUrls: ['./template-editor.component.scss']
})
export class TemplateEditorComponent implements OnInit {
  api : string = '/api/files/';
  replaceapi : string = "/api/files/";
  emailTemplateId: string;
  title = 'Create Email Template';
  isLoading: BehaviorSubject<boolean>;
  emailTemplate?: IEmailTemplate;
  errorMessage = '';
  formGroup: FormGroup;
  editorView: boolean = false;

  entityName: string = 'Email';
  IsReadPermission: Boolean = false;
  IsCreatePermission: Boolean = false;
  IsUpdatePermission: Boolean = false;
  IsDeletePermission: Boolean = false;
  @ViewChild('subject', {read: ElementRef, static: true})
  subject: ElementRef;

  @ViewChild('category', {read: ElementRef, static: false})
  category: ElementRef;

  categoryList: string[];
  categories: Observable<string[]>;
  files: Set<File> = new Set();
  loadedFiles: Set<String> = new Set();
  loadedInlineImages: Set<String> = new Set();
  private inlineImages = new Set();

  constructor(
    private _ngb: IpEmailBuilderService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar,
    private emailtemplateService: EmailTemplateService,
    private mailtemplateService: MailTemplateService,
    private emailVariableService: EmailVariableService,
    private formBuilder: FormBuilder,
    private translate: TranslateService,
    private emailFileService: EmailFileService,
    private confirmDialog: MatDialog,
  ) {
    this.isLoading = _ngb.isLoading;
    _ngb.MergeTags = new Set(['tag22']); //new Set(['{{firstName}}', '{{lastName}}']);
  }

  private _filter(value: any): string[] {
    const filterValue = value.toLowerCase();
    return this.categoryList.filter(option => option.toLowerCase().includes(filterValue));
  }


  private setupCategory() {
    this.emailtemplateService.getAllCategories().subscribe(categories => {
      this.categoryList = categories;

      this.categories = this.formGroup.get('category').valueChanges.pipe(
        startWith(''),
        map(value => this._filter(value))
      );
    });


  }

  ngOnInit() {

    const param = this.route.snapshot.paramMap.get('id');
    this.emailTemplateId=param;
    this.setPermissions();
    this.emailVariableService.getAllWithoutPagination().subscribe(
      items => {
        let tags = items.map(item => item.propertyName);
        this._ngb.MergeTags = new Set(tags);
      },
      error => this.errorMessage = <any>error
    );
    this.formGroup = this.formBuilder.group({
      id: [''],
      templateName: ['', Validators.required],
      category: ['', Validators.required],
      subject: ['', Validators.required],
      to: ['', Validators.required],
      description: [''],
      attachmentpath: [''],
      active: [true, Validators.required]

    });
    if (param) {
      const id = +param;
      this.getEmailTemplate(id);
      this.editorView = true;
    } else {
      // const id = +param;
      //this.getEmailTemplate(id);
      this.emailTemplate = {} as IEmailTemplate;
      //<p class="ql-align-center">
      this._ngb.Email = new IPDefaultEmail({
        structures: [
          new Structure('cols_1',
            [

              [
                new ImageBlock(
                  'https://secureservercdn.net/198.71.190.232/rjq.996.myftpupload.com/wp-content/uploads/2018/07/Nav-Logo.png',
                  {
                    width: {value: 200, unit: 'px'},
                    height: {value: 50, unit: 'px'},

                  }
                ),

                new DividerBlock(null, {
                  disabled: false,
                  message: ''
                }),
                new TextBlock(
                  `${this.translate.instant('EMAIL-EDITOR.MESSAGES.SAMPLE-TEMPLATE')}`,
                  // `<p>Dear Email Editor,</p><p >  With this email template editor, you can edit your text, and add buttons,images and dividers.
                  //  The above logo image and dividers are for demo purpose, you can remove or replace them.
                  //  You can view the list of supported elements by clicking the Drag & Drop Content section of the page</p>
                  // <p>Thanks,</p>
                  // <p>John</p>`,
                  {
                    lineHeight: {
                      value: 22,
                      unit: 'px'
                    },
                    padding: {bottom: 10, left: 25, right: 25, top: 10}
                  }
                ),
                /*   new TextBlock(
                     `<h2 class="ql-align-center">${this.translate.instant('EMAIL-EDITOR.MESSAGES.SAMPLE-TEMPLATE2')}</h2>`
                   )*/


              ]
            ])
        ]
      });
    }
    this.setupCategory();
  }

  setPermissions = () => {
    this.IsReadPermission = true;
    this.IsDeletePermission = true;
    this.IsUpdatePermission = true;
    this.IsCreatePermission = true;

  };

  sendTestMail() {
    const to = prompt(this.translate.instant('EMAIL-EDITOR.MESSAGES.SELECT-RECEIVER-PROMPT'));
    if (!to) {
      return;
    }

    this.loadEmailTemplate();
    this.emailTemplate.to = to;
    let contentJson = this.emailTemplate.contentJson.replace(this.replaceapi , "cid:IMAGE");
    for(let i = 0; i < 20; i++){
      contentJson = contentJson.replace(this.replaceapi , "cid:IMAGE");
    }
    this.emailTemplate.contentJson = contentJson;
    //this.emailTemplate.contentJson = template;
    let attachments = [];
    // this.files.forEach(file =>{
    //   attachments.push({name: file.name, description: file.name});
    // });

    let inlinImages = [];
    this.inlineImages.forEach(file => {
      const id = file['src'].replace(this.replaceapi, '');
      inlinImages.push({name: id, description: file['src'], summary: 'IMAGE' + id});
    });

    this.emailTemplate.attachments = attachments;
    this.emailTemplate.inlineImages = inlinImages;
    this.mailtemplateService.create(this.emailTemplate)
      .subscribe(
        data => {
          var snackBarRef = this.snackBar.open(this.translate.instant('EMAIL-EDITOR.MESSAGES.EMAIL-SENT-SUCCESS'), this.translate.instant('EMAIL-GENERAL.ACTIONS.OK'), {
            duration: 1000,
            panelClass: ['snackbar-background']
          });
        },
        error => {
        });
  }

  loadEmail(structures: any) {
    structures.structures.forEach(structure => {
      structure.elements.forEach(element => {
        element.forEach(block => {
          if (block.type === 'image' && block.src && block.src != 'undefined') {
            this.inlineImages.add(block);
            this.loadedInlineImages.add(block.src);
            try {
              block.src = block.src.replace(this.replaceapi, '');
            } catch (error) {
              // ignore
            }
            block.src = this.api + block.src;
          }
        });
      });
    });
    this._ngb.Email = new IPDefaultEmail(structures);
  }

  getEmailTemplate(id: number) {
    this.emailtemplateService.getById(id).subscribe(
      templ => {
        this.emailTemplate = templ;
        this.formGroup.patchValue({
          id: templ.id,
          templateName: templ.templateName,
          subject: templ.subject,
          to: templ.to,
          contentJson: templ.contentJson,
          contentHtml: templ.contentHtml,
          category: templ.category,
          description: templ.description,
          attachments: templ.attachments,
          active: templ.active

        });
        this.files = new Set(templ.attachments);
        this.loadedFiles = new Set();
        templ.attachments.forEach(e => {
          this.loadedFiles.add(e.name);
        });
        this.loadEmail(JSON.parse(templ.contentJson));

      },
      error => this.errorMessage = <any>error);
  }


  resetTemplate()
{

   this.confirmDialog
      .open(ConfirmDialogComponent, {
        data: {
          message: this.translate.instant('EMAIL-BUILDER.MESSAGES.RESET-TITLE')
        }
      })
      .afterClosed()
      .subscribe(result => {
        console.log("reseut",result);
        if(result ==1)
          {
            this.resetEmailTemplate();
          }
      });

 
} 


resetEmailTemplate()
{
this.emailtemplateService.resetTemplateById(this.emailTemplateId).subscribe(
      templ => {
          var snackBarRef = this.snackBar.open(this.translate.instant('EMAIL-EDITOR.MESSAGES.RESET-SUCCESS'), this.translate.instant('EMAIL-GENERAL.ACTIONS.OK'), {
            duration: 1000,
            panelClass: ['snackbar-background']
          });

        this.emailTemplate = templ;
        this.formGroup.patchValue({
          id: templ.id,
          templateName: templ.templateName,
          subject: templ.subject,
          to: templ.to,
          contentJson: templ.contentJson,
          contentHtml: templ.contentHtml,
          category: templ.category,
          description: templ.description,
          attachments: templ.attachments,
          active: templ.active

        });
        this.files = new Set(templ.attachments);
        this.loadedFiles = new Set();
        templ.attachments.forEach(e => {
          this.loadedFiles.add(e.name);
        });
        this.loadEmail(JSON.parse(templ.contentJson));

      },
      error => this.errorMessage = <any>error);
}

saveEmail = () => {
    if (!this._ngb.IsChanged) {
      this._ngb.notify(this.translate.instant('EMAIL-EDITOR.MESSAGES.NO-CHANGES'));
      // return Promise.reject(`There's no changes to be saved`);
    } else {
      // this.generatingTemplate = true;
      //await this._ngb.sendRequest();
      if (this.formGroup.invalid) {
        return;
      }
      if (!this.editorView) {
        this.editorView = true;
      } else {
        this.saveAttachmentsAndEmail();

      }
      // this.generatingTemplate = false;
    }
  };
  loadEmailTemplate = () => {
    this.emailTemplate.templateName = this.formGroup.value.templateName;
    this.emailTemplate.category = this.formGroup.value.category;
    this.emailTemplate.to = this.formGroup.value.to;
    this.emailTemplate.subject = this.formGroup.value.subject;
    this.emailTemplate.contentHtml = '';
    this.emailTemplate.description = this.formGroup.value.description;
    this.emailTemplate.attachments = this.formGroup.value.attachments;
    this.emailTemplate.active = this.formGroup.value.active;
    this.emailTemplate.contentJson = JSON.stringify(this._ngb.Email);
  };
  onEmailTemplateSave = (template, attachments, inlineImages) => {
    let x = template;
    let y = JSON.parse(x);
    this.emailTemplate.templateName = this.formGroup.value.templateName;
    this.emailTemplate.category = this.formGroup.value.category;
    this.emailTemplate.to = this.formGroup.value.to;
    this.emailTemplate.subject = this.formGroup.value.subject;
    this.emailTemplate.contentHtml = '';
    this.emailTemplate.description = this.formGroup.value.description;
    this.emailTemplate.attachments = this.formGroup.value.attachments;
    this.emailTemplate.active = this.formGroup.value.active;
    this.emailTemplate.contentJson = template;
    this.emailTemplate.attachments = attachments;
    this.emailTemplate.inlineImages = inlineImages;


    if (!this.emailTemplate.id) {
      this.emailtemplateService.create(this.emailTemplate)
        .subscribe(
          data => {
            x = data;
            y = JSON.parse(x.contentJson);
            this.emailTemplate = {...this.emailTemplate, ...data};
            this.onBack();
          },
          error => {
          });
    } else {

      this.emailtemplateService.update(this.emailTemplate, this.emailTemplate.id)
        .subscribe(
          data => {
            x = data;
            y = JSON.parse(x.contentJson);
            this.onBack();
          },
          error => {
          });
    }
  };

  onBack(): void {
    this.router.navigate(['./emailtemplates'], {relativeTo: this.route.parent});
  }


  onKeyUp($event, varComboId: string) {
    const comboElement = document.getElementById(varComboId) as any;
    const len = $event.target.value.length;
    let pos = $event.target.selectionStart;
    if ('{{' === $event.target.value.substring(pos - 2, pos)) {
      if (!comboElement.classList.contains('show')) {
        comboElement.classList.add('show');
      }
    } else {
      if (comboElement.classList.contains('show')) {
        comboElement.classList.remove('show');
      }

    }
  }

  insertVariable(value: any, targetId: string, comboId: string) {
    const element = document.getElementById(targetId) as HTMLInputElement;
    let pos = element.selectionStart;
    let insString = '{{' + value.target.innerText + '}}';
    if (element.value[pos - 1] == '{' && element.value[pos - 2] == '{') {
      element.value = element.value.substring(0, pos - 2);
    }
    const updatedValue = this.insert(element.value, insString, pos);
    this.formGroup.value.subject = updatedValue; //this.formGroup.value.subject + "{{" +value + "}}";
    element.value = updatedValue;
    element.selectionStart = pos + insString.length;
    element.selectionEnd = pos + insString.length;
    this.showHideVariables(comboId);
    return false;
  }

  insert = (main_string, ins_string, pos) => {
    if (typeof (pos) == 'undefined') {
      pos = 0;
    }
    if (typeof (ins_string) == 'undefined') {
      ins_string = '';
    }
    return main_string.slice(0, pos) + ins_string + main_string.slice(pos);
  };

  showHideVariables(subject3Variable: string) {
    const comboElement = document.getElementById(subject3Variable) as any;
    
    if (comboElement.classList.contains('show')) {
      comboElement.classList.remove('show');
    } else {
      comboElement.classList.add('show');
    }
    return false;
  }

  onAttachmentAdd(files: Set<File>) {
    this.files = files;
  }

  saveAttachmentsAndEmail() {
    let attachments = [];
    let inlineImages = [];
    if (this.files.size > 0) {
      this.files.forEach(file => {

        if (file.name && !this.loadedFiles.has(file.name)) {
          const fileMetadata = {
            name: file.name, summary: file.name
          };
          this.emailFileService.createFileMetadata(fileMetadata).subscribe(res => {
            this.emailFileService.uploadFile(res.id, file);
            attachments.push({id: res.id});
          });
        }
      });
    }

    this._ngb.Email.structures.forEach(structure => {
      structure.elements.forEach(element => {
        element.forEach(block => {
          if (block && block['file'] && block['file']['name'] && block.type === 'image') {
            if (!this.loadedInlineImages.has(block.src)) {
              const fileMetadata = {
                name: block['file']['name'], summary: block['file']['name']
              };
              this.emailFileService.createFileMetadata(fileMetadata).subscribe(res => {
                this.emailFileService.uploadBlock(res.id, block);
                block.src = res.id;
                inlineImages.push({id: res.id});
                //additional code
                //attachments.push({id: res.id});
              });
            } else {
              block.src.replace(this.replaceapi, '');
            }
          }
        });
      });
    });

    setTimeout(() => this.onEmailTemplateSave(JSON.stringify(this._ngb.Email), attachments, inlineImages), 3000);
  }

  waitFor(conditionFunction) {

    const poll = resolve => {
      if (conditionFunction()) {
        resolve();
      } else {
        setTimeout(_ => poll(resolve), 400);
      }
    };

    return new Promise(poll);
  }

  openEmailTemplate()
  {
    this.editorView=false;
  }
}
