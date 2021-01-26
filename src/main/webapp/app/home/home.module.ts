import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FluidQrServerSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [FluidQrServerSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent],
})
export class FluidQrServerHomeModule {}
