import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'resolucion',
        data: { pageTitle: 'Resolucions' },
        loadChildren: () => import('./resolucion/resolucion.module').then(m => m.ResolucionModule),
      },
      {
        path: 'gerdis',
        data: { pageTitle: 'Gerdis' },
        loadChildren: () => import('./gerdis/gerdis.module').then(m => m.GerdisModule),
      },
      {
        path: 'resofom',
        data: { pageTitle: 'Resofoms' },
        loadChildren: () => import('./resofom/resofom.module').then(m => m.ResofomModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
