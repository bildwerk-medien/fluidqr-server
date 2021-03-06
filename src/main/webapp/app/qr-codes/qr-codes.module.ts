import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FluidQrServerSharedModule } from '../shared/shared.module';

import { QR_CODES_ROUTE, QrCodesComponent } from './';
import { QrCodeDisplayComponent } from 'app/qr-code-display/qr-code-display.component';
import { AddModalComponent } from 'app/qr-codes/add/add-modal.component';

@NgModule({
  imports: [FluidQrServerSharedModule, RouterModule.forRoot([QR_CODES_ROUTE], { useHash: true })],
  declarations: [QrCodesComponent, QrCodeDisplayComponent, AddModalComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class FluidQrServerAppQrCodesModule {}
