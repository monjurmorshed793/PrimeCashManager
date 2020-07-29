import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { lazyLoadEventToServerQueryParams } from 'app/shared/util/request-util';
import { LazyLoadEvent } from 'primeng/api';
import { JhiDataUtils } from 'ng-jhipster';
import { IExpense } from 'app/shared/model/expense.model';
import { MonthType, MONTH_TYPE_ARRAY } from 'app/shared/model/enumerations/month-type.model';
import { ExpenseService } from './expense.service';
import { MessageService } from 'primeng/api';
import { IPayTo } from 'app/shared/model/pay-to.model';
import { PayToService } from 'app/entities/pay-to/pay-to.service';

@Component({
  selector: 'jhi-expense-update',
  templateUrl: './expense-update.component.html'
})
export class ExpenseUpdateComponent implements OnInit {
  isSaving = false;
  payToOptions: IPayTo[] | null = null;
  payToFilterValue?: any;
  monthOptions = MONTH_TYPE_ARRAY.map((s: MonthType) => ({ label: s.toString(), value: s }));

  editForm = this.fb.group({
    id: [],
    loginId: [null, [Validators.required]],
    voucherNo: [null, []],
    voucherDate: [null, [Validators.required]],
    month: [null, [Validators.required]],
    notes: [null, [Validators.required]],
    totalAmount: [],
    isPosted: [],
    postDate: [],
    createdBy: [],
    createdOn: [],
    modifiedBy: [],
    modifiedOn: [],
    payToId: [null, Validators.required]
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected messageService: MessageService,
    protected expenseService: ExpenseService,
    protected payToService: PayToService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ expense }) => {
      this.updateForm(expense);
    });
  }

  onPayToLazyLoadEvent(event: LazyLoadEvent): void {
    this.payToService.query(lazyLoadEventToServerQueryParams(event, 'name.contains')).subscribe(
      (res: HttpResponse<IPayTo[]>) => (this.payToOptions = res.body),
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(expense: IExpense | null): void {
    if (expense) {
      this.editForm.reset({ ...expense });
      this.payToFilterValue = expense.payToId;
    } else {
      this.editForm.reset({
        voucherDate: new Date(),
        isPosted: false,
        postDate: new Date(),
        createdOn: new Date(),
        modifiedOn: new Date()
      });
    }
  }

  onFileSelect(event: { event: Event; files: File[] }, field: string): void {
    const file = event.files[0];
    this.dataUtils.toBase64(file, (base64Data: string) => {
      this.editForm.patchValue({
        [field]: base64Data,
        [field + 'ContentType']: file.type
      });
    });
  }

  onFileRemove(event: { event: Event; files: File[] }, field: string): void {
    this.editForm.patchValue({
      [field]: null,
      [field + 'ContentType']: null
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expense = this.editForm.value;
    if (expense.id !== null) {
      this.subscribeToSaveResponse(this.expenseService.update(expense));
    } else {
      this.subscribeToSaveResponse(this.expenseService.create(expense));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpense>>): void {
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
