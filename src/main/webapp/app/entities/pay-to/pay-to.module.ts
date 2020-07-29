import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrimeCashManagerSharedModule } from 'app/shared/shared.module';
import { PayToComponent } from './pay-to.component';
import { PayToDetailComponent } from './pay-to-detail.component';
import { PayToUpdateComponent } from './pay-to-update.component';
import { payToRoute } from './pay-to.route';

@NgModule({
  imports: [PrimeCashManagerSharedModule, RouterModule.forChild(payToRoute)],
  declarations: [PayToComponent, PayToDetailComponent, PayToUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrimeCashManagerPayToModule {}
