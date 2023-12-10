import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRedirection, NewRedirection } from '../redirection.model';

export type PartialUpdateRedirection = Partial<IRedirection> & Pick<IRedirection, 'id'>;

type RestOf<T extends IRedirection | NewRedirection> = Omit<T, 'creation' | 'startDate' | 'endDate'> & {
  creation?: string | null;
  startDate?: string | null;
  endDate?: string | null;
};

export type RestRedirection = RestOf<IRedirection>;

export type NewRestRedirection = RestOf<NewRedirection>;

export type PartialUpdateRestRedirection = RestOf<PartialUpdateRedirection>;

export type EntityResponseType = HttpResponse<IRedirection>;
export type EntityArrayResponseType = HttpResponse<IRedirection[]>;

@Injectable({ providedIn: 'root' })
export class RedirectionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/redirections');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(redirection: NewRedirection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(redirection);
    return this.http
      .post<RestRedirection>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(redirection: IRedirection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(redirection);
    return this.http
      .put<RestRedirection>(`${this.resourceUrl}/${this.getRedirectionIdentifier(redirection)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(redirection: PartialUpdateRedirection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(redirection);
    return this.http
      .patch<RestRedirection>(`${this.resourceUrl}/${this.getRedirectionIdentifier(redirection)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRedirection>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRedirection[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRedirectionIdentifier(redirection: Pick<IRedirection, 'id'>): number {
    return redirection.id;
  }

  compareRedirection(o1: Pick<IRedirection, 'id'> | null, o2: Pick<IRedirection, 'id'> | null): boolean {
    return o1 && o2 ? this.getRedirectionIdentifier(o1) === this.getRedirectionIdentifier(o2) : o1 === o2;
  }

  addRedirectionToCollectionIfMissing<Type extends Pick<IRedirection, 'id'>>(
    redirectionCollection: Type[],
    ...redirectionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const redirections: Type[] = redirectionsToCheck.filter(isPresent);
    if (redirections.length > 0) {
      const redirectionCollectionIdentifiers = redirectionCollection.map(
        redirectionItem => this.getRedirectionIdentifier(redirectionItem)!,
      );
      const redirectionsToAdd = redirections.filter(redirectionItem => {
        const redirectionIdentifier = this.getRedirectionIdentifier(redirectionItem);
        if (redirectionCollectionIdentifiers.includes(redirectionIdentifier)) {
          return false;
        }
        redirectionCollectionIdentifiers.push(redirectionIdentifier);
        return true;
      });
      return [...redirectionsToAdd, ...redirectionCollection];
    }
    return redirectionCollection;
  }

  protected convertDateFromClient<T extends IRedirection | NewRedirection | PartialUpdateRedirection>(redirection: T): RestOf<T> {
    return {
      ...redirection,
      creation: redirection.creation?.toJSON() ?? null,
      startDate: redirection.startDate?.toJSON() ?? null,
      endDate: redirection.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRedirection: RestRedirection): IRedirection {
    return {
      ...restRedirection,
      creation: restRedirection.creation ? dayjs(restRedirection.creation) : undefined,
      startDate: restRedirection.startDate ? dayjs(restRedirection.startDate) : undefined,
      endDate: restRedirection.endDate ? dayjs(restRedirection.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRedirection>): HttpResponse<IRedirection> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRedirection[]>): HttpResponse<IRedirection[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
