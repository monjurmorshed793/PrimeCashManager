import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { lazyLoadEventToServerQueryParams } from 'app/shared/util/request-util';
import { LazyLoadEvent } from 'primeng/api';
import { IExpenseDtl } from 'app/shared/model/expense-dtl.model';
import { ExpenseDtlService } from './expense-dtl.service';
import { MessageService } from 'primeng/api';
import { IItem } from 'app/shared/model/item.model';
import { ItemService } from 'app/entities/item/item.service';
import { IExpense } from 'app/shared/model/expense.model';
import { ExpenseService } from 'app/entities/expense/expense.service';

@Component({
  selector: 'jhi-expense-dtl-update',
  templateUrl: './expense-dtl-update.component.html'
})
export class ExpenseDtlUpdateComponent implements OnInit {
  isSaving = false;
  itemOptions: IItem[] | null = null;
  itemFilterValue?: any;
  expenseOptions: IExpense[] | null = null;
  expenseFilterValue?: any;

  editForm = this.fb.group({
    id: [],
    quantity: [],
    unitPrice: [],
    amount: [],
    createdBy: [],
    createdOn: [],
    modifiedBy: [],
    modifiedOn: [],
    itemId: [null, Validators.required],
    expenseId: []
  });

  constructor(
    protected messageService: MessageService,
    protected expenseDtlService: ExpenseDtlService,
    protected itemService: ItemService,
    protected expenseService: ExpenseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ expenseDtl }) => {
      this.updateForm(expenseDtl);
    });
  }

  onItemLazyLoadEvent(event: LazyLoadEvent): void {
    this.itemService.query(lazyLoadEventToServerQueryParams(event, 'name.contains')).subscribe(
      (res: HttpResponse<IItem[]>) => (this.itemOptions = res.body),
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  onExpenseLazyLoadEvent(event: LazyLoadEvent): void {
    this.expenseService.query(lazyLoadEventToServerQueryParams(event, 'voucherNo.contains')).subscribe(
      (res: HttpResponse<IExpense[]>) => (this.expenseOptions = res.body),
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(expenseDtl: IExpenseDtl | null): void {
    if (expenseDtl) {
      this.editForm.reset({ ...expenseDtl });
      this.itemFilterValue = expenseDtl.itemId;
      this.expenseFilterValue = expenseDtl.expenseId;
    } else {
      this.editForm.reset({
        createdOn: new Date(),
        modifiedOn: new Date()
      });
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expenseDtl = this.editForm.value;
    if (expenseDtl.id !== null) {
      this.subscribeToSaveResponse(this.expenseDtlService.update(expenseDtl));
    } else {
      this.subscribeToSaveResponse(this.expenseDtlService.create(expenseDtl));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpenseDtl>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
  protected onError(errorMessage: string): void {
    this.messageService.add({ severity: 'error', summary: errorMessage });
  }
}
