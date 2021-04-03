import { Component, OnInit } from '@angular/core';
import { QrCodeService } from 'app/entities/qr-code/qr-code.service';
import { QrCode } from 'app/shared/model/qr-code.model';
import { AddModalService } from 'app/qr-codes/add/add-modal.service';
import { RedirectionService } from 'app/entities/redirection/redirection.service';

@Component({
  selector: 'jhi-qr-codes',
  templateUrl: './qr-codes.component.html',
  styleUrls: ['qr-codes.component.scss'],
})
export class QrCodesComponent implements OnInit {
  qrCodes?: QrCode[];

  constructor(
    private qrCodeService: QrCodeService,
    private redirectionService: RedirectionService,
    private addModalService: AddModalService
  ) {}

  ngOnInit(): void {
    this.readQrCodes();
  }

  openAdd(): void {
    const promise = this.addModalService.open();
    promise?.finally(() => {
      this.addModalService.close();
      this.readQrCodes();
    });
  }

  readQrCodes(): void {
    this.qrCodeService.query().subscribe(res => {
      if (res.body) {
        this.qrCodes = res.body;
      }
    });
  }

  deleteQrCode(): void {
    this.readQrCodes();
  }
}
