import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGerdis } from '../gerdis.model';
import { GerdisService } from '../service/gerdis.service';

@Injectable({ providedIn: 'root' })
export class GerdisRoutingResolveService implements Resolve<IGerdis | null> {
  constructor(protected service: GerdisService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGerdis | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gerdis: HttpResponse<IGerdis>) => {
          if (gerdis.body) {
            return of(gerdis.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
