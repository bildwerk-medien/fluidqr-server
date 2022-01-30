import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { QrCodeComponent } from './list/qr-code.component';
import { QrCodeDetailComponent } from './detail/qr-code-detail.component';
import { QrCodeUpdateComponent } from './update/qr-code-update.component';
import { QrCodeDeleteDialogComponent } from './delete/qr-code-delete-dialog.component';
import { QrCodeRoutingModule } from './route/qr-code-routing.module';

@NgModule({
  imports: [SharedModule, QrCodeRoutingModule],
  declarations: [QrCodeComponent, QrCodeDetailComponent, QrCodeUpdateComponent, QrCodeDeleteDialogComponent],
  entryComponents: [QrCodeDeleteDialogComponent],
})
export class QrCodeModule {}
