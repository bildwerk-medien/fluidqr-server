import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';
import { FormBuilder, NgForm } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { QrCodeService } from 'app/entities/qr-code/qr-code.service';

@Component({
  selector: 'jhi-login-modal',
  templateUrl: './update-modal.component.html',
})
export class UpdateModalComponent implements AfterViewInit {
  urlPattern = '(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?';

  @ViewChild('code', { static: false })
  code?: ElementRef;

  @Input()
  currentRedirect?: string;

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

  update(f: NgForm): void {
    this.creationError = false;
  }
}
