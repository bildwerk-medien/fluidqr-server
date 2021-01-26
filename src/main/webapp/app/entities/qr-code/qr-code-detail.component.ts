import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQrCode } from 'app/shared/model/qr-code.model';

@Component({
  selector: 'jhi-qr-code-detail',
  templateUrl: './qr-code-detail.component.html',
})
export class QrCodeDetailComponent implements OnInit {
  qrCode: IQrCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ qrCode }) => (this.qrCode = qrCode));
  }

  previousState(): void {
    window.history.back();
  }
}
