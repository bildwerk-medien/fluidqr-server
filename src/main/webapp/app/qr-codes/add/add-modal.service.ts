import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AddModalComponent } from './add-modal.component';

@Injectable({ providedIn: 'root' })
export class AddModalService {
  private isOpen = false;

  constructor(private modalService: NgbModal) {}

  open(): void {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;
    const modalRef: NgbModalRef = this.modalService.open(AddModalComponent);
    modalRef.result.finally(() => (this.isOpen = false));
  }
}
