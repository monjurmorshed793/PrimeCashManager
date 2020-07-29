import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDeposit } from 'app/shared/model/deposit.model';
import { DepositService } from './deposit.service';
import { DepositComponent } from './deposit.component';
import { DepositDetailComponent } from './deposit-detail.component';
import { DepositUpdateComponent } from './deposit-update.component';

@Injectable({ providedIn: 'root' })
export class DepositResolve implements Resolve<IDeposit | null> {
  constructor(private service: DepositService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeposit | null> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        flatMap((deposit: HttpResponse<IDeposit>) => {
          if (deposit.body) {
            return of(deposit.body);
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

export const depositRoute: Routes = [
  {
    path: '',
    component: DepositComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'primeCashManagerApp.deposit.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DepositDetailComponent,
    resolve: {
      deposit: DepositResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'primeCashManagerApp.deposit.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DepositUpdateComponent,
    resolve: {
      deposit: DepositResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'primeCashManagerApp.deposit.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DepositUpdateComponent,
    resolve: {
      deposit: DepositResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'primeCashManagerApp.deposit.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
