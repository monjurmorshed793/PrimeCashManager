import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPayTo } from 'app/shared/model/pay-to.model';
import { PayToService } from './pay-to.service';
import { PayToComponent } from './pay-to.component';
import { PayToDetailComponent } from './pay-to-detail.component';
import { PayToUpdateComponent } from './pay-to-update.component';

@Injectable({ providedIn: 'root' })
export class PayToResolve implements Resolve<IPayTo | null> {
  constructor(private service: PayToService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPayTo | null> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        flatMap((payTo: HttpResponse<IPayTo>) => {
          if (payTo.body) {
            return of(payTo.body);
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

export const payToRoute: Routes = [
  {
    path: '',
    component: PayToComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'primeCashManagerApp.payTo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PayToDetailComponent,
    resolve: {
      payTo: PayToResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'primeCashManagerApp.payTo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PayToUpdateComponent,
    resolve: {
      payTo: PayToResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'primeCashManagerApp.payTo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PayToUpdateComponent,
    resolve: {
      payTo: PayToResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'primeCashManagerApp.payTo.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
