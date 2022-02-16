import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGoogleUser, getGoogleUserIdentifier } from '../google-user.model';

export type EntityResponseType = HttpResponse<IGoogleUser>;
export type EntityArrayResponseType = HttpResponse<IGoogleUser[]>;

@Injectable({ providedIn: 'root' })
export class GoogleUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/google-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(googleUser: IGoogleUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(googleUser);
    return this.http
      .post<IGoogleUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(googleUser: IGoogleUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(googleUser);
    return this.http
      .put<IGoogleUser>(`${this.resourceUrl}/${getGoogleUserIdentifier(googleUser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(googleUser: IGoogleUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(googleUser);
    return this.http
      .patch<IGoogleUser>(`${this.resourceUrl}/${getGoogleUserIdentifier(googleUser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IGoogleUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGoogleUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGoogleUserToCollectionIfMissing(
    googleUserCollection: IGoogleUser[],
    ...googleUsersToCheck: (IGoogleUser | null | undefined)[]
  ): IGoogleUser[] {
    const googleUsers: IGoogleUser[] = googleUsersToCheck.filter(isPresent);
    if (googleUsers.length > 0) {
      const googleUserCollectionIdentifiers = googleUserCollection.map(googleUserItem => getGoogleUserIdentifier(googleUserItem)!);
      const googleUsersToAdd = googleUsers.filter(googleUserItem => {
        const googleUserIdentifier = getGoogleUserIdentifier(googleUserItem);
        if (googleUserIdentifier == null || googleUserCollectionIdentifiers.includes(googleUserIdentifier)) {
          return false;
        }
        googleUserCollectionIdentifiers.push(googleUserIdentifier);
        return true;
      });
      return [...googleUsersToAdd, ...googleUserCollection];
    }
    return googleUserCollection;
  }

  protected convertDateFromClient(googleUser: IGoogleUser): IGoogleUser {
    return Object.assign({}, googleUser, {
      creationTime: googleUser.creationTime?.isValid() ? googleUser.creationTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationTime = res.body.creationTime ? dayjs(res.body.creationTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((googleUser: IGoogleUser) => {
        googleUser.creationTime = googleUser.creationTime ? dayjs(googleUser.creationTime) : undefined;
      });
    }
    return res;
  }
}
