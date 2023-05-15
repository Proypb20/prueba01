import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GerdisComponent } from './list/gerdis.component';
import { GerdisDetailComponent } from './detail/gerdis-detail.component';
import { GerdisUpdateComponent } from './update/gerdis-update.component';
import { GerdisDeleteDialogComponent } from './delete/gerdis-delete-dialog.component';
import { GerdisRoutingModule } from './route/gerdis-routing.module';

@NgModule({
  imports: [SharedModule, GerdisRoutingModule],
  declarations: [GerdisComponent, GerdisDetailComponent, GerdisUpdateComponent, GerdisDeleteDialogComponent],
})
export class GerdisModule {}
