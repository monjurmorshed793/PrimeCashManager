import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExpenseDtl } from 'app/shared/model/expense-dtl.model';

type EntityResponseType = HttpResponse<IExpenseDtl>;
type EntityArrayResponseType = HttpResponse<IExpenseDtl[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseDtlService {
  public resourceUrl = SERVER_API_URL + 'api/expense-dtls';

  constructor(protected http: HttpClient) {}

  create(expenseDtl: IExpenseDtl): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseDtl);
    return this.http
      .post<IExpenseDtl>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(expenseDtl: IExpenseDtl): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseDtl);
    return this.http
      .put<IExpenseDtl>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExpenseDtl>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExpenseDtl[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(expenseDtl: IExpenseDtl): IExpenseDtl {
    const copy: IExpenseDtl = Object.assign({}, expenseDtl, {
      createdOn: expenseDtl.createdOn != null ? expenseDtl.createdOn.toISOString() : undefined,
      modifiedOn: expenseDtl.modifiedOn != null ? expenseDtl.modifiedOn.toISOString() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdOn = res.body.createdOn ? new Date(res.body.createdOn) : undefined;
      res.body.modifiedOn = res.body.modifiedOn ? new Date(res.body.modifiedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((expenseDtl: IExpenseDtl) => {
        expenseDtl.createdOn = expenseDtl.createdOn ? new Date(expenseDtl.createdOn) : undefined;
        expenseDtl.modifiedOn = expenseDtl.modifiedOn ? new Date(expenseDtl.modifiedOn) : undefined;
      });
    }
    return res;
  }
}
