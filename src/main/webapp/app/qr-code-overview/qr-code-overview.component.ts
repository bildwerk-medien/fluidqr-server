import { Component } from '@angular/core';
import SharedModule from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { QrCodeCardComponent } from './qr-code-card/qr-code-card.component';
import { IQrCode } from '../entities/qr-code/qr-code.model';
import { QrCodeService } from '../entities/qr-code/service/qr-code.service';

@Component({
  standalone: true,
  selector: 'jhi-qr-code-overview',
  templateUrl: './qr-code-overview.component.html',
  styleUrls: ['./qr-code-overview.component.scss'],
  imports: [SharedModule, RouterModule, QrCodeCardComponent],
})
export class QrCodeOverviewComponent {
  qrCodes?: IQrCode[];

  constructor(private qrCodeService: QrCodeService) {}

  ngOnInit(): void {
    this.readQrCodes();
  }

  private readQrCodes() {
    this.qrCodeService.query().subscribe(res => {
      if (res.body) {
        this.qrCodes = res.body;
      }
    });
  }
}
