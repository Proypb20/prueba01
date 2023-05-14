import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IResolucion } from '../resolucion.model';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, ResolucionService } from '../service/resolucion.service';
import { ResolucionDeleteDialogComponent } from '../delete/resolucion-delete-dialog.component';
import { SortService } from 'app/shared/sort/sort.service';
import { FormBuilder, Validators } from '@angular/forms'; // de filtro

@Component({
  selector: 'jhi-resolucion',
  templateUrl: './resolucion.component.html',
})
export class ResolucionComponent implements OnInit {
  resolucions?: IResolucion[];
  isLoading = false;

  predicate = 'id';
  ascending = true;
  // filtro
  nroRes = '';
  res: any;

  showFilter = false;
  filterNroRes = '';

  findForm = this.fb.group({
    resolucion: [null],
  });
  // fin filtro

  constructor(
    protected resolucionService: ResolucionService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal,
    protected fb: FormBuilder // de filtro
  ) {}

  trackId = (_index: number, item: IResolucion): number => this.resolucionService.getResolucionIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  delete(resolucion: IResolucion): void {
    const modalRef = this.modalService.open(ResolucionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.resolucion = resolucion;
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
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.predicate, this.ascending);
  }

  // mostrar filtro
  showFilters(): void {
    if (this.showFilter) {
      this.showFilter = false;
      this.filterNroRes = '';
      this.loadFromBackendWithRouteInformations().subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
    } else {
      this.showFilter = true;
    }
  }

  onChangeRes(): void {
    this.nroRes = '';

    if (this.findForm.get('resolucion')!.value! !== null) {
      this.nroRes = this.findForm.get('resolucion')!.value!;
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

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.resolucions = this.refineData(dataFromBody);
  }

  protected refineData(data: IResolucion[]): IResolucion[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected fillComponentAttributesFromResponseBody(data: IResolucion[] | null): IResolucion[] {
    return data ?? [];
  }

  protected queryBackend(predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    if (this.filterNroRes !== '') {
      this.nroRes = this.filterNroRes;
    }
    const queryObject = {
      sort: this.getSortQueryParam(predicate, ascending),
      'resolucion.contains': this.nroRes, // de filtro
    };
    return this.resolucionService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
