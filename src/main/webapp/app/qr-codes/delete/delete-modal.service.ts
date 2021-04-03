import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IQrCode } from 'app/shared/model/qr-code.model';
import { DeleteModalComponent } from 'app/qr-codes/delete/delete-modal.component';

@Injectable({ providedIn: 'root' })
export class DeleteModalService {
  private isOpen = false;

  constructor(private modalService: NgbModal) {}

  open(currentQrCode: IQrCode | undefined): Promise<any> | null {
    if (this.isOpen) {
      return null;
    }
    this.isOpen = true;
    const modalRef: NgbModalRef = this.modalService.open(DeleteModalComponent);
    modalRef.componentInstance.currentQrCode = currentQrCode;
    return modalRef.result;
  }

  close(): void {
    this.isOpen = false;
  }
}
