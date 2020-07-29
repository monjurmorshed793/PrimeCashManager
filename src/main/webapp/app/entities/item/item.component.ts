import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, tap, switchMap } from 'rxjs/operators';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { MessageService } from 'primeng/api';
import { IItem } from 'app/shared/model/item.model';
import { ItemService } from './item.service';
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
  selector: 'jhi-item',
  templateUrl: './item.component.html'
})
export class ItemComponent implements OnInit, OnDestroy {
  items?: IItem[];
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

  @ViewChild('itemTable', { static: true })
  itemTable!: Table;

  constructor(
    protected itemService: ItemService,
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
    this.registerChangeInItems();
    this.activatedRoute.queryParams
      .pipe(
        tap(queryParams => fillTableFromQueryParams(this.itemTable, queryParams, this.filtersDetails)),
        tap(
          () =>
            (this.createdOnRange =
              this.itemTable.filters.createdOn &&
              this.itemTable.filters.createdOn.value &&
              this.itemTable.filters.createdOn.value.map((x: string) => new Date(x)))
        ),
        tap(
          () =>
            (this.modifiedOnRange =
              this.itemTable.filters.modifiedOn &&
              this.itemTable.filters.modifiedOn.value &&
              this.itemTable.filters.modifiedOn.value.map((x: string) => new Date(x)))
        ),
        tap(() => (this.loading = true)),
        switchMap(() => this.itemService.query(lazyLoadEventToServerQueryParams(this.itemTable.createLazyLoadMetadata()))),
        filter((res: HttpResponse<IItem[]>) => res.ok)
      )
      .subscribe(
        (res: HttpResponse<IItem[]>) => {
          this.paginateItems(res.body!, res.headers);
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
    this.router.navigate(['/item'], { queryParams });
  }

  filter(value: any, field: string): void {
    this.itemTable.filter(value, field, computeFilterMatchMode(this.filtersDetails[field]));
  }

  delete(id: number): void {
    this.confirmationService.confirm({
      header: this.translateService.instant('entity.delete.title'),
      message: this.translateService.instant('primengtestApp.item.delete.question', { id }),
      accept: () => {
        this.itemService.delete(id).subscribe(() => {
          this.eventManager.broadcast({
            name: 'itemListModification',
            content: 'Deleted an item'
          });
        });
      }
    });
  }

  trackId(index: number, item: IItem): string {
    return '' + item.id;
  }

  byteSize(field: string): string {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType: string, field: string): void {
    this.dataUtils.openFile(contentType, field);
  }

  registerChangeInItems(): void {
    this.eventSubscriber = this.eventManager.subscribe('itemListModification', () =>
      this.router.navigate(['/item'], { queryParams: { r: Date.now() } })
    );
  }

  protected paginateItems(data: IItem[], headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.items = data;
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
