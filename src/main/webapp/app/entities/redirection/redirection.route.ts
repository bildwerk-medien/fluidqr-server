import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRedirection, Redirection } from 'app/shared/model/redirection.model';
import { RedirectionService } from './redirection.service';
import { RedirectionComponent } from './redirection.component';
import { RedirectionDetailComponent } from './redirection-detail.component';
import { RedirectionUpdateComponent } from './redirection-update.component';

@Injectable({ providedIn: 'root' })
export class RedirectionResolve implements Resolve<IRedirection> {
  constructor(private service: RedirectionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRedirection> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((redirection: HttpResponse<Redirection>) => {
          if (redirection.body) {
            return of(redirection.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Redirection());
  }
}

export const redirectionRoute: Routes = [
  {
    path: '',
    component: RedirectionComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fluidQrServerApp.redirection.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RedirectionDetailComponent,
    resolve: {
      redirection: RedirectionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fluidQrServerApp.redirection.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RedirectionUpdateComponent,
    resolve: {
      redirection: RedirectionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fluidQrServerApp.redirection.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RedirectionUpdateComponent,
    resolve: {
      redirection: RedirectionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fluidQrServerApp.redirection.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
