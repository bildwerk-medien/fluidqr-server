import { Component, Input, OnInit } from '@angular/core';
import QRCodeStyling from 'qr-code-styling';
import { QrCode } from 'app/shared/model/qr-code.model';
import { QrCodeService } from 'app/entities/qr-code/qr-code.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'jhi-qr-code-display',
  templateUrl: './qr-code-display.component.html',
  styleUrls: ['./qr-code-display.component.scss'],
})
export class QrCodeDisplayComponent implements OnInit {
  qrCodeImage = '';
  qrCode: QRCodeStyling | null = null;
  urlPattern = '(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?';
  redirectUrl = '';
  currentRedirection = this.redirectUrl;
  updateRedirect: any;

  @Input()
  currentQrCode: QrCode | undefined;

  constructor(private qrCodeService: QrCodeService) {}

  getQrCode(): QRCodeStyling {
    return new QRCodeStyling({
      width: 400,
      height: 400,
      data: 'https://www.bildwerk-medien.de/',
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
    if (f.valid) {
      if (this.updateRedirect && this.currentQrCode) {
        this.qrCodeImage = '';
      }
    }
  }

  onDownload(): void {
    this.qrCodeImage = document.getElementsByTagName('img')[0].src;
  }

  ngOnInit(): void {
    this.qrCode = this.getQrCode();
    this.qrCode.append(this.getHtmlElement());
  }

  getHtmlElement(): HTMLElement | undefined {
    const htmlElement = document.getElementById('canvas');
    if (htmlElement != null) {
      return htmlElement;
    }
    return undefined;
  }
}
