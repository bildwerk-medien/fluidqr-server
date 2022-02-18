import { Route } from '@angular/router';

import { GoogleLoginComponent } from './google-login.component';
import { UserRouteAccessService } from '../core/auth/user-route-access.service';

export const GOOGLE_LOGIN_ROUTE: Route = {
  path: 'google-login',
  component: GoogleLoginComponent,
  data: {
    authorities: [],
    pageTitle: 'qr-codes.title',
  },
  canActivate: [UserRouteAccessService],
};
