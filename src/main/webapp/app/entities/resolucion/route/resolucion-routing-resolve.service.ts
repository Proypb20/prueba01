import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResolucion } from '../resolucion.model';
import { ResolucionService } from '../service/resolucion.service';

@Injectable({ providedIn: 'root' })
export class ResolucionRoutingResolveService implements Resolve<IResolucion | null> {
  constructor(protected service: ResolucionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResolucion | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((resolucion: HttpResponse<IResolucion>) => {
          if (resolucion.body) {
            return of(resolucion.body);
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
