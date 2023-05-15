import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGerdis } from '../gerdis.model';
import { GerdisService } from '../service/gerdis.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './gerdis-delete-dialog.component.html',
})
export class GerdisDeleteDialogComponent {
  gerdis?: IGerdis;

  constructor(protected gerdisService: GerdisService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gerdisService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
