import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ResofomComponent } from '../list/resofom.component';
import { ResofomDetailComponent } from '../detail/resofom-detail.component';
import { ResofomUpdateComponent } from '../update/resofom-update.component';
import { ResofomRoutingResolveService } from './resofom-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const resofomRoute: Routes = [
  {
    path: '',
    component: ResofomComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResofomDetailComponent,
    resolve: {
      resofom: ResofomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResofomUpdateComponent,
    resolve: {
      resofom: ResofomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResofomUpdateComponent,
    resolve: {
      resofom: ResofomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(resofomRoute)],
  exports: [RouterModule],
})
export class ResofomRoutingModule {}
