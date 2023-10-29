import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITblSensorReadings, NewTblSensorReadings } from '../tbl-sensor-readings.model';

export type PartialUpdateTblSensorReadings = Partial<ITblSensorReadings> & Pick<ITblSensorReadings, 'id'>;

type RestOf<T extends ITblSensorReadings | NewTblSensorReadings> = Omit<T, 'readingTime'> & {
  readingTime?: string | null;
};

export type RestTblSensorReadings = RestOf<ITblSensorReadings>;

export type NewRestTblSensorReadings = RestOf<NewTblSensorReadings>;

export type PartialUpdateRestTblSensorReadings = RestOf<PartialUpdateTblSensorReadings>;

export type EntityResponseType = HttpResponse<ITblSensorReadings>;
export type EntityArrayResponseType = HttpResponse<ITblSensorReadings[]>;

@Injectable({ providedIn: 'root' })
export class TblSensorReadingsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tbl-sensor-readings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tblSensorReadings: NewTblSensorReadings): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tblSensorReadings);
    return this.http
      .post<RestTblSensorReadings>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tblSensorReadings: ITblSensorReadings): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tblSensorReadings);
    return this.http
      .put<RestTblSensorReadings>(`${this.resourceUrl}/${this.getTblSensorReadingsIdentifier(tblSensorReadings)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tblSensorReadings: PartialUpdateTblSensorReadings): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tblSensorReadings);
    return this.http
      .patch<RestTblSensorReadings>(`${this.resourceUrl}/${this.getTblSensorReadingsIdentifier(tblSensorReadings)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTblSensorReadings>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTblSensorReadings[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTblSensorReadingsIdentifier(tblSensorReadings: Pick<ITblSensorReadings, 'id'>): number {
    return tblSensorReadings.id;
  }

  compareTblSensorReadings(o1: Pick<ITblSensorReadings, 'id'> | null, o2: Pick<ITblSensorReadings, 'id'> | null): boolean {
    return o1 && o2 ? this.getTblSensorReadingsIdentifier(o1) === this.getTblSensorReadingsIdentifier(o2) : o1 === o2;
  }

  addTblSensorReadingsToCollectionIfMissing<Type extends Pick<ITblSensorReadings, 'id'>>(
    tblSensorReadingsCollection: Type[],
    ...tblSensorReadingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tblSensorReadings: Type[] = tblSensorReadingsToCheck.filter(isPresent);
    if (tblSensorReadings.length > 0) {
      const tblSensorReadingsCollectionIdentifiers = tblSensorReadingsCollection.map(
        tblSensorReadingsItem => this.getTblSensorReadingsIdentifier(tblSensorReadingsItem)!
      );
      const tblSensorReadingsToAdd = tblSensorReadings.filter(tblSensorReadingsItem => {
        const tblSensorReadingsIdentifier = this.getTblSensorReadingsIdentifier(tblSensorReadingsItem);
        if (tblSensorReadingsCollectionIdentifiers.includes(tblSensorReadingsIdentifier)) {
          return false;
        }
        tblSensorReadingsCollectionIdentifiers.push(tblSensorReadingsIdentifier);
        return true;
      });
      return [...tblSensorReadingsToAdd, ...tblSensorReadingsCollection];
    }
    return tblSensorReadingsCollection;
  }

  protected convertDateFromClient<T extends ITblSensorReadings | NewTblSensorReadings | PartialUpdateTblSensorReadings>(
    tblSensorReadings: T
  ): RestOf<T> {
    return {
      ...tblSensorReadings,
      readingTime: tblSensorReadings.readingTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTblSensorReadings: RestTblSensorReadings): ITblSensorReadings {
    return {
      ...restTblSensorReadings,
      readingTime: restTblSensorReadings.readingTime ? dayjs(restTblSensorReadings.readingTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTblSensorReadings>): HttpResponse<ITblSensorReadings> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTblSensorReadings[]>): HttpResponse<ITblSensorReadings[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
