import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { QrCodeService } from 'app/entities/qr-code/qr-code.service';

@Component({
  selector: 'jhi-login-modal',
  templateUrl: './add-modal.component.html',
})
export class AddModalComponent implements AfterViewInit {
  @ViewChild('code', { static: false })
  code?: ElementRef;

  creationError = false;

  loginForm = this.fb.group({
    code: [''],
  });

  constructor(private qrCodeService: QrCodeService, private router: Router, public activeModal: NgbActiveModal, private fb: FormBuilder) {}

  ngAfterViewInit(): void {
    if (this.code) {
      this.code.nativeElement.focus();
    }
  }

  cancel(): void {
    this.creationError = false;
    this.loginForm.patchValue({
      username: '',
      password: '',
    });
    this.activeModal.dismiss('cancel');
  }

  add(): void {
    this.qrCodeService
      .create({
        code: this.loginForm.get('code')!.value,
      })
      .subscribe(
        () => {
          this.creationError = false;
          this.activeModal.close();
        },
        () => (this.creationError = true)
      );
  }
}
