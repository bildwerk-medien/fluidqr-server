import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { IQrCode } from '../../entities/qr-code/qr-code.model';
import { QrCodeService } from '../../entities/qr-code/service/qr-code.service';

@Component({
  selector: 'jhi-delete-modal',
  templateUrl: './delete-modal.component.html',
})
export class DeleteModalComponent implements AfterViewInit {
  urlPattern = '(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?';

  @ViewChild('code', { static: false })
  code?: ElementRef;

  @Input()
  currentQrCode: IQrCode | undefined;

  currentRedirection: string | undefined;

  creationError = false;

  loginForm = this.fb.group({
    code: [''],
  });

  constructor(private qrCodeService: QrCodeService, private router: Router, public activeModal: NgbActiveModal, private fb: FormBuilder) {
    if (this.currentQrCode?.redirections && this.currentQrCode.redirections.length > 0) {
      this.currentRedirection = this.currentQrCode.redirections[0].url;
    } else {
      this.currentRedirection = '';
    }
  }

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

  delete(): void {
    if (this.currentQrCode?.id) {
      this.qrCodeService.delete(this.currentQrCode.id).subscribe((res: { ok: any }) => {
        if (!res.ok) {
          this.creationError = false;
        } else {
          this.creationError = false;
          this.activeModal.close();
        }
      });
    } else {
      this.creationError = true;
    }
  }
}
