import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IExpenseDtl } from 'app/shared/model/expense-dtl.model';
import { ExpenseDtlService } from './expense-dtl.service';
import { ExpenseDtlComponent } from './expense-dtl.component';
import { ExpenseDtlDetailComponent } from './expense-dtl-detail.component';
import { ExpenseDtlUpdateComponent } from './expense-dtl-update.component';

@Injectable({ providedIn: 'root' })
export class ExpenseDtlResolve implements Resolve<IExpenseDtl | null> {
  constructor(private service: ExpenseDtlService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExpenseDtl | null> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        flatMap((expenseDtl: HttpResponse<IExpenseDtl>) => {
          if (expenseDtl.body) {
            return of(expenseDtl.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}

export const expenseDtlRoute: Routes = [
  {
    path: '',
    component: ExpenseDtlComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'primeCashManagerApp.expenseDtl.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ExpenseDtlDetailComponent,
    resolve: {
      expenseDtl: ExpenseDtlResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'primeCashManagerApp.expenseDtl.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ExpenseDtlUpdateComponent,
    resolve: {
      expenseDtl: ExpenseDtlResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'primeCashManagerApp.expenseDtl.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ExpenseDtlUpdateComponent,
    resolve: {
      expenseDtl: ExpenseDtlResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'primeCashManagerApp.expenseDtl.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
