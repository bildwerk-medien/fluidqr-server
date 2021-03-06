import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AddModalComponent } from './add-modal.component';

@Injectable({ providedIn: 'root' })
export class AddModalService {
  private isOpen = false;

  constructor(private modalService: NgbModal) {}

  open(): Promise<any> | null {
    if (this.isOpen) {
      return null;
    }
    this.isOpen = true;
    const modalRef: NgbModalRef = this.modalService.open(AddModalComponent);
    return modalRef.result;
  }

  close(): void {
    this.isOpen = false;
  }
}
