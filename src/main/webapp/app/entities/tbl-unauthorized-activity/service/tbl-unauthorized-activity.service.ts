import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITblUnauthorizedActivity, NewTblUnauthorizedActivity } from '../tbl-unauthorized-activity.model';

export type PartialUpdateTblUnauthorizedActivity = Partial<ITblUnauthorizedActivity> & Pick<ITblUnauthorizedActivity, 'id'>;

type RestOf<T extends ITblUnauthorizedActivity | NewTblUnauthorizedActivity> = Omit<T, 'readingTime'> & {
  readingTime?: string | null;
};

export type RestTblUnauthorizedActivity = RestOf<ITblUnauthorizedActivity>;

export type NewRestTblUnauthorizedActivity = RestOf<NewTblUnauthorizedActivity>;

export type PartialUpdateRestTblUnauthorizedActivity = RestOf<PartialUpdateTblUnauthorizedActivity>;

export type EntityResponseType = HttpResponse<ITblUnauthorizedActivity>;
export type EntityArrayResponseType = HttpResponse<ITblUnauthorizedActivity[]>;

@Injectable({ providedIn: 'root' })
export class TblUnauthorizedActivityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tbl-unauthorized-activities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tblUnauthorizedActivity: NewTblUnauthorizedActivity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tblUnauthorizedActivity);
    return this.http
      .post<RestTblUnauthorizedActivity>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tblUnauthorizedActivity: ITblUnauthorizedActivity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tblUnauthorizedActivity);
    return this.http
      .put<RestTblUnauthorizedActivity>(`${this.resourceUrl}/${this.getTblUnauthorizedActivityIdentifier(tblUnauthorizedActivity)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tblUnauthorizedActivity: PartialUpdateTblUnauthorizedActivity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tblUnauthorizedActivity);
    return this.http
      .patch<RestTblUnauthorizedActivity>(
        `${this.resourceUrl}/${this.getTblUnauthorizedActivityIdentifier(tblUnauthorizedActivity)}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTblUnauthorizedActivity>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTblUnauthorizedActivity[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTblUnauthorizedActivityIdentifier(tblUnauthorizedActivity: Pick<ITblUnauthorizedActivity, 'id'>): number {
    return tblUnauthorizedActivity.id;
  }

  compareTblUnauthorizedActivity(
    o1: Pick<ITblUnauthorizedActivity, 'id'> | null,
    o2: Pick<ITblUnauthorizedActivity, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getTblUnauthorizedActivityIdentifier(o1) === this.getTblUnauthorizedActivityIdentifier(o2) : o1 === o2;
  }

  addTblUnauthorizedActivityToCollectionIfMissing<Type extends Pick<ITblUnauthorizedActivity, 'id'>>(
    tblUnauthorizedActivityCollection: Type[],
    ...tblUnauthorizedActivitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tblUnauthorizedActivities: Type[] = tblUnauthorizedActivitiesToCheck.filter(isPresent);
    if (tblUnauthorizedActivities.length > 0) {
      const tblUnauthorizedActivityCollectionIdentifiers = tblUnauthorizedActivityCollection.map(
        tblUnauthorizedActivityItem => this.getTblUnauthorizedActivityIdentifier(tblUnauthorizedActivityItem)!
      );
      const tblUnauthorizedActivitiesToAdd = tblUnauthorizedActivities.filter(tblUnauthorizedActivityItem => {
        const tblUnauthorizedActivityIdentifier = this.getTblUnauthorizedActivityIdentifier(tblUnauthorizedActivityItem);
        if (tblUnauthorizedActivityCollectionIdentifiers.includes(tblUnauthorizedActivityIdentifier)) {
          return false;
        }
        tblUnauthorizedActivityCollectionIdentifiers.push(tblUnauthorizedActivityIdentifier);
        return true;
      });
      return [...tblUnauthorizedActivitiesToAdd, ...tblUnauthorizedActivityCollection];
    }
    return tblUnauthorizedActivityCollection;
  }

  protected convertDateFromClient<T extends ITblUnauthorizedActivity | NewTblUnauthorizedActivity | PartialUpdateTblUnauthorizedActivity>(
    tblUnauthorizedActivity: T
  ): RestOf<T> {
    return {
      ...tblUnauthorizedActivity,
      readingTime: tblUnauthorizedActivity.readingTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTblUnauthorizedActivity: RestTblUnauthorizedActivity): ITblUnauthorizedActivity {
    return {
      ...restTblUnauthorizedActivity,
      readingTime: restTblUnauthorizedActivity.readingTime ? dayjs(restTblUnauthorizedActivity.readingTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTblUnauthorizedActivity>): HttpResponse<ITblUnauthorizedActivity> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTblUnauthorizedActivity[]>): HttpResponse<ITblUnauthorizedActivity[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
