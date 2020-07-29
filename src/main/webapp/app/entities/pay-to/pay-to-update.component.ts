import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';
import { IPayTo } from 'app/shared/model/pay-to.model';
import { PayToService } from './pay-to.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'jhi-pay-to-update',
  templateUrl: './pay-to-update.component.html'
})
export class PayToUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    createdBy: [],
    createdOn: [],
    modifiedBy: [],
    modifiedOn: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected messageService: MessageService,
    protected payToService: PayToService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ payTo }) => {
      this.updateForm(payTo);
    });
  }

  updateForm(payTo: IPayTo | null): void {
    if (payTo) {
      this.editForm.reset({ ...payTo });
    } else {
      this.editForm.reset({
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
    const payTo = this.editForm.value;
    if (payTo.id !== null) {
      this.subscribeToSaveResponse(this.payToService.update(payTo));
    } else {
      this.subscribeToSaveResponse(this.payToService.create(payTo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayTo>>): void {
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
