import { Component } from '@angular/core';
import SharedModule from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true,
  selector: 'jhi-qr-code-overview',
  templateUrl: './qr-code-overview.component.html',
  styleUrls: ['./qr-code-overview.component.scss'],
  imports: [SharedModule, RouterModule],
})
export class QrCodeOverviewComponent {}
