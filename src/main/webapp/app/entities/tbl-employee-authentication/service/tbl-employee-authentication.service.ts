import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITblEmployeeAuthentication, NewTblEmployeeAuthentication } from '../tbl-employee-authentication.model';

export type PartialUpdateTblEmployeeAuthentication = Partial<ITblEmployeeAuthentication> & Pick<ITblEmployeeAuthentication, 'id'>;

export type EntityResponseType = HttpResponse<ITblEmployeeAuthentication>;
export type EntityArrayResponseType = HttpResponse<ITblEmployeeAuthentication[]>;

@Injectable({ providedIn: 'root' })
export class TblEmployeeAuthenticationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tbl-employee-authentications');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tblEmployeeAuthentication: NewTblEmployeeAuthentication): Observable<EntityResponseType> {
    return this.http.post<ITblEmployeeAuthentication>(this.resourceUrl, tblEmployeeAuthentication, { observe: 'response' });
  }

  update(tblEmployeeAuthentication: ITblEmployeeAuthentication): Observable<EntityResponseType> {
    return this.http.put<ITblEmployeeAuthentication>(
      `${this.resourceUrl}/${this.getTblEmployeeAuthenticationIdentifier(tblEmployeeAuthentication)}`,
      tblEmployeeAuthentication,
      { observe: 'response' }
    );
  }

  partialUpdate(tblEmployeeAuthentication: PartialUpdateTblEmployeeAuthentication): Observable<EntityResponseType> {
    return this.http.patch<ITblEmployeeAuthentication>(
      `${this.resourceUrl}/${this.getTblEmployeeAuthenticationIdentifier(tblEmployeeAuthentication)}`,
      tblEmployeeAuthentication,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITblEmployeeAuthentication>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITblEmployeeAuthentication[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTblEmployeeAuthenticationIdentifier(tblEmployeeAuthentication: Pick<ITblEmployeeAuthentication, 'id'>): number {
    return tblEmployeeAuthentication.id;
  }

  compareTblEmployeeAuthentication(
    o1: Pick<ITblEmployeeAuthentication, 'id'> | null,
    o2: Pick<ITblEmployeeAuthentication, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getTblEmployeeAuthenticationIdentifier(o1) === this.getTblEmployeeAuthenticationIdentifier(o2) : o1 === o2;
  }

  addTblEmployeeAuthenticationToCollectionIfMissing<Type extends Pick<ITblEmployeeAuthentication, 'id'>>(
    tblEmployeeAuthenticationCollection: Type[],
    ...tblEmployeeAuthenticationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tblEmployeeAuthentications: Type[] = tblEmployeeAuthenticationsToCheck.filter(isPresent);
    if (tblEmployeeAuthentications.length > 0) {
      const tblEmployeeAuthenticationCollectionIdentifiers = tblEmployeeAuthenticationCollection.map(
        tblEmployeeAuthenticationItem => this.getTblEmployeeAuthenticationIdentifier(tblEmployeeAuthenticationItem)!
      );
      const tblEmployeeAuthenticationsToAdd = tblEmployeeAuthentications.filter(tblEmployeeAuthenticationItem => {
        const tblEmployeeAuthenticationIdentifier = this.getTblEmployeeAuthenticationIdentifier(tblEmployeeAuthenticationItem);
        if (tblEmployeeAuthenticationCollectionIdentifiers.includes(tblEmployeeAuthenticationIdentifier)) {
          return false;
        }
        tblEmployeeAuthenticationCollectionIdentifiers.push(tblEmployeeAuthenticationIdentifier);
        return true;
      });
      return [...tblEmployeeAuthenticationsToAdd, ...tblEmployeeAuthenticationCollection];
    }
    return tblEmployeeAuthenticationCollection;
  }
}
