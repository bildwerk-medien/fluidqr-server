import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQrCode } from '../qr-code.model';
import { QrCodeService } from '../service/qr-code.service';

@Component({
  templateUrl: './qr-code-delete-dialog.component.html',
})
export class QrCodeDeleteDialogComponent {
  qrCode?: IQrCode;

  constructor(protected qrCodeService: QrCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.qrCodeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
