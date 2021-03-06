import { Component, OnInit } from '@angular/core';
import { QrCodeService } from 'app/entities/qr-code/qr-code.service';
import { QrCode } from 'app/shared/model/qr-code.model';

@Component({
  selector: 'jhi-qr-codes',
  templateUrl: './qr-codes.component.html',
  styleUrls: ['qr-codes.component.scss'],
})
export class QrCodesComponent implements OnInit {
  qrCodes?: QrCode[];

  constructor(private qrCodeService: QrCodeService) {}

  ngOnInit(): void {
    this.qrCodeService.query().subscribe(res => {
      if (res.body) {
        this.qrCodes = res.body;
      }
    });
  }
}
