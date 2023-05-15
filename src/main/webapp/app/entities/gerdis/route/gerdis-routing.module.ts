import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GerdisComponent } from '../list/gerdis.component';
import { GerdisDetailComponent } from '../detail/gerdis-detail.component';
import { GerdisUpdateComponent } from '../update/gerdis-update.component';
import { GerdisRoutingResolveService } from './gerdis-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const gerdisRoute: Routes = [
  {
    path: '',
    component: GerdisComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GerdisDetailComponent,
    resolve: {
      gerdis: GerdisRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GerdisUpdateComponent,
    resolve: {
      gerdis: GerdisRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GerdisUpdateComponent,
    resolve: {
      gerdis: GerdisRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gerdisRoute)],
  exports: [RouterModule],
})
export class GerdisRoutingModule {}
