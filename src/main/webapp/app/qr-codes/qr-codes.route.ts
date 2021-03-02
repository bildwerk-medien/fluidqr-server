import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { QrCodesComponent } from './qr-codes.component';

export const QR_CODES_ROUTE: Route = {
  path: 'qr-codes',
  component: QrCodesComponent,
  data: {
    authorities: [],
    pageTitle: 'qr-codes.title',
  },
  canActivate: [UserRouteAccessService],
};
