import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QR_CODES_ROUTE, QrCodesComponent } from './';
import { QrCodeDisplayComponent } from 'app/qr-code-display/qr-code-display.component';
import { AddModalComponent } from 'app/qr-codes/add/add-modal.component';
import { UpdateModalComponent } from 'app/qr-codes/update/update-modal.component';
import { DeleteModalComponent } from 'app/qr-codes/delete/delete-modal.component';
import { SharedModule } from '../shared/shared.module';
import { UrlUtil } from '../shared/util/url-util';
import { ShowModalComponent } from './show/show-modal.component';

@NgModule({
  imports: [SharedModule, RouterModule.forRoot([QR_CODES_ROUTE], { useHash: true })],
  declarations: [
    QrCodesComponent,
    QrCodeDisplayComponent,
    AddModalComponent,
    UpdateModalComponent,
    DeleteModalComponent,
    ShowModalComponent,
  ],
  entryComponents: [],
  providers: [UrlUtil],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class QrCodesModule {}
