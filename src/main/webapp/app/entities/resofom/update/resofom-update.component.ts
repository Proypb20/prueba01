import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ResofomFormService, ResofomFormGroup } from './resofom-form.service';
import { IResofom } from '../resofom.model';
import { ResofomService } from '../service/resofom.service';
import { IResolucion } from 'app/entities/resolucion/resolucion.model';
import { ResolucionService } from 'app/entities/resolucion/service/resolucion.service';
import { IGerdis } from 'app/entities/gerdis/gerdis.model';
import { GerdisService } from 'app/entities/gerdis/service/gerdis.service';

@Component({
  selector: 'jhi-resofom-update',
  templateUrl: './resofom-update.component.html',
})
export class ResofomUpdateComponent implements OnInit {
  isSaving = false;
  resofom: IResofom | null = null;

  resolucionsSharedCollection: IResolucion[] = [];
  gerdisSharedCollection: IGerdis[] = [];

  editForm: ResofomFormGroup = this.resofomFormService.createResofomFormGroup();

  constructor(
    protected resofomService: ResofomService,
    protected resofomFormService: ResofomFormService,
    protected resolucionService: ResolucionService,
    protected gerdisService: GerdisService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareResolucion = (o1: IResolucion | null, o2: IResolucion | null): boolean => this.resolucionService.compareResolucion(o1, o2);

  compareGerdis = (o1: IGerdis | null, o2: IGerdis | null): boolean => this.gerdisService.compareGerdis(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resofom }) => {
      this.resofom = resofom;
      if (resofom) {
        this.updateForm(resofom);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resofom = this.resofomFormService.getResofom(this.editForm);
    if (resofom.id !== null) {
      this.subscribeToSaveResponse(this.resofomService.update(resofom));
    } else {
      this.subscribeToSaveResponse(this.resofomService.create(resofom));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResofom>>): void {
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

  protected updateForm(resofom: IResofom): void {
    this.resofom = resofom;
    this.resofomFormService.resetForm(this.editForm, resofom);

    this.resolucionsSharedCollection = this.resolucionService.addResolucionToCollectionIfMissing<IResolucion>(
      this.resolucionsSharedCollection,
      resofom.resolucion
    );
    this.gerdisSharedCollection = this.gerdisService.addGerdisToCollectionIfMissing<IGerdis>(this.gerdisSharedCollection, resofom.gerdis);
  }

  protected loadRelationshipsOptions(): void {
    this.resolucionService
      .query()
      .pipe(map((res: HttpResponse<IResolucion[]>) => res.body ?? []))
      .pipe(
        map((resolucions: IResolucion[]) =>
          this.resolucionService.addResolucionToCollectionIfMissing<IResolucion>(resolucions, this.resofom?.resolucion)
        )
      )
      .subscribe((resolucions: IResolucion[]) => (this.resolucionsSharedCollection = resolucions));

    this.gerdisService
      .query()
      .pipe(map((res: HttpResponse<IGerdis[]>) => res.body ?? []))
      .pipe(map((gerdis: IGerdis[]) => this.gerdisService.addGerdisToCollectionIfMissing<IGerdis>(gerdis, this.resofom?.gerdis)))
      .subscribe((gerdis: IGerdis[]) => (this.gerdisSharedCollection = gerdis));
  }
}
