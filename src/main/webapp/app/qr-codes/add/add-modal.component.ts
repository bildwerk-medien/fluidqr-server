import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, NgForm } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { URL_PATTERN } from 'app/app.constants';
import { Account } from '../../core/auth/account.model';
import { QrCodeService } from '../../entities/qr-code/service/qr-code.service';
import { RedirectionService } from '../../entities/redirection/service/redirection.service';
import { UrlUtil } from '../../shared/util/url-util';

@Component({
  selector: 'jhi-login-modal',
  templateUrl: './add-modal.component.html',
})
export class AddModalComponent implements OnInit, AfterViewInit {
  urlPattern = URL_PATTERN;

  @ViewChild('code-input', { static: false })
  codeInput?: ElementRef;

  account: Account | null = null;
  creationError = false;

  currentRedirection: string | undefined;
  code: string | undefined;

  constructor(
    private accountService: AccountService,
    private qrCodeService: QrCodeService,
    private redirectionService: RedirectionService,
    private router: Router,
    public activeModal: NgbActiveModal,
    private fb: FormBuilder,
    private urlUtil: UrlUtil
  ) {}

  ngAfterViewInit(): void {
    if (this.codeInput) {
      this.codeInput.nativeElement.focus();
    }
  }

  cancel(): void {
    this.creationError = false;
    this.activeModal.dismiss('cancel');
  }

  submit(f: NgForm): void {
    // eslint-disable-next-line no-console
    console.log(f.valid)

    // eslint-disable-next-line no-console
    console.log(this.code)

    // eslint-disable-next-line no-console
    console.log(this.account)

    if (f.valid && this.code && this.account) {
      this.qrCodeService
        .create({
          code: `${this.account.login}-${this.code}`,
        })
        .subscribe(
          res => {
            if (res.body) {
              this.redirectionService
                .create({
                  url: this.urlUtil.enhanceToHttps(this.currentRedirection),
                  enabled: true,
                  qrCode: {
                    id: res.body.id,
                  },
                })
                .subscribe(() => {
                  this.creationError = false;
                  this.activeModal.close();
                });
            }
          },
          () => (this.creationError = true)
        );
    }
  }

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }
}
