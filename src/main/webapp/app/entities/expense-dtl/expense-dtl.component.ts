import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, tap, switchMap } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';
import { MessageService } from 'primeng/api';
import { IExpenseDtl } from 'app/shared/model/expense-dtl.model';
import { ExpenseDtlService } from './expense-dtl.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {
  computeFilterMatchMode,
  lazyLoadEventToServerQueryParams,
  lazyLoadEventToRouterQueryParams,
  fillTableFromQueryParams
} from 'app/shared/util/request-util';
import { ConfirmationService, LazyLoadEvent } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';
import { IItem } from 'app/shared/model/item.model';
import { ItemService } from 'app/entities/item/item.service';
import { IExpense } from 'app/shared/model/expense.model';
import { ExpenseService } from 'app/entities/expense/expense.service';
import { Table } from 'primeng/table';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'jhi-expense-dtl',
  templateUrl: './expense-dtl.component.html'
})
export class ExpenseDtlComponent implements OnInit, OnDestroy {
  expenseDtls?: IExpenseDtl[];
  eventSubscriber?: Subscription;
  createdOnRange?: Date[];
  modifiedOnRange?: Date[];
  itemOptions: IItem[] | null = null;
  expenseOptions: IExpense[] | null = null;

  totalItems?: number;
  itemsPerPage!: number;
  loading!: boolean;

  private filtersDetails: { [_: string]: { matchMode?: string; flatten?: (_: any) => string; unflatten?: (_: string) => any } } = {
    id: { matchMode: 'equals', unflatten: (x: string) => +x },
    quantity: { matchMode: 'equals', unflatten: (x: string) => +x },
    unitPrice: { matchMode: 'equals', unflatten: (x: string) => +x },
    amount: { matchMode: 'equals', unflatten: (x: string) => +x },
    createdOn: { matchMode: 'between', flatten: a => a.filter((x: string) => x).join(','), unflatten: (a: string) => a.split(',') },
    modifiedOn: { matchMode: 'between', flatten: a => a.filter((x: string) => x).join(','), unflatten: (a: string) => a.split(',') },
    itemId: { matchMode: 'in', flatten: a => a.join(','), unflatten: a => a.split(',').map(x => +x) },
    expenseId: { matchMode: 'in', flatten: a => a.join(','), unflatten: a => a.split(',').map(x => +x) }
  };

  @ViewChild('expenseDtlTable', { static: true })
  expenseDtlTable!: Table;

  constructor(
    protected expenseDtlService: ExpenseDtlService,
    protected itemService: ItemService,
    protected expenseService: ExpenseService,
    protected messageService: MessageService,
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
    this.registerChangeInExpenseDtls();
    this.activatedRoute.queryParams
      .pipe(
        tap(queryParams => fillTableFromQueryParams(this.expenseDtlTable, queryParams, this.filtersDetails)),
        tap(
          () =>
            (this.createdOnRange =
              this.expenseDtlTable.filters.createdOn &&
              this.expenseDtlTable.filters.createdOn.value &&
              this.expenseDtlTable.filters.createdOn.value.map((x: string) => new Date(x)))
        ),
        tap(
          () =>
            (this.modifiedOnRange =
              this.expenseDtlTable.filters.modifiedOn &&
              this.expenseDtlTable.filters.modifiedOn.value &&
              this.expenseDtlTable.filters.modifiedOn.value.map((x: string) => new Date(x)))
        ),
        tap(() => (this.loading = true)),
        switchMap(() => this.expenseDtlService.query(lazyLoadEventToServerQueryParams(this.expenseDtlTable.createLazyLoadMetadata()))),
        filter((res: HttpResponse<IExpenseDtl[]>) => res.ok)
      )
      .subscribe(
        (res: HttpResponse<IExpenseDtl[]>) => {
          this.paginateExpenseDtls(res.body!, res.headers);
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
    this.router.navigate(['/expense-dtl'], { queryParams });
  }

  filter(value: any, field: string): void {
    this.expenseDtlTable.filter(value, field, computeFilterMatchMode(this.filtersDetails[field]));
  }

  delete(id: number): void {
    this.confirmationService.confirm({
      header: this.translateService.instant('entity.delete.title'),
      message: this.translateService.instant('primengtestApp.expenseDtl.delete.question', { id }),
      accept: () => {
        this.expenseDtlService.delete(id).subscribe(() => {
          this.eventManager.broadcast({
            name: 'expenseDtlListModification',
            content: 'Deleted an expenseDtl'
          });
        });
      }
    });
  }

  onItemLazyLoadEvent(event: LazyLoadEvent): void {
    this.itemService.query(lazyLoadEventToServerQueryParams(event, 'name.contains')).subscribe(res => (this.itemOptions = res.body));
  }

  onExpenseLazyLoadEvent(event: LazyLoadEvent): void {
    this.expenseService
      .query(lazyLoadEventToServerQueryParams(event, 'voucherNo.contains'))
      .subscribe(res => (this.expenseOptions = res.body));
  }

  trackId(index: number, item: IExpenseDtl): string {
    return '' + item.id;
  }

  registerChangeInExpenseDtls(): void {
    this.eventSubscriber = this.eventManager.subscribe('expenseDtlListModification', () =>
      this.router.navigate(['/expense-dtl'], { queryParams: { r: Date.now() } })
    );
  }

  protected paginateExpenseDtls(data: IExpenseDtl[], headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.expenseDtls = data;
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
