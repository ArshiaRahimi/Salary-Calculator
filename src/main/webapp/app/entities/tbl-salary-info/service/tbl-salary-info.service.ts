import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITblSalaryInfo, NewTblSalaryInfo } from '../tbl-salary-info.model';

export type PartialUpdateTblSalaryInfo = Partial<ITblSalaryInfo> & Pick<ITblSalaryInfo, 'id'>;

export type EntityResponseType = HttpResponse<ITblSalaryInfo>;
export type EntityArrayResponseType = HttpResponse<ITblSalaryInfo[]>;

@Injectable({ providedIn: 'root' })
export class TblSalaryInfoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tbl-salary-infos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tblSalaryInfo: NewTblSalaryInfo): Observable<EntityResponseType> {
    return this.http.post<ITblSalaryInfo>(this.resourceUrl, tblSalaryInfo, { observe: 'response' });
  }

  update(tblSalaryInfo: ITblSalaryInfo): Observable<EntityResponseType> {
    return this.http.put<ITblSalaryInfo>(`${this.resourceUrl}/${this.getTblSalaryInfoIdentifier(tblSalaryInfo)}`, tblSalaryInfo, {
      observe: 'response',
    });
  }

  partialUpdate(tblSalaryInfo: PartialUpdateTblSalaryInfo): Observable<EntityResponseType> {
    return this.http.patch<ITblSalaryInfo>(`${this.resourceUrl}/${this.getTblSalaryInfoIdentifier(tblSalaryInfo)}`, tblSalaryInfo, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITblSalaryInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITblSalaryInfo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTblSalaryInfoIdentifier(tblSalaryInfo: Pick<ITblSalaryInfo, 'id'>): number {
    return tblSalaryInfo.id;
  }

  compareTblSalaryInfo(o1: Pick<ITblSalaryInfo, 'id'> | null, o2: Pick<ITblSalaryInfo, 'id'> | null): boolean {
    return o1 && o2 ? this.getTblSalaryInfoIdentifier(o1) === this.getTblSalaryInfoIdentifier(o2) : o1 === o2;
  }

  addTblSalaryInfoToCollectionIfMissing<Type extends Pick<ITblSalaryInfo, 'id'>>(
    tblSalaryInfoCollection: Type[],
    ...tblSalaryInfosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tblSalaryInfos: Type[] = tblSalaryInfosToCheck.filter(isPresent);
    if (tblSalaryInfos.length > 0) {
      const tblSalaryInfoCollectionIdentifiers = tblSalaryInfoCollection.map(
        tblSalaryInfoItem => this.getTblSalaryInfoIdentifier(tblSalaryInfoItem)!
      );
      const tblSalaryInfosToAdd = tblSalaryInfos.filter(tblSalaryInfoItem => {
        const tblSalaryInfoIdentifier = this.getTblSalaryInfoIdentifier(tblSalaryInfoItem);
        if (tblSalaryInfoCollectionIdentifiers.includes(tblSalaryInfoIdentifier)) {
          return false;
        }
        tblSalaryInfoCollectionIdentifiers.push(tblSalaryInfoIdentifier);
        return true;
      });
      return [...tblSalaryInfosToAdd, ...tblSalaryInfoCollection];
    }
    return tblSalaryInfoCollection;
  }
}
