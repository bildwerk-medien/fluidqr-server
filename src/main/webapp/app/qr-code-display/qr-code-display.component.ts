import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import QRCodeStyling from 'qr-code-styling';
import { QrCode } from 'app/shared/model/qr-code.model';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'jhi-qr-code-display',
  templateUrl: './qr-code-display.component.html',
  styleUrls: ['./qr-code-display.component.scss'],
})
export class QrCodeDisplayComponent implements OnInit, AfterViewInit {
  qrCodeImage = '';
  qrCode: QRCodeStyling | null = null;
  urlPattern = '(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?';
  redirectUrl = '';
  currentRedirection = this.redirectUrl;
  updateRedirect: any;

  @Input()
  currentQrCode?: QrCode;

  constructor() {}

  getQrCode(): QRCodeStyling {
    return new QRCodeStyling({
      width: 200,
      height: 200,
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
        margin: 10,
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
  }

  getHtmlElement(): HTMLElement | undefined {
    const htmlElement = document.getElementById('canvas-' + this.currentQrCode?.id);
    if (htmlElement != null) {
      return htmlElement;
    }
    return undefined;
  }

  ngAfterViewInit(): void {
    this.qrCode?.append(this.getHtmlElement());
  }
}
