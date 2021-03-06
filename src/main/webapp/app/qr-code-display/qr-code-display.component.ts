import { AfterViewInit, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import QRCodeStyling from 'qr-code-styling';
import { QrCode } from 'app/shared/model/qr-code.model';
import { NgForm } from '@angular/forms';
import { UpdateModalService } from 'app/qr-codes/update/update-modal.service';

@Component({
  selector: 'jhi-qr-code-display',
  templateUrl: './qr-code-display.component.html',
  styleUrls: ['./qr-code-display.component.scss'],
})
export class QrCodeDisplayComponent implements OnInit, AfterViewInit {
  qrCodeImage: string | undefined = '';
  qrCode: QRCodeStyling | null = null;
  urlPattern = '(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?';
  redirectUrl = '';
  currentRedirection = this.redirectUrl;
  updateRedirect: any;

  @Input()
  currentQrCode?: QrCode;

  @Output()
  deleteQrCode = new EventEmitter<number>();

  constructor(private updateModalService: UpdateModalService) {}

  getQrCode(qrCode: QrCode | undefined): QRCodeStyling {
    return new QRCodeStyling({
      width: 200,
      height: 200,
      data: qrCode?.currentRedirect,
      dotsOptions: {
        color: '#000000',
        type: 'rounded',
      },
      cornersSquareOptions: {
        type: 'extra-rounded',
      },
      backgroundOptions: {
        color: '#FFFFFF',
      },
      imageOptions: {
        crossOrigin: 'anonymous',
        margin: 20,
      },
    });
  }

  updateRedirection(f: NgForm): void {
    // if (f.valid) {
    //   if (this.updateRedirect && this.currentQrCode) {
    //     this.qrCodeImage = '';
    //   }
    // }
    const promise = this.updateModalService.open(this.currentQrCode?.currentRedirect);
    promise?.finally(() => {
      this.updateModalService.close();
      // this.readQrCodes();
    });
  }

  onDownload(): void {
    const downloadQrCode = this.getQrCode(this.currentQrCode);
    downloadQrCode.update({
      width: 1000,
      height: 1000,
    });
    downloadQrCode.download({
      name: this.currentQrCode?.code,
    });
  }

  ngOnInit(): void {}

  getHtmlElement(): HTMLElement | undefined {
    const htmlElement = document.getElementById('canvas-' + this.currentQrCode?.id);
    if (htmlElement != null) {
      return htmlElement;
    }
    return undefined;
  }

  ngAfterViewInit(): void {
    this.qrCode = this.getQrCode(this.currentQrCode);
    this.qrCode?.append(this.getHtmlElement());
  }

  onDelete(): void {
    this.deleteQrCode.emit(this.currentQrCode?.id);
  }
}
