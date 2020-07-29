import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, tap, switchMap } from 'rxjs/operators';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { MessageService } from 'primeng/api';
import { IExpense } from 'app/shared/model/expense.model';
import { MonthType, MONTH_TYPE_ARRAY } from 'app/shared/model/enumerations/month-type.model';
import { ExpenseService } from './expense.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {
  computeFilterMatchMode,
  lazyLoadEventToServerQueryParams,
  lazyLoadEventToRouterQueryParams,
  fillTableFromQueryParams
} from 'app/shared/util/request-util';
import { ConfirmationService, LazyLoadEvent } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';
import { IPayTo } from 'app/shared/model/pay-to.model';
import { PayToService } from 'app/entities/pay-to/pay-to.service';
import { Table } from 'primeng/table';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'jhi-expense',
  templateUrl: './expense.component.html'
})
export class ExpenseComponent implements OnInit, OnDestroy {
  expenses?: IExpense[];
  eventSubscriber?: Subscription;
  voucherDateRange?: Date[];
  monthOptions = MONTH_TYPE_ARRAY.map((s: MonthType) => ({ label: s.toString(), value: s }));
  postDateRange?: Date[];
  createdOnRange?: Date[];
  modifiedOnRange?: Date[];
  payToOptions: IPayTo[] | null = null;

  totalItems?: number;
  itemsPerPage!: number;
  loading!: boolean;

  private filtersDetails: { [_: string]: { matchMode?: string; flatten?: (_: any) => string; unflatten?: (_: string) => any } } = {
    id: { matchMode: 'equals', unflatten: (x: string) => +x },
    voucherNo: { matchMode: 'equals', unflatten: (x: string) => +x },
    voucherDate: { matchMode: 'between', flatten: a => a.filter((x: string) => x).join(','), unflatten: (a: string) => a.split(',') },
    month: { matchMode: 'in' },
    totalAmount: { matchMode: 'equals', unflatten: (x: string) => +x },
    isPosted: { matchMode: 'equals', unflatten: (x: string) => x === 'true' },
    postDate: { matchMode: 'between', flatten: a => a.filter((x: string) => x).join(','), unflatten: (a: string) => a.split(',') },
    createdOn: { matchMode: 'between', flatten: a => a.filter((x: string) => x).join(','), unflatten: (a: string) => a.split(',') },
    modifiedOn: { matchMode: 'between', flatten: a => a.filter((x: string) => x).join(','), unflatten: (a: string) => a.split(',') },
    payToId: { matchMode: 'in', flatten: a => a.join(','), unflatten: a => a.split(',').map(x => +x) }
  };

  @ViewChild('expenseTable', { static: true })
  expenseTable!: Table;

  constructor(
    protected expenseService: ExpenseService,
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
    this.registerChangeInExpenses();
    this.activatedRoute.queryParams
      .pipe(
        tap(queryParams => fillTableFromQueryParams(this.expenseTable, queryParams, this.filtersDetails)),
        tap(
          () =>
            (this.voucherDateRange =
              this.expenseTable.filters.voucherDate &&
              this.expenseTable.filters.voucherDate.value &&
              this.expenseTable.filters.voucherDate.value.map((x: string) => new Date(x)))
        ),
        tap(
          () =>
            (this.postDateRange =
              this.expenseTable.filters.postDate &&
              this.expenseTable.filters.postDate.value &&
              this.expenseTable.filters.postDate.value.map((x: string) => new Date(x)))
        ),
        tap(
          () =>
            (this.createdOnRange =
              this.expenseTable.filters.createdOn &&
              this.expenseTable.filters.createdOn.value &&
              this.expenseTable.filters.createdOn.value.map((x: string) => new Date(x)))
        ),
        tap(
          () =>
            (this.modifiedOnRange =
              this.expenseTable.filters.modifiedOn &&
              this.expenseTable.filters.modifiedOn.value &&
              this.expenseTable.filters.modifiedOn.value.map((x: string) => new Date(x)))
        ),
        tap(() => (this.loading = true)),
        switchMap(() => this.expenseService.query(lazyLoadEventToServerQueryParams(this.expenseTable.createLazyLoadMetadata()))),
        filter((res: HttpResponse<IExpense[]>) => res.ok)
      )
      .subscribe(
        (res: HttpResponse<IExpense[]>) => {
          this.paginateExpenses(res.body!, res.headers);
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
    this.router.navigate(['/expense'], { queryParams });
  }

  filter(value: any, field: string): void {
    this.expenseTable.filter(value, field, computeFilterMatchMode(this.filtersDetails[field]));
  }

  delete(id: number): void {
    this.confirmationService.confirm({
      header: this.translateService.instant('entity.delete.title'),
      message: this.translateService.instant('primengtestApp.expense.delete.question', { id }),
      accept: () => {
        this.expenseService.delete(id).subscribe(() => {
          this.eventManager.broadcast({
            name: 'expenseListModification',
            content: 'Deleted an expense'
          });
        });
      }
    });
  }

  onPayToLazyLoadEvent(event: LazyLoadEvent): void {
    this.payToService.query(lazyLoadEventToServerQueryParams(event, 'name.contains')).subscribe(res => (this.payToOptions = res.body));
  }

  trackId(index: number, item: IExpense): string {
    return '' + item.id;
  }

  byteSize(field: string): string {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType: string, field: string): void {
    this.dataUtils.openFile(contentType, field);
  }

  registerChangeInExpenses(): void {
    this.eventSubscriber = this.eventManager.subscribe('expenseListModification', () =>
      this.router.navigate(['/expense'], { queryParams: { r: Date.now() } })
    );
  }

  protected paginateExpenses(data: IExpense[], headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.expenses = data;
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
