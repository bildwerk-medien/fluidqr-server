import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IQrCode } from '../../entities/qr-code/qr-code.model';
import SharedModule from '../../shared/shared.module';
import QRCodeStyling from 'qr-code-styling';
import { QrCodeService } from '../../entities/qr-code/service/qr-code.service';

@Component({
  standalone: true,
  selector: 'jhi-qr-code-card',
  templateUrl: './qr-code-card.component.html',
  styleUrls: ['./qr-code-card.component.scss'],
  imports: [SharedModule],
})
export class QrCodeCardComponent {
  qrCodeImage: string | undefined = '';
  qrCode: QRCodeStyling | null = null;
  urlPattern = '(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?';
  redirectUrl = '';
  currentRedirection = this.redirectUrl;
  updateRedirect: any;

  @Input()
  currentQrCode?: IQrCode;

  @Output()
  deleteQrCode = new EventEmitter<void>();

  constructor(private qrCodeService: QrCodeService) {}

  getQrCode(qrCode: IQrCode | undefined): QRCodeStyling {
    return new QRCodeStyling({
      width: 200,
      height: 200,
      data: qrCode?.link,
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

  refreshQrCode(): void {
    if (this.currentQrCode?.id) {
      this.qrCodeService.find(this.currentQrCode.id).subscribe(res => {
        if (res.body) {
          this.currentQrCode = res.body;
        }
      });
    }
  }

  onDownload(): void {
    const downloadQrCode = this.getQrCode(this.currentQrCode);
    downloadQrCode.update({
      width: 1000,
      height: 1000,
    });

    downloadQrCode.download({
      name: this.mapCurrentQrCode(),
    });
  }

  mapCurrentQrCode(): string | undefined {
    if (this.currentQrCode?.code === null) {
      return undefined;
    }
    return this.currentQrCode?.code;
  }

  getHtmlElement(): HTMLElement | undefined {
    const htmlElement = document.getElementById(this.createCanvasQrCodeId());
    if (htmlElement != null) {
      return htmlElement;
    }
    return undefined;
  }

  ngAfterViewInit(): void {
    this.qrCode = this.getQrCode(this.currentQrCode);
    this.qrCode.append(this.getHtmlElement());
  }

  private createCanvasQrCodeId(): string {
    let qrCodeId = '';

    if (this.currentQrCode?.id) {
      qrCodeId = this.currentQrCode.id.toString();
    }

    return `canvas-${qrCodeId}`;
  }
}
