import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'redirection',
        data: { pageTitle: 'fluidQrServerApp.redirection.home.title' },
        loadChildren: () => import('./redirection/redirection.routes'),
      },
      {
        path: 'qr-code',
        data: { pageTitle: 'fluidQrServerApp.qrCode.home.title' },
        loadChildren: () => import('./qr-code/qr-code.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
