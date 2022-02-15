import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'redirection',
        data: { pageTitle: 'fluidQrServerApp.redirection.home.title' },
        loadChildren: () => import('./redirection/redirection.module').then(m => m.RedirectionModule),
      },
      {
        path: 'qr-code',
        data: { pageTitle: 'fluidQrServerApp.qrCode.home.title' },
        loadChildren: () => import('./qr-code/qr-code.module').then(m => m.QrCodeModule),
      },
      {
        path: 'google-user',
        data: { pageTitle: 'fluidQrServerApp.googleUser.home.title' },
        loadChildren: () => import('./google-user/google-user.module').then(m => m.GoogleUserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
