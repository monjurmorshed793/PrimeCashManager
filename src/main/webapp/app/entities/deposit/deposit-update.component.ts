import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';
import { IDeposit } from 'app/shared/model/deposit.model';
import { DepositMedium, DEPOSIT_MEDIUM_ARRAY } from 'app/shared/model/enumerations/deposit-medium.model';
import { DepositService } from './deposit.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'jhi-deposit-update',
  templateUrl: './deposit-update.component.html'
})
export class DepositUpdateComponent implements OnInit {
  isSaving = false;
  mediumOptions = DEPOSIT_MEDIUM_ARRAY.map((s: DepositMedium) => ({ label: s.toString(), value: s }));

  editForm = this.fb.group({
    id: [],
    loginId: [null, [Validators.required]],
    depositNo: [null, []],
    depositBy: [null, [Validators.required]],
    depositDate: [null, [Validators.required]],
    medium: [null, [Validators.required]],
    amount: [null, [Validators.required]],
    note: [null, [Validators.required]],
    isPosted: [],
    postDate: [],
    createdBy: [],
    createdOn: [],
    modifiedBy: [],
    modifiedOn: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected messageService: MessageService,
    protected depositService: DepositService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ deposit }) => {
      this.updateForm(deposit);
    });
  }

  updateForm(deposit: IDeposit | null): void {
    if (deposit) {
      this.editForm.reset({ ...deposit });
    } else {
      this.editForm.reset({
        depositDate: new Date(),
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
    const deposit = this.editForm.value;
    if (deposit.id !== null) {
      this.subscribeToSaveResponse(this.depositService.update(deposit));
    } else {
      this.subscribeToSaveResponse(this.depositService.create(deposit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeposit>>): void {
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
