import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'item',
        loadChildren: () => import('./item/item.module').then(m => m.PrimeCashManagerItemModule)
      },
      {
        path: 'pay-to',
        loadChildren: () => import('./pay-to/pay-to.module').then(m => m.PrimeCashManagerPayToModule)
      },
      {
        path: 'deposit',
        loadChildren: () => import('./deposit/deposit.module').then(m => m.PrimeCashManagerDepositModule)
      },
      {
        path: 'expense',
        loadChildren: () => import('./expense/expense.module').then(m => m.PrimeCashManagerExpenseModule)
      },
      {
        path: 'expense-dtl',
        loadChildren: () => import('./expense-dtl/expense-dtl.module').then(m => m.PrimeCashManagerExpenseDtlModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PrimeCashManagerEntityModule {}
