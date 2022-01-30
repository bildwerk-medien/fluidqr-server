import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UpdateModalComponent } from './update-modal.component';
import { IQrCode } from '../../entities/qr-code/qr-code.model';

@Injectable({ providedIn: 'root' })
export class UpdateModalService {
  private isOpen = false;

  constructor(private modalService: NgbModal) {}

  open(currentQrCode: IQrCode | undefined): Promise<any> | null {
    if (this.isOpen) {
      return null;
    }
    this.isOpen = true;
    const modalRef: NgbModalRef = this.modalService.open(UpdateModalComponent);
    modalRef.componentInstance.currentQrCode = currentQrCode;
    return modalRef.result;
  }

  close(): void {
    this.isOpen = false;
  }
}
