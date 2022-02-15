import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGoogleUser, GoogleUser } from '../google-user.model';
import { GoogleUserService } from '../service/google-user.service';

@Injectable({ providedIn: 'root' })
export class GoogleUserRoutingResolveService implements Resolve<IGoogleUser> {
  constructor(protected service: GoogleUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGoogleUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((googleUser: HttpResponse<GoogleUser>) => {
          if (googleUser.body) {
            return of(googleUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GoogleUser());
  }
}
