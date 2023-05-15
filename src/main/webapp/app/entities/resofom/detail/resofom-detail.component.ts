import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResofom } from '../resofom.model';

@Component({
  selector: 'jhi-resofom-detail',
  templateUrl: './resofom-detail.component.html',
})
export class ResofomDetailComponent implements OnInit {
  resofom: IResofom | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resofom }) => {
      this.resofom = resofom;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
