import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ResolucionComponent } from './list/resolucion.component';
import { ResolucionDetailComponent } from './detail/resolucion-detail.component';
import { ResolucionUpdateComponent } from './update/resolucion-update.component';
import { ResolucionDeleteDialogComponent } from './delete/resolucion-delete-dialog.component';
import { ResolucionRoutingModule } from './route/resolucion-routing.module';

@NgModule({
  imports: [SharedModule, ResolucionRoutingModule],
  declarations: [ResolucionComponent, ResolucionDetailComponent, ResolucionUpdateComponent, ResolucionDeleteDialogComponent],
})
export class ResolucionModule {}
