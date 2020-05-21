import {Component, Inject, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {EmailFileService} from '../email-file.service';

@Component({
  selector: 'lib-email-attachment',
  templateUrl: './email-attachment.component.html',
  styleUrls: ['./email-attachment.component.css']
})
export class EmailAttachmentComponent {
  myForm = new FormGroup({
    file: new FormControl('', [Validators.required]),
    fileSource: new FormControl('', [Validators.required])
  });
  name: string = '';
  file: any;

  constructor(private http: HttpClient,
              private emailFileService: EmailFileService) {
  }

  get f() {
    return this.myForm.controls;
  }

  onFileChange(event) {

    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.myForm.patchValue({
        fileSource: file
      });
    }
  }

  submit() {
    const fileMetadata = {
      name: this.name, summary: this.name
    };
    this.emailFileService.createFileMetadata(fileMetadata).subscribe(res => {
      console.log(res);
      this.emailFileService.uploadFile(res.id, this.myForm);
    });

  }

}
