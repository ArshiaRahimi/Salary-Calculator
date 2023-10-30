import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITblEmployeeInformation, NewTblEmployeeInformation } from '../tbl-employee-information.model';

export type PartialUpdateTblEmployeeInformation = Partial<ITblEmployeeInformation> & Pick<ITblEmployeeInformation, 'id'>;

export type EntityResponseType = HttpResponse<ITblEmployeeInformation>;
export type EntityArrayResponseType = HttpResponse<ITblEmployeeInformation[]>;

@Injectable({ providedIn: 'root' })
export class TblEmployeeInformationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tbl-employee-informations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tblEmployeeInformation: NewTblEmployeeInformation): Observable<EntityResponseType> {
    return this.http.post<ITblEmployeeInformation>(this.resourceUrl, tblEmployeeInformation, { observe: 'response' });
  }

  update(tblEmployeeInformation: ITblEmployeeInformation): Observable<EntityResponseType> {
    return this.http.put<ITblEmployeeInformation>(
      `${this.resourceUrl}/${this.getTblEmployeeInformationIdentifier(tblEmployeeInformation)}`,
      tblEmployeeInformation,
      { observe: 'response' }
    );
  }

  partialUpdate(tblEmployeeInformation: PartialUpdateTblEmployeeInformation): Observable<EntityResponseType> {
    return this.http.patch<ITblEmployeeInformation>(
      `${this.resourceUrl}/${this.getTblEmployeeInformationIdentifier(tblEmployeeInformation)}`,
      tblEmployeeInformation,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITblEmployeeInformation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITblEmployeeInformation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTblEmployeeInformationIdentifier(tblEmployeeInformation: Pick<ITblEmployeeInformation, 'id'>): number {
    return tblEmployeeInformation.id;
  }

  compareTblEmployeeInformation(o1: Pick<ITblEmployeeInformation, 'id'> | null, o2: Pick<ITblEmployeeInformation, 'id'> | null): boolean {
    return o1 && o2 ? this.getTblEmployeeInformationIdentifier(o1) === this.getTblEmployeeInformationIdentifier(o2) : o1 === o2;
  }

  addTblEmployeeInformationToCollectionIfMissing<Type extends Pick<ITblEmployeeInformation, 'id'>>(
    tblEmployeeInformationCollection: Type[],
    ...tblEmployeeInformationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tblEmployeeInformations: Type[] = tblEmployeeInformationsToCheck.filter(isPresent);
    if (tblEmployeeInformations.length > 0) {
      const tblEmployeeInformationCollectionIdentifiers = tblEmployeeInformationCollection.map(
        tblEmployeeInformationItem => this.getTblEmployeeInformationIdentifier(tblEmployeeInformationItem)!
      );
      const tblEmployeeInformationsToAdd = tblEmployeeInformations.filter(tblEmployeeInformationItem => {
        const tblEmployeeInformationIdentifier = this.getTblEmployeeInformationIdentifier(tblEmployeeInformationItem);
        if (tblEmployeeInformationCollectionIdentifiers.includes(tblEmployeeInformationIdentifier)) {
          return false;
        }
        tblEmployeeInformationCollectionIdentifiers.push(tblEmployeeInformationIdentifier);
        return true;
      });
      return [...tblEmployeeInformationsToAdd, ...tblEmployeeInformationCollection];
    }
    return tblEmployeeInformationCollection;
  }
}
