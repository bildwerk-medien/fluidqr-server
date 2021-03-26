import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { QrCodeService } from 'app/entities/qr-code/qr-code.service';
import { RedirectionService } from 'app/entities/redirection/redirection.service';
import { Account } from 'app/core/user/account.model';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-login-modal',
  templateUrl: './add-modal.component.html',
})
export class AddModalComponent implements AfterViewInit, OnInit {
  @ViewChild('code', { static: false })
  code?: ElementRef;

  account: Account | null = null;
  creationError = false;

  loginForm = this.fb.group({
    code: [''],
  });

  constructor(
    private accountService: AccountService,
    private qrCodeService: QrCodeService,
    private redirectionService: RedirectionService,
    private router: Router,
    public activeModal: NgbActiveModal,
    private fb: FormBuilder
  ) {}

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
        code: this.account?.login + '-' + this.loginForm.get('code')!.value,
      })
      .subscribe(
        res => {
          if (res.body) {
            this.redirectionService
              .create({
                url: 'https://bildwerk-medien.de',
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

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }
}
