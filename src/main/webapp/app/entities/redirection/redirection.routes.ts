import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RedirectionComponent } from './list/redirection.component';
import { RedirectionDetailComponent } from './detail/redirection-detail.component';
import { RedirectionUpdateComponent } from './update/redirection-update.component';
import RedirectionResolve from './route/redirection-routing-resolve.service';

const redirectionRoute: Routes = [
  {
    path: '',
    component: RedirectionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RedirectionDetailComponent,
    resolve: {
      redirection: RedirectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RedirectionUpdateComponent,
    resolve: {
      redirection: RedirectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RedirectionUpdateComponent,
    resolve: {
      redirection: RedirectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default redirectionRoute;
