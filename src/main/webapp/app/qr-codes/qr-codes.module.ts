import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FluidQrServerSharedModule } from '../shared/shared.module';

import { QR_CODES_ROUTE, QrCodesComponent } from './';

@NgModule({
  imports: [FluidQrServerSharedModule, RouterModule.forRoot([QR_CODES_ROUTE], { useHash: true })],
  declarations: [QrCodesComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class FluidQrServerAppQrCodesModule {}
