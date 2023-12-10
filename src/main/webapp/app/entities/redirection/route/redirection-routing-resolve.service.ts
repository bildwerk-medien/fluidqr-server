import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRedirection } from '../redirection.model';
import { RedirectionService } from '../service/redirection.service';

export const redirectionResolve = (route: ActivatedRouteSnapshot): Observable<null | IRedirection> => {
  const id = route.params['id'];
  if (id) {
    return inject(RedirectionService)
      .find(id)
      .pipe(
        mergeMap((redirection: HttpResponse<IRedirection>) => {
          if (redirection.body) {
            return of(redirection.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default redirectionResolve;
