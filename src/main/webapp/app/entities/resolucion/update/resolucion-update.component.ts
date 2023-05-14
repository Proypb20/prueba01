import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ResolucionFormService, ResolucionFormGroup } from './resolucion-form.service';
import { IResolucion } from '../resolucion.model';
import { ResolucionService } from '../service/resolucion.service';

@Component({
  selector: 'jhi-resolucion-update',
  templateUrl: './resolucion-update.component.html',
})
export class ResolucionUpdateComponent implements OnInit {
  isSaving = false;
  resolucion: IResolucion | null = null;

  editForm: ResolucionFormGroup = this.resolucionFormService.createResolucionFormGroup();

  constructor(
    protected resolucionService: ResolucionService,
    protected resolucionFormService: ResolucionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resolucion }) => {
      this.resolucion = resolucion;
      if (resolucion) {
        this.updateForm(resolucion);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resolucion = this.resolucionFormService.getResolucion(this.editForm);
    if (resolucion.id !== null) {
      this.subscribeToSaveResponse(this.resolucionService.update(resolucion));
    } else {
      this.subscribeToSaveResponse(this.resolucionService.create(resolucion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResolucion>>): void {
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

  protected updateForm(resolucion: IResolucion): void {
    this.resolucion = resolucion;
    this.resolucionFormService.resetForm(this.editForm, resolucion);
  }
}
