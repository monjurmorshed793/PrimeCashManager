import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrimeCashManagerSharedModule } from 'app/shared/shared.module';
import { DepositComponent } from './deposit.component';
import { DepositDetailComponent } from './deposit-detail.component';
import { DepositUpdateComponent } from './deposit-update.component';
import { depositRoute } from './deposit.route';

@NgModule({
  imports: [PrimeCashManagerSharedModule, RouterModule.forChild(depositRoute)],
  declarations: [DepositComponent, DepositDetailComponent, DepositUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrimeCashManagerDepositModule {}
