import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITblWorkingHours, NewTblWorkingHours } from '../tbl-working-hours.model';

export type PartialUpdateTblWorkingHours = Partial<ITblWorkingHours> & Pick<ITblWorkingHours, 'id'>;

export type EntityResponseType = HttpResponse<ITblWorkingHours>;
export type EntityArrayResponseType = HttpResponse<ITblWorkingHours[]>;

@Injectable({ providedIn: 'root' })
export class TblWorkingHoursService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tbl-working-hours');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tblWorkingHours: NewTblWorkingHours): Observable<EntityResponseType> {
    return this.http.post<ITblWorkingHours>(this.resourceUrl, tblWorkingHours, { observe: 'response' });
  }

  update(tblWorkingHours: ITblWorkingHours): Observable<EntityResponseType> {
    return this.http.put<ITblWorkingHours>(`${this.resourceUrl}/${this.getTblWorkingHoursIdentifier(tblWorkingHours)}`, tblWorkingHours, {
      observe: 'response',
    });
  }

  partialUpdate(tblWorkingHours: PartialUpdateTblWorkingHours): Observable<EntityResponseType> {
    return this.http.patch<ITblWorkingHours>(`${this.resourceUrl}/${this.getTblWorkingHoursIdentifier(tblWorkingHours)}`, tblWorkingHours, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITblWorkingHours>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITblWorkingHours[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTblWorkingHoursIdentifier(tblWorkingHours: Pick<ITblWorkingHours, 'id'>): number {
    return tblWorkingHours.id;
  }

  compareTblWorkingHours(o1: Pick<ITblWorkingHours, 'id'> | null, o2: Pick<ITblWorkingHours, 'id'> | null): boolean {
    return o1 && o2 ? this.getTblWorkingHoursIdentifier(o1) === this.getTblWorkingHoursIdentifier(o2) : o1 === o2;
  }

  addTblWorkingHoursToCollectionIfMissing<Type extends Pick<ITblWorkingHours, 'id'>>(
    tblWorkingHoursCollection: Type[],
    ...tblWorkingHoursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tblWorkingHours: Type[] = tblWorkingHoursToCheck.filter(isPresent);
    if (tblWorkingHours.length > 0) {
      const tblWorkingHoursCollectionIdentifiers = tblWorkingHoursCollection.map(
        tblWorkingHoursItem => this.getTblWorkingHoursIdentifier(tblWorkingHoursItem)!
      );
      const tblWorkingHoursToAdd = tblWorkingHours.filter(tblWorkingHoursItem => {
        const tblWorkingHoursIdentifier = this.getTblWorkingHoursIdentifier(tblWorkingHoursItem);
        if (tblWorkingHoursCollectionIdentifiers.includes(tblWorkingHoursIdentifier)) {
          return false;
        }
        tblWorkingHoursCollectionIdentifiers.push(tblWorkingHoursIdentifier);
        return true;
      });
      return [...tblWorkingHoursToAdd, ...tblWorkingHoursCollection];
    }
    return tblWorkingHoursCollection;
  }
}
