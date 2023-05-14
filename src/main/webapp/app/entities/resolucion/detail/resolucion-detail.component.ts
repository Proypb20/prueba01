import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResolucion } from '../resolucion.model';

@Component({
  selector: 'jhi-resolucion-detail',
  templateUrl: './resolucion-detail.component.html',
})
export class ResolucionDetailComponent implements OnInit {
  resolucion: IResolucion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resolucion }) => {
      this.resolucion = resolucion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
