import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IResolucion } from '../resolucion.model';
import { ResolucionService } from '../service/resolucion.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './resolucion-delete-dialog.component.html',
})
export class ResolucionDeleteDialogComponent {
  resolucion?: IResolucion;

  constructor(protected resolucionService: ResolucionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.resolucionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
