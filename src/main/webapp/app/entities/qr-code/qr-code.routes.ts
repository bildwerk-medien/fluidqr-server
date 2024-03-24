import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { QrCodeComponent } from './list/qr-code.component';
import { QrCodeDetailComponent } from './detail/qr-code-detail.component';
import { QrCodeUpdateComponent } from './update/qr-code-update.component';
import QrCodeResolve from './route/qr-code-routing-resolve.service';

const qrCodeRoute: Routes = [
  {
    path: '',
    component: QrCodeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QrCodeDetailComponent,
    resolve: {
      qrCode: QrCodeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QrCodeUpdateComponent,
    resolve: {
      qrCode: QrCodeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QrCodeUpdateComponent,
    resolve: {
      qrCode: QrCodeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default qrCodeRoute;
