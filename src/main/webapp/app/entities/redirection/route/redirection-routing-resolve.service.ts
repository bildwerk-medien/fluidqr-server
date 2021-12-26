import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRedirection, Redirection } from '../redirection.model';
import { RedirectionService } from '../service/redirection.service';

@Injectable({ providedIn: 'root' })
export class RedirectionRoutingResolveService implements Resolve<IRedirection> {
  constructor(protected service: RedirectionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRedirection> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((redirection: HttpResponse<Redirection>) => {
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
