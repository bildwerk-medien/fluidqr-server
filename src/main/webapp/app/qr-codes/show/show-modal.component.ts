import { AfterViewInit, Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import QRCodeStyling from 'qr-code-styling';

@Component({
  selector: 'jhi-delete-modal',
  templateUrl: './show-modal.component.html',
})
export class ShowModalComponent implements AfterViewInit {
  qrCodeCanvasId = '';
  qrCode: QRCodeStyling | undefined;

  constructor(public activeModal: NgbActiveModal) {}

  ngAfterViewInit(): void {
    if (this.qrCode) {
      this.qrCode.append(this.getHtmlElement());
    }
  }

  getHtmlElement(): HTMLElement | undefined {
    const htmlElement = document.getElementById(this.qrCodeCanvasId);
    if (htmlElement != null) {
      return htmlElement;
    }
    return undefined;
  }

  cancel(): void {
    this.activeModal.dismiss('cancel');
  }
}
