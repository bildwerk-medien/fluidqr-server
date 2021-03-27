import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';
import { FormBuilder, NgForm } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { IQrCode } from 'app/shared/model/qr-code.model';
import { RedirectionService } from 'app/entities/redirection/redirection.service';
import { CommonModal } from 'app/core/common-modal.interface';
import { UrlUtil } from 'app/shared/util/url-util';
import { URL_PATTERN } from 'app/app.constants';

@Component({
  selector: 'jhi-login-modal',
  templateUrl: './update-modal.component.html',
})
export class UpdateModalComponent implements AfterViewInit, CommonModal {
  urlPattern = URL_PATTERN;

  @ViewChild('redirection-input', { static: false })
  redirectionInput?: ElementRef;

  @Input()
  currentQrCode: IQrCode | undefined;

  currentRedirection: string | undefined;

  creationError = false;

  constructor(private redirectionService: RedirectionService, public activeModal: NgbActiveModal, public urlUtil: UrlUtil) {
    this.currentRedirection = '';
  }

  ngAfterViewInit(): void {
    if (this.redirectionInput) {
      this.redirectionInput.nativeElement.focus();
    }
  }

  cancel(): void {
    this.creationError = false;
    this.activeModal.dismiss('cancel');
  }

  submit(f: NgForm): void {
    if (f.valid) {
      if (this.currentQrCode?.redirections && this.currentQrCode.redirections.length > 0) {
        this.currentRedirection = this.urlUtil.enhanceToHttps(this.currentRedirection);
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
