import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

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

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGoogleUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGoogleUser[]>(this.resourceUrl, { params: options, observe: 'response' });
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
}
