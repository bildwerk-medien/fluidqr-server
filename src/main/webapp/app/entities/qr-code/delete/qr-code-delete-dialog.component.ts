import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IQrCode } from '../qr-code.model';
import { QrCodeService } from '../service/qr-code.service';

@Component({
  standalone: true,
  templateUrl: './qr-code-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QrCodeDeleteDialogComponent {
  qrCode?: IQrCode;

  constructor(
    protected qrCodeService: QrCodeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.qrCodeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
