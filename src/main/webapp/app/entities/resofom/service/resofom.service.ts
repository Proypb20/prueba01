import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResofom, NewResofom } from '../resofom.model';

export type PartialUpdateResofom = Partial<IResofom> & Pick<IResofom, 'id'>;

export type EntityResponseType = HttpResponse<IResofom>;
export type EntityArrayResponseType = HttpResponse<IResofom[]>;

@Injectable({ providedIn: 'root' })
export class ResofomService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/resofoms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(resofom: NewResofom): Observable<EntityResponseType> {
    return this.http.post<IResofom>(this.resourceUrl, resofom, { observe: 'response' });
  }

  update(resofom: IResofom): Observable<EntityResponseType> {
    return this.http.put<IResofom>(`${this.resourceUrl}/${this.getResofomIdentifier(resofom)}`, resofom, { observe: 'response' });
  }

  partialUpdate(resofom: PartialUpdateResofom): Observable<EntityResponseType> {
    return this.http.patch<IResofom>(`${this.resourceUrl}/${this.getResofomIdentifier(resofom)}`, resofom, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResofom>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResofom[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getResofomIdentifier(resofom: Pick<IResofom, 'id'>): number {
    return resofom.id;
  }

  compareResofom(o1: Pick<IResofom, 'id'> | null, o2: Pick<IResofom, 'id'> | null): boolean {
    return o1 && o2 ? this.getResofomIdentifier(o1) === this.getResofomIdentifier(o2) : o1 === o2;
  }

  addResofomToCollectionIfMissing<Type extends Pick<IResofom, 'id'>>(
    resofomCollection: Type[],
    ...resofomsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const resofoms: Type[] = resofomsToCheck.filter(isPresent);
    if (resofoms.length > 0) {
      const resofomCollectionIdentifiers = resofomCollection.map(resofomItem => this.getResofomIdentifier(resofomItem)!);
      const resofomsToAdd = resofoms.filter(resofomItem => {
        const resofomIdentifier = this.getResofomIdentifier(resofomItem);
        if (resofomCollectionIdentifiers.includes(resofomIdentifier)) {
          return false;
        }
        resofomCollectionIdentifiers.push(resofomIdentifier);
        return true;
      });
      return [...resofomsToAdd, ...resofomCollection];
    }
    return resofomCollection;
  }
}
