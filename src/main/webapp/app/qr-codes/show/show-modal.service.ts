import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ShowModalComponent } from './show-modal.component';

@Injectable({ providedIn: 'root' })
export class ShowModalService {
  private isOpen = false;

  constructor(private modalService: NgbModal) {}

  open(qrCodeCanvasId: string, qrCode: any): void {
    const modalRef: NgbModalRef = this.modalService.open(ShowModalComponent);
    modalRef.componentInstance.qrCodeCanvasId = qrCodeCanvasId;
    modalRef.componentInstance.qrCode = qrCode;
  }

  close(): void {
    this.isOpen = false;
  }
}
