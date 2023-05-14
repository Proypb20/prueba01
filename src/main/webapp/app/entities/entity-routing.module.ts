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
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
