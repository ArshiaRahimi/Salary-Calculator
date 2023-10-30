import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITblOffDay, NewTblOffDay } from '../tbl-off-day.model';

export type PartialUpdateTblOffDay = Partial<ITblOffDay> & Pick<ITblOffDay, 'id'>;

export type EntityResponseType = HttpResponse<ITblOffDay>;
export type EntityArrayResponseType = HttpResponse<ITblOffDay[]>;

@Injectable({ providedIn: 'root' })
export class TblOffDayService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tbl-off-days');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tblOffDay: NewTblOffDay): Observable<EntityResponseType> {
    return this.http.post<ITblOffDay>(this.resourceUrl, tblOffDay, { observe: 'response' });
  }

  update(tblOffDay: ITblOffDay): Observable<EntityResponseType> {
    return this.http.put<ITblOffDay>(`${this.resourceUrl}/${this.getTblOffDayIdentifier(tblOffDay)}`, tblOffDay, { observe: 'response' });
  }

  partialUpdate(tblOffDay: PartialUpdateTblOffDay): Observable<EntityResponseType> {
    return this.http.patch<ITblOffDay>(`${this.resourceUrl}/${this.getTblOffDayIdentifier(tblOffDay)}`, tblOffDay, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITblOffDay>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITblOffDay[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTblOffDayIdentifier(tblOffDay: Pick<ITblOffDay, 'id'>): number {
    return tblOffDay.id;
  }

  compareTblOffDay(o1: Pick<ITblOffDay, 'id'> | null, o2: Pick<ITblOffDay, 'id'> | null): boolean {
    return o1 && o2 ? this.getTblOffDayIdentifier(o1) === this.getTblOffDayIdentifier(o2) : o1 === o2;
  }

  addTblOffDayToCollectionIfMissing<Type extends Pick<ITblOffDay, 'id'>>(
    tblOffDayCollection: Type[],
    ...tblOffDaysToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tblOffDays: Type[] = tblOffDaysToCheck.filter(isPresent);
    if (tblOffDays.length > 0) {
      const tblOffDayCollectionIdentifiers = tblOffDayCollection.map(tblOffDayItem => this.getTblOffDayIdentifier(tblOffDayItem)!);
      const tblOffDaysToAdd = tblOffDays.filter(tblOffDayItem => {
        const tblOffDayIdentifier = this.getTblOffDayIdentifier(tblOffDayItem);
        if (tblOffDayCollectionIdentifiers.includes(tblOffDayIdentifier)) {
          return false;
        }
        tblOffDayCollectionIdentifiers.push(tblOffDayIdentifier);
        return true;
      });
      return [...tblOffDaysToAdd, ...tblOffDayCollection];
    }
    return tblOffDayCollection;
  }
}
