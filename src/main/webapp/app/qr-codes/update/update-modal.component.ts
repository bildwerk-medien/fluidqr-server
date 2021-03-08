import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';
import { FormBuilder, NgForm } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { QrCodeService } from 'app/entities/qr-code/qr-code.service';
import { IQrCode } from 'app/shared/model/qr-code.model';
import { RedirectionService } from 'app/entities/redirection/redirection.service';

@Component({
  selector: 'jhi-login-modal',
  templateUrl: './update-modal.component.html',
})
export class UpdateModalComponent implements AfterViewInit {
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

  constructor(
    private redirectionService: RedirectionService,
    private router: Router,
    public activeModal: NgbActiveModal,
    private fb: FormBuilder
  ) {
    if (this.currentQrCode?.redirections && this.currentQrCode.redirections?.length > 0) {
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

  update(f: NgForm): void {
    if (f.valid) {
      if (this.currentQrCode?.redirections && this.currentQrCode.redirections.length > 0) {
        this.currentQrCode.redirections[0].url = this.currentRedirection;
        this.redirectionService.update(this.currentQrCode.redirections[0]).subscribe(() => {
          this.creationError = false;
          this.activeModal.close();
        });
      }
      return;
    }
    this.creationError = true;
  }
}
