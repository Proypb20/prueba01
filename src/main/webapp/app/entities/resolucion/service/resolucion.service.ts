import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResolucion, NewResolucion } from '../resolucion.model';

export type PartialUpdateResolucion = Partial<IResolucion> & Pick<IResolucion, 'id'>;

type RestOf<T extends IResolucion | NewResolucion> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

export type RestResolucion = RestOf<IResolucion>;

export type NewRestResolucion = RestOf<NewResolucion>;

export type PartialUpdateRestResolucion = RestOf<PartialUpdateResolucion>;

export type EntityResponseType = HttpResponse<IResolucion>;
export type EntityArrayResponseType = HttpResponse<IResolucion[]>;

@Injectable({ providedIn: 'root' })
export class ResolucionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/resolucions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(resolucion: NewResolucion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resolucion);
    return this.http
      .post<RestResolucion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(resolucion: IResolucion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resolucion);
    return this.http
      .put<RestResolucion>(`${this.resourceUrl}/${this.getResolucionIdentifier(resolucion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(resolucion: PartialUpdateResolucion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resolucion);
    return this.http
      .patch<RestResolucion>(`${this.resourceUrl}/${this.getResolucionIdentifier(resolucion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestResolucion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestResolucion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getResolucionIdentifier(resolucion: Pick<IResolucion, 'id'>): number {
    return resolucion.id;
  }

  compareResolucion(o1: Pick<IResolucion, 'id'> | null, o2: Pick<IResolucion, 'id'> | null): boolean {
    return o1 && o2 ? this.getResolucionIdentifier(o1) === this.getResolucionIdentifier(o2) : o1 === o2;
  }

  addResolucionToCollectionIfMissing<Type extends Pick<IResolucion, 'id'>>(
    resolucionCollection: Type[],
    ...resolucionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const resolucions: Type[] = resolucionsToCheck.filter(isPresent);
    if (resolucions.length > 0) {
      const resolucionCollectionIdentifiers = resolucionCollection.map(resolucionItem => this.getResolucionIdentifier(resolucionItem)!);
      const resolucionsToAdd = resolucions.filter(resolucionItem => {
        const resolucionIdentifier = this.getResolucionIdentifier(resolucionItem);
        if (resolucionCollectionIdentifiers.includes(resolucionIdentifier)) {
          return false;
        }
        resolucionCollectionIdentifiers.push(resolucionIdentifier);
        return true;
      });
      return [...resolucionsToAdd, ...resolucionCollection];
    }
    return resolucionCollection;
  }

  protected convertDateFromClient<T extends IResolucion | NewResolucion | PartialUpdateResolucion>(resolucion: T): RestOf<T> {
    return {
      ...resolucion,
      fecha: resolucion.fecha?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restResolucion: RestResolucion): IResolucion {
    return {
      ...restResolucion,
      fecha: restResolucion.fecha ? dayjs(restResolucion.fecha) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestResolucion>): HttpResponse<IResolucion> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestResolucion[]>): HttpResponse<IResolucion[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
