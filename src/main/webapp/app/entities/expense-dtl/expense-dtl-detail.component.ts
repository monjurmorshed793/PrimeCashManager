import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExpenseDtl } from 'app/shared/model/expense-dtl.model';

@Component({
  selector: 'jhi-expense-dtl-detail',
  templateUrl: './expense-dtl-detail.component.html'
})
export class ExpenseDtlDetailComponent implements OnInit {
  expenseDtl: IExpenseDtl | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseDtl }) => (this.expenseDtl = expenseDtl));
  }

  previousState(): void {
    window.history.back();
  }
}
