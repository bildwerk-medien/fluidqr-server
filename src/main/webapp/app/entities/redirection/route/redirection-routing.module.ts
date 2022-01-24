import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RedirectionComponent } from '../list/redirection.component';
import { RedirectionDetailComponent } from '../detail/redirection-detail.component';
import { RedirectionUpdateComponent } from '../update/redirection-update.component';
import { RedirectionRoutingResolveService } from './redirection-routing-resolve.service';

const redirectionRoute: Routes = [
  {
    path: '',
    component: RedirectionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RedirectionDetailComponent,
    resolve: {
      redirection: RedirectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RedirectionUpdateComponent,
    resolve: {
      redirection: RedirectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RedirectionUpdateComponent,
    resolve: {
      redirection: RedirectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(redirectionRoute)],
  exports: [RouterModule],
})
export class RedirectionRoutingModule {}
