import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ResolucionComponent } from '../list/resolucion.component';
import { ResolucionDetailComponent } from '../detail/resolucion-detail.component';
import { ResolucionUpdateComponent } from '../update/resolucion-update.component';
import { ResolucionRoutingResolveService } from './resolucion-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const resolucionRoute: Routes = [
  {
    path: '',
    component: ResolucionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResolucionDetailComponent,
    resolve: {
      resolucion: ResolucionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResolucionUpdateComponent,
    resolve: {
      resolucion: ResolucionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResolucionUpdateComponent,
    resolve: {
      resolucion: ResolucionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(resolucionRoute)],
  exports: [RouterModule],
})
export class ResolucionRoutingModule {}
