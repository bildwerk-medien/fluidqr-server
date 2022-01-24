import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRedirection, getRedirectionIdentifier } from '../redirection.model';

export type EntityResponseType = HttpResponse<IRedirection>;
export type EntityArrayResponseType = HttpResponse<IRedirection[]>;

@Injectable({ providedIn: 'root' })
export class RedirectionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/redirections');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(redirection: IRedirection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(redirection);
    return this.http
      .post<IRedirection>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(redirection: IRedirection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(redirection);
    return this.http
      .put<IRedirection>(`${this.resourceUrl}/${getRedirectionIdentifier(redirection) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(redirection: IRedirection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(redirection);
    return this.http
      .patch<IRedirection>(`${this.resourceUrl}/${getRedirectionIdentifier(redirection) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRedirection>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRedirection[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRedirectionToCollectionIfMissing(
    redirectionCollection: IRedirection[],
    ...redirectionsToCheck: (IRedirection | null | undefined)[]
  ): IRedirection[] {
    const redirections: IRedirection[] = redirectionsToCheck.filter(isPresent);
    if (redirections.length > 0) {
      const redirectionCollectionIdentifiers = redirectionCollection.map(redirectionItem => getRedirectionIdentifier(redirectionItem)!);
      const redirectionsToAdd = redirections.filter(redirectionItem => {
        const redirectionIdentifier = getRedirectionIdentifier(redirectionItem);
        if (redirectionIdentifier == null || redirectionCollectionIdentifiers.includes(redirectionIdentifier)) {
          return false;
        }
        redirectionCollectionIdentifiers.push(redirectionIdentifier);
        return true;
      });
      return [...redirectionsToAdd, ...redirectionCollection];
    }
    return redirectionCollection;
  }

  protected convertDateFromClient(redirection: IRedirection): IRedirection {
    return Object.assign({}, redirection, {
      creation: redirection.creation?.isValid() ? redirection.creation.toJSON() : undefined,
      startDate: redirection.startDate?.isValid() ? redirection.startDate.toJSON() : undefined,
      endDate: redirection.endDate?.isValid() ? redirection.endDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creation = res.body.creation ? dayjs(res.body.creation) : undefined;
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((redirection: IRedirection) => {
        redirection.creation = redirection.creation ? dayjs(redirection.creation) : undefined;
        redirection.startDate = redirection.startDate ? dayjs(redirection.startDate) : undefined;
        redirection.endDate = redirection.endDate ? dayjs(redirection.endDate) : undefined;
      });
    }
    return res;
  }
}
