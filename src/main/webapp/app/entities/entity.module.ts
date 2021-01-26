import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'redirection',
        loadChildren: () => import('./redirection/redirection.module').then(m => m.FluidQrServerRedirectionModule),
      },
      {
        path: 'qr-code',
        loadChildren: () => import('./qr-code/qr-code.module').then(m => m.FluidQrServerQrCodeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class FluidQrServerEntityModule {}
