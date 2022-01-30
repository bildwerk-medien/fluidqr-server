import { Route } from '@angular/router';

import { QrCodesComponent } from './qr-codes.component';
import { UserRouteAccessService } from '../core/auth/user-route-access.service';

export const QR_CODES_ROUTE: Route = {
  path: 'qr-codes',
  component: QrCodesComponent,
  data: {
    authorities: [],
    pageTitle: 'qr-codes.title',
  },
  canActivate: [UserRouteAccessService],
};
