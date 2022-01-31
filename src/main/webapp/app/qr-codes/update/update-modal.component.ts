import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { URL_PATTERN } from 'app/app.constants';
import { RedirectionService } from '../../entities/redirection/service/redirection.service';
import { IQrCode } from '../../entities/qr-code/qr-code.model';
import { UrlUtil } from '../../shared/util/url-util';

@Component({
  selector: 'jhi-login-modal',
  templateUrl: './update-modal.component.html',
})
export class UpdateModalComponent implements AfterViewInit {
  urlPattern = URL_PATTERN;

  @ViewChild('redirection-input', { static: false })
  redirectionInput?: ElementRef;

  @Input()
  currentQrCode: IQrCode | undefined;

  currentRedirection: string | undefined;

  creationError = false;

  constructor(private redirectionService: RedirectionService, public activeModal: NgbActiveModal, private urlUtil: UrlUtil) {
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
        this.currentQrCode.redirections[0].url = this.urlUtil.enhanceToHttps(this.currentRedirection);
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
