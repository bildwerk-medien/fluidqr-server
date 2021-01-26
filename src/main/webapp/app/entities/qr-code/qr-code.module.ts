import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FluidQrServerSharedModule } from 'app/shared/shared.module';
import { QrCodeComponent } from './qr-code.component';
import { QrCodeDetailComponent } from './qr-code-detail.component';
import { QrCodeUpdateComponent } from './qr-code-update.component';
import { QrCodeDeleteDialogComponent } from './qr-code-delete-dialog.component';
import { qrCodeRoute } from './qr-code.route';

@NgModule({
  imports: [FluidQrServerSharedModule, RouterModule.forChild(qrCodeRoute)],
  declarations: [QrCodeComponent, QrCodeDetailComponent, QrCodeUpdateComponent, QrCodeDeleteDialogComponent],
  entryComponents: [QrCodeDeleteDialogComponent],
})
export class FluidQrServerQrCodeModule {}
