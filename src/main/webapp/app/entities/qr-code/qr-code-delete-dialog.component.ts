import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IQrCode } from 'app/shared/model/qr-code.model';
import { QrCodeService } from './qr-code.service';

@Component({
  templateUrl: './qr-code-delete-dialog.component.html',
})
export class QrCodeDeleteDialogComponent {
  qrCode?: IQrCode;

  constructor(protected qrCodeService: QrCodeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.qrCodeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('qrCodeListModification');
      this.activeModal.close();
    });
  }
}
