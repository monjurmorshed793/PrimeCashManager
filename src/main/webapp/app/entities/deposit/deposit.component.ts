import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, tap, switchMap } from 'rxjs/operators';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { MessageService } from 'primeng/api';
import { IDeposit } from 'app/shared/model/deposit.model';
import { DepositMedium, DEPOSIT_MEDIUM_ARRAY } from 'app/shared/model/enumerations/deposit-medium.model';
import { DepositService } from './deposit.service';
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
  selector: 'jhi-deposit',
  templateUrl: './deposit.component.html'
})
export class DepositComponent implements OnInit, OnDestroy {
  deposits?: IDeposit[];
  eventSubscriber?: Subscription;
  depositDateRange?: Date[];
  mediumOptions = DEPOSIT_MEDIUM_ARRAY.map((s: DepositMedium) => ({ label: s.toString(), value: s }));
  postDateRange?: Date[];
  createdOnRange?: Date[];
  modifiedOnRange?: Date[];

  totalItems?: number;
  itemsPerPage!: number;
  loading!: boolean;

  private filtersDetails: { [_: string]: { matchMode?: string; flatten?: (_: any) => string; unflatten?: (_: string) => any } } = {
    id: { matchMode: 'equals', unflatten: (x: string) => +x },
    depositNo: { matchMode: 'equals', unflatten: (x: string) => +x },
    depositDate: { matchMode: 'between', flatten: a => a.filter((x: string) => x).join(','), unflatten: (a: string) => a.split(',') },
    medium: { matchMode: 'in' },
    amount: { matchMode: 'equals', unflatten: (x: string) => +x },
    isPosted: { matchMode: 'equals', unflatten: (x: string) => x === 'true' },
    postDate: { matchMode: 'between', flatten: a => a.filter((x: string) => x).join(','), unflatten: (a: string) => a.split(',') },
    createdOn: { matchMode: 'between', flatten: a => a.filter((x: string) => x).join(','), unflatten: (a: string) => a.split(',') },
    modifiedOn: { matchMode: 'between', flatten: a => a.filter((x: string) => x).join(','), unflatten: (a: string) => a.split(',') }
  };

  @ViewChild('depositTable', { static: true })
  depositTable!: Table;

  constructor(
    protected depositService: DepositService,
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
    this.registerChangeInDeposits();
    this.activatedRoute.queryParams
      .pipe(
        tap(queryParams => fillTableFromQueryParams(this.depositTable, queryParams, this.filtersDetails)),
        tap(
          () =>
            (this.depositDateRange =
              this.depositTable.filters.depositDate &&
              this.depositTable.filters.depositDate.value &&
              this.depositTable.filters.depositDate.value.map((x: string) => new Date(x)))
        ),
        tap(
          () =>
            (this.postDateRange =
              this.depositTable.filters.postDate &&
              this.depositTable.filters.postDate.value &&
              this.depositTable.filters.postDate.value.map((x: string) => new Date(x)))
        ),
        tap(
          () =>
            (this.createdOnRange =
              this.depositTable.filters.createdOn &&
              this.depositTable.filters.createdOn.value &&
              this.depositTable.filters.createdOn.value.map((x: string) => new Date(x)))
        ),
        tap(
          () =>
            (this.modifiedOnRange =
              this.depositTable.filters.modifiedOn &&
              this.depositTable.filters.modifiedOn.value &&
              this.depositTable.filters.modifiedOn.value.map((x: string) => new Date(x)))
        ),
        tap(() => (this.loading = true)),
        switchMap(() => this.depositService.query(lazyLoadEventToServerQueryParams(this.depositTable.createLazyLoadMetadata()))),
        filter((res: HttpResponse<IDeposit[]>) => res.ok)
      )
      .subscribe(
        (res: HttpResponse<IDeposit[]>) => {
          this.paginateDeposits(res.body!, res.headers);
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
    this.router.navigate(['/deposit'], { queryParams });
  }

  filter(value: any, field: string): void {
    this.depositTable.filter(value, field, computeFilterMatchMode(this.filtersDetails[field]));
  }

  delete(id: number): void {
    this.confirmationService.confirm({
      header: this.translateService.instant('entity.delete.title'),
      message: this.translateService.instant('primengtestApp.deposit.delete.question', { id }),
      accept: () => {
        this.depositService.delete(id).subscribe(() => {
          this.eventManager.broadcast({
            name: 'depositListModification',
            content: 'Deleted an deposit'
          });
        });
      }
    });
  }

  trackId(index: number, item: IDeposit): string {
    return '' + item.id;
  }

  byteSize(field: string): string {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType: string, field: string): void {
    this.dataUtils.openFile(contentType, field);
  }

  registerChangeInDeposits(): void {
    this.eventSubscriber = this.eventManager.subscribe('depositListModification', () =>
      this.router.navigate(['/deposit'], { queryParams: { r: Date.now() } })
    );
  }

  protected paginateDeposits(data: IDeposit[], headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.deposits = data;
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
