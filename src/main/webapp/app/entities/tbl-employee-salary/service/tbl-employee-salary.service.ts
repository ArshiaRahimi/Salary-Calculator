import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITblEmployeeSalary, NewTblEmployeeSalary } from '../tbl-employee-salary.model';

export type PartialUpdateTblEmployeeSalary = Partial<ITblEmployeeSalary> & Pick<ITblEmployeeSalary, 'id'>;

type RestOf<T extends ITblEmployeeSalary | NewTblEmployeeSalary> = Omit<T, 'dateCalculated'> & {
  dateCalculated?: string | null;
};

export type RestTblEmployeeSalary = RestOf<ITblEmployeeSalary>;

export type NewRestTblEmployeeSalary = RestOf<NewTblEmployeeSalary>;

export type PartialUpdateRestTblEmployeeSalary = RestOf<PartialUpdateTblEmployeeSalary>;

export type EntityResponseType = HttpResponse<ITblEmployeeSalary>;
export type EntityArrayResponseType = HttpResponse<ITblEmployeeSalary[]>;

@Injectable({ providedIn: 'root' })
export class TblEmployeeSalaryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tbl-employee-salaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tblEmployeeSalary: NewTblEmployeeSalary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tblEmployeeSalary);
    return this.http
      .post<RestTblEmployeeSalary>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tblEmployeeSalary: ITblEmployeeSalary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tblEmployeeSalary);
    return this.http
      .put<RestTblEmployeeSalary>(`${this.resourceUrl}/${this.getTblEmployeeSalaryIdentifier(tblEmployeeSalary)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tblEmployeeSalary: PartialUpdateTblEmployeeSalary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tblEmployeeSalary);
    return this.http
      .patch<RestTblEmployeeSalary>(`${this.resourceUrl}/${this.getTblEmployeeSalaryIdentifier(tblEmployeeSalary)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTblEmployeeSalary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTblEmployeeSalary[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTblEmployeeSalaryIdentifier(tblEmployeeSalary: Pick<ITblEmployeeSalary, 'id'>): number {
    return tblEmployeeSalary.id;
  }

  compareTblEmployeeSalary(o1: Pick<ITblEmployeeSalary, 'id'> | null, o2: Pick<ITblEmployeeSalary, 'id'> | null): boolean {
    return o1 && o2 ? this.getTblEmployeeSalaryIdentifier(o1) === this.getTblEmployeeSalaryIdentifier(o2) : o1 === o2;
  }

  addTblEmployeeSalaryToCollectionIfMissing<Type extends Pick<ITblEmployeeSalary, 'id'>>(
    tblEmployeeSalaryCollection: Type[],
    ...tblEmployeeSalariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tblEmployeeSalaries: Type[] = tblEmployeeSalariesToCheck.filter(isPresent);
    if (tblEmployeeSalaries.length > 0) {
      const tblEmployeeSalaryCollectionIdentifiers = tblEmployeeSalaryCollection.map(
        tblEmployeeSalaryItem => this.getTblEmployeeSalaryIdentifier(tblEmployeeSalaryItem)!
      );
      const tblEmployeeSalariesToAdd = tblEmployeeSalaries.filter(tblEmployeeSalaryItem => {
        const tblEmployeeSalaryIdentifier = this.getTblEmployeeSalaryIdentifier(tblEmployeeSalaryItem);
        if (tblEmployeeSalaryCollectionIdentifiers.includes(tblEmployeeSalaryIdentifier)) {
          return false;
        }
        tblEmployeeSalaryCollectionIdentifiers.push(tblEmployeeSalaryIdentifier);
        return true;
      });
      return [...tblEmployeeSalariesToAdd, ...tblEmployeeSalaryCollection];
    }
    return tblEmployeeSalaryCollection;
  }

  protected convertDateFromClient<T extends ITblEmployeeSalary | NewTblEmployeeSalary | PartialUpdateTblEmployeeSalary>(
    tblEmployeeSalary: T
  ): RestOf<T> {
    return {
      ...tblEmployeeSalary,
      dateCalculated: tblEmployeeSalary.dateCalculated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTblEmployeeSalary: RestTblEmployeeSalary): ITblEmployeeSalary {
    return {
      ...restTblEmployeeSalary,
      dateCalculated: restTblEmployeeSalary.dateCalculated ? dayjs(restTblEmployeeSalary.dateCalculated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTblEmployeeSalary>): HttpResponse<ITblEmployeeSalary> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTblEmployeeSalary[]>): HttpResponse<ITblEmployeeSalary[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
