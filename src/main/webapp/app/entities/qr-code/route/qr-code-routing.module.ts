import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QrCodeComponent } from '../list/qr-code.component';
import { QrCodeDetailComponent } from '../detail/qr-code-detail.component';
import { QrCodeUpdateComponent } from '../update/qr-code-update.component';
import { QrCodeRoutingResolveService } from './qr-code-routing-resolve.service';

const qrCodeRoute: Routes = [
  {
    path: '',
    component: QrCodeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QrCodeDetailComponent,
    resolve: {
      qrCode: QrCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QrCodeUpdateComponent,
    resolve: {
      qrCode: QrCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QrCodeUpdateComponent,
    resolve: {
      qrCode: QrCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(qrCodeRoute)],
  exports: [RouterModule],
})
export class QrCodeRoutingModule {}
