import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResofom } from '../resofom.model';
import { ResofomService } from '../service/resofom.service';

@Injectable({ providedIn: 'root' })
export class ResofomRoutingResolveService implements Resolve<IResofom | null> {
  constructor(protected service: ResofomService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResofom | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((resofom: HttpResponse<IResofom>) => {
          if (resofom.body) {
            return of(resofom.body);
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
