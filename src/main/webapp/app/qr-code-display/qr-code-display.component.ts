import { AfterViewInit, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import QRCodeStyling from 'qr-code-styling';
import { IQrCode, QrCode } from 'app/shared/model/qr-code.model';
import { NgForm } from '@angular/forms';
import { UpdateModalService } from 'app/qr-codes/update/update-modal.service';
import { QrCodeService } from 'app/entities/qr-code/qr-code.service';
import { DeleteModalService } from 'app/qr-codes/delete/delete-modal.service';

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
  currentQrCode?: IQrCode;

  @Output()
  deleteQrCode = new EventEmitter<void>();

  constructor(
    private updateModalService: UpdateModalService,
    private deleteModalService: DeleteModalService,
    private qrCodeService: QrCodeService
  ) {}

  getQrCode(qrCode: QrCode | undefined): QRCodeStyling {
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

  updateRedirection(): void {
    const promise = this.updateModalService.open(this.currentQrCode);
    promise?.finally(() => {
      this.updateModalService.close();
      this.refreshQrCode();
    });
  }

  refreshQrCode(): void {
    if (this.currentQrCode?.id) {
      this.qrCodeService.find(this.currentQrCode?.id).subscribe(res => {
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
    //this.deleteQrCode.emit(this.currentQrCode?.id);
    const promise = this.deleteModalService.open(this.currentQrCode);
    promise?.finally(() => {
      this.deleteModalService.close();
      this.deleteQrCode.emit();
    });
  }
}
