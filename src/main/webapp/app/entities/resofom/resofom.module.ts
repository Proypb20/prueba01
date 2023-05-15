import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ResofomComponent } from './list/resofom.component';
import { ResofomDetailComponent } from './detail/resofom-detail.component';
import { ResofomUpdateComponent } from './update/resofom-update.component';
import { ResofomDeleteDialogComponent } from './delete/resofom-delete-dialog.component';
import { ResofomRoutingModule } from './route/resofom-routing.module';

@NgModule({
  imports: [SharedModule, ResofomRoutingModule],
  declarations: [ResofomComponent, ResofomDetailComponent, ResofomUpdateComponent, ResofomDeleteDialogComponent],
})
export class ResofomModule {}
