import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGerdis, NewGerdis } from '../gerdis.model';

export type PartialUpdateGerdis = Partial<IGerdis> & Pick<IGerdis, 'id'>;

export type EntityResponseType = HttpResponse<IGerdis>;
export type EntityArrayResponseType = HttpResponse<IGerdis[]>;

@Injectable({ providedIn: 'root' })
export class GerdisService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gerdis');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gerdis: NewGerdis): Observable<EntityResponseType> {
    return this.http.post<IGerdis>(this.resourceUrl, gerdis, { observe: 'response' });
  }

  update(gerdis: IGerdis): Observable<EntityResponseType> {
    return this.http.put<IGerdis>(`${this.resourceUrl}/${this.getGerdisIdentifier(gerdis)}`, gerdis, { observe: 'response' });
  }

  partialUpdate(gerdis: PartialUpdateGerdis): Observable<EntityResponseType> {
    return this.http.patch<IGerdis>(`${this.resourceUrl}/${this.getGerdisIdentifier(gerdis)}`, gerdis, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGerdis>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGerdis[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGerdisIdentifier(gerdis: Pick<IGerdis, 'id'>): number {
    return gerdis.id;
  }

  compareGerdis(o1: Pick<IGerdis, 'id'> | null, o2: Pick<IGerdis, 'id'> | null): boolean {
    return o1 && o2 ? this.getGerdisIdentifier(o1) === this.getGerdisIdentifier(o2) : o1 === o2;
  }

  addGerdisToCollectionIfMissing<Type extends Pick<IGerdis, 'id'>>(
    gerdisCollection: Type[],
    ...gerdisToCheck: (Type | null | undefined)[]
  ): Type[] {
    const gerdis: Type[] = gerdisToCheck.filter(isPresent);
    if (gerdis.length > 0) {
      const gerdisCollectionIdentifiers = gerdisCollection.map(gerdisItem => this.getGerdisIdentifier(gerdisItem)!);
      const gerdisToAdd = gerdis.filter(gerdisItem => {
        const gerdisIdentifier = this.getGerdisIdentifier(gerdisItem);
        if (gerdisCollectionIdentifiers.includes(gerdisIdentifier)) {
          return false;
        }
        gerdisCollectionIdentifiers.push(gerdisIdentifier);
        return true;
      });
      return [...gerdisToAdd, ...gerdisCollection];
    }
    return gerdisCollection;
  }
}
