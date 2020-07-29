import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, tap, switchMap } from 'rxjs/operators';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { MessageService } from 'primeng/api';
import { IPayTo } from 'app/shared/model/pay-to.model';
import { PayToService } from './pay-to.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {
  computeFilterMatchMode,
  lazyLoadEventToServerQueryParams,
  lazyLoadEventToRouterQueryParams,
  fillTableFromQueryParams
} from 'app/shared/util/request-util';
import { ConfirmationService, LazyLoadEvent } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';
import { Table } from 'primeng/table';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'jhi-pay-to',
  templateUrl: './pay-to.component.html'
})
export class PayToComponent implements OnInit, OnDestroy {
  payTos?: IPayTo[];
  eventSubscriber?: Subscription;
  createdOnRange?: Date[];
  modifiedOnRange?: Date[];

  totalItems?: number;
  itemsPerPage!: number;
  loading!: boolean;

  private filtersDetails: { [_: string]: { matchMode?: string; flatten?: (_: any) => string; unflatten?: (_: string) => any } } = {
    id: { matchMode: 'equals', unflatten: (x: string) => +x },
    createdOn: { matchMode: 'between', flatten: a => a.filter((x: string) => x).join(','), unflatten: (a: string) => a.split(',') },
    modifiedOn: { matchMode: 'between', flatten: a => a.filter((x: string) => x).join(','), unflatten: (a: string) => a.split(',') }
  };

  @ViewChild('payToTable', { static: true })
  payToTable!: Table;

  constructor(
    protected payToService: PayToService,
    protected messageService: MessageService,
    protected dataUtils: JhiDataUtils,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected confirmationService: ConfirmationService,
    protected translateService: TranslateService,
    protected datePipe: DatePipe
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.loading = true;
  }

  ngOnInit(): void {
    this.registerChangeInPayTos();
    this.activatedRoute.queryParams
      .pipe(
        tap(queryParams => fillTableFromQueryParams(this.payToTable, queryParams, this.filtersDetails)),
        tap(
          () =>
            (this.createdOnRange =
              this.payToTable.filters.createdOn &&
              this.payToTable.filters.createdOn.value &&
              this.payToTable.filters.createdOn.value.map((x: string) => new Date(x)))
        ),
        tap(
          () =>
            (this.modifiedOnRange =
              this.payToTable.filters.modifiedOn &&
              this.payToTable.filters.modifiedOn.value &&
              this.payToTable.filters.modifiedOn.value.map((x: string) => new Date(x)))
        ),
        tap(() => (this.loading = true)),
        switchMap(() => this.payToService.query(lazyLoadEventToServerQueryParams(this.payToTable.createLazyLoadMetadata()))),
        filter((res: HttpResponse<IPayTo[]>) => res.ok)
      )
      .subscribe(
        (res: HttpResponse<IPayTo[]>) => {
          this.paginatePayTos(res.body!, res.headers);
          this.loading = false;
        },
        (res: HttpErrorResponse) => {
          this.onError(res.message);
          this.loading = false;
        }
      );
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  onLazyLoadEvent(event: LazyLoadEvent): void {
    const queryParams = lazyLoadEventToRouterQueryParams(event, this.filtersDetails);
    this.router.navigate(['/pay-to'], { queryParams });
  }

  filter(value: any, field: string): void {
    this.payToTable.filter(value, field, computeFilterMatchMode(this.filtersDetails[field]));
  }

  delete(id: number): void {
    this.confirmationService.confirm({
      header: this.translateService.instant('entity.delete.title'),
      message: this.translateService.instant('primengtestApp.payTo.delete.question', { id }),
      accept: () => {
        this.payToService.delete(id).subscribe(() => {
          this.eventManager.broadcast({
            name: 'payToListModification',
            content: 'Deleted an payTo'
          });
        });
      }
    });
  }

  trackId(index: number, item: IPayTo): string {
    return '' + item.id;
  }

  byteSize(field: string): string {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType: string, field: string): void {
    this.dataUtils.openFile(contentType, field);
  }

  registerChangeInPayTos(): void {
    this.eventSubscriber = this.eventManager.subscribe('payToListModification', () =>
      this.router.navigate(['/pay-to'], { queryParams: { r: Date.now() } })
    );
  }

  protected paginatePayTos(data: IPayTo[], headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.payTos = data;
  }

  protected onError(errorMessage: string): void {
    this.messageService.add({ severity: 'error', summary: errorMessage });
  }

  onDateSelect(dateRange: Date[] | undefined, column: string, time = false): void {
    const dateToString = time ? (x: Date) => x && x.toISOString() : (x: Date) => x && this.datePipe.transform(x, 'yyyy-MM-dd');
    if (dateRange) {
      this.filter(dateRange.map(dateToString), column);
    } else {
      this.filter(undefined, column);
    }
  }
}
