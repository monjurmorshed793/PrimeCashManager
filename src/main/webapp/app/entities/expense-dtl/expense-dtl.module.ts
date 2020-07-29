import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrimeCashManagerSharedModule } from 'app/shared/shared.module';
import { ExpenseDtlComponent } from './expense-dtl.component';
import { ExpenseDtlDetailComponent } from './expense-dtl-detail.component';
import { ExpenseDtlUpdateComponent } from './expense-dtl-update.component';
import { expenseDtlRoute } from './expense-dtl.route';

@NgModule({
  imports: [PrimeCashManagerSharedModule, RouterModule.forChild(expenseDtlRoute)],
  declarations: [ExpenseDtlComponent, ExpenseDtlDetailComponent, ExpenseDtlUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrimeCashManagerExpenseDtlModule {}
