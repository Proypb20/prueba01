import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IResofom } from '../resofom.model';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, ResofomService } from '../service/resofom.service';
import { EntityArrayResponseType as EntityArrayResponseType2, GerdisService } from 'app/entities/gerdis/service/gerdis.service';
import { ResofomDeleteDialogComponent } from '../delete/resofom-delete-dialog.component';
import { SortService } from 'app/shared/sort/sort.service';
import { FormBuilder, Validators } from '@angular/forms'; // de filtro
import { IGerdis } from 'app/entities/gerdis/gerdis.model';

@Component({
  selector: 'jhi-resofom',
  templateUrl: './resofom.component.html',
})
export class ResofomComponent implements OnInit {
  resofoms?: IResofom[];
  gerdis?: IGerdis[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  //filtro
  g: any;
  gId = 0;

  showFilter = false;
  filterGerdis = 0;

  findForm = this.fb.group({
    gerdis: [null],
  });
  // fin filtro

  constructor(
    protected resofomService: ResofomService,
    protected gerdisService: GerdisService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal,
    protected fb: FormBuilder // de filtro
  ) {}

  trackId = (_index: number, item: IResofom): number => this.resofomService.getResofomIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  delete(resofom: IResofom): void {
    const modalRef = this.modalService.open(ResofomDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.resofom = resofom;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
    this.loadFromBackendWithRouteInformations2().subscribe({
      next: (res: EntityArrayResponseType2) => {
        this.onResponseSuccessGerdis(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.predicate, this.ascending);
  }

  // mostrar filtro
  showFilters(): void {
    if (this.showFilter) {
      this.showFilter = false;
      this.filterGerdis = 0;
      this.loadFromBackendWithRouteInformations().subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
    } else {
      this.showFilter = true;
    }
  }

  onChangeGerdis(): void {
    // Agregado
    if (this.findForm.get('gerdis')!.value! !== null) {
      this.g = this.findForm.get('gerdis')!.value!;
      this.gId = this.g!.id;
    } else {
      this.gId = 0;
    }
    // Fin Agregado

    if (this.gId !== 0) {
      this.filterGerdis = this.gId;
    } else {
      if (this.findForm.get('gerdis')!.value! == null) {
        this.filterGerdis = 0;
        this.gId = this.filterGerdis;
      } else {
        this.g = this.findForm.get('gerdis')!.value!;
        this.filterGerdis = this.g!.id;
        this.gId = this.filterGerdis;
      }
    }

    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.predicate, this.ascending))
    );
  }

  protected loadFromBackendWithRouteInformations2(): Observable<EntityArrayResponseType2> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend2(this.predicate, this.ascending))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.resofoms = this.refineData(dataFromBody);
  }

  protected onResponseSuccessGerdis(response: EntityArrayResponseType2): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody2(response.body);
    this.gerdis = this.refineData2(dataFromBody);
  }

  protected refineData(data: IResofom[]): IResofom[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected refineData2(data: IGerdis[]): IGerdis[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected fillComponentAttributesFromResponseBody(data: IResofom[] | null): IResofom[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseBody2(data: IGerdis[] | null): IGerdis[] {
    return data ?? [];
  }

  protected queryBackend(predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject = {
      eagerload: true,
      sort: this.getSortQueryParam(predicate, ascending),
      'gerdisId.equals': this.gId, // de filtro, comparar con
    };
    return this.resofomService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected queryBackend2(predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType2> {
    this.isLoading = true;
    const queryObject = {
      sort: this.getSortQueryParam(predicate, ascending),
      'id.equals': this.gId, // de filtro, comparar con
    };
    return this.gerdisService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
