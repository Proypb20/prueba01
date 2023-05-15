import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGerdis } from '../gerdis.model';

@Component({
  selector: 'jhi-gerdis-detail',
  templateUrl: './gerdis-detail.component.html',
})
export class GerdisDetailComponent implements OnInit {
  gerdis: IGerdis | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gerdis }) => {
      this.gerdis = gerdis;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
