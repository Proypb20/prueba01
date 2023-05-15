import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { GerdisFormService, GerdisFormGroup } from './gerdis-form.service';
import { IGerdis } from '../gerdis.model';
import { GerdisService } from '../service/gerdis.service';

@Component({
  selector: 'jhi-gerdis-update',
  templateUrl: './gerdis-update.component.html',
})
export class GerdisUpdateComponent implements OnInit {
  isSaving = false;
  gerdis: IGerdis | null = null;

  editForm: GerdisFormGroup = this.gerdisFormService.createGerdisFormGroup();

  constructor(
    protected gerdisService: GerdisService,
    protected gerdisFormService: GerdisFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gerdis }) => {
      this.gerdis = gerdis;
      if (gerdis) {
        this.updateForm(gerdis);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gerdis = this.gerdisFormService.getGerdis(this.editForm);
    if (gerdis.id !== null) {
      this.subscribeToSaveResponse(this.gerdisService.update(gerdis));
    } else {
      this.subscribeToSaveResponse(this.gerdisService.create(gerdis));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGerdis>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(gerdis: IGerdis): void {
    this.gerdis = gerdis;
    this.gerdisFormService.resetForm(this.editForm, gerdis);
  }
}
