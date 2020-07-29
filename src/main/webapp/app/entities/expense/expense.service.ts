import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DatePipe } from '@angular/common';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExpense } from 'app/shared/model/expense.model';

type EntityResponseType = HttpResponse<IExpense>;
type EntityArrayResponseType = HttpResponse<IExpense[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseService {
  public resourceUrl = SERVER_API_URL + 'api/expenses';

  constructor(protected http: HttpClient, protected datePipe: DatePipe) {}

  create(expense: IExpense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expense);
    return this.http
      .post<IExpense>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(expense: IExpense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expense);
    return this.http
      .put<IExpense>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExpense>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExpense[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(expense: IExpense): IExpense {
    const copy: IExpense = Object.assign({}, expense, {
      voucherDate: expense.voucherDate != null ? this.datePipe.transform(expense.voucherDate, DATE_FORMAT) : undefined,
      postDate: expense.postDate != null ? expense.postDate.toISOString() : undefined,
      createdOn: expense.createdOn != null ? expense.createdOn.toISOString() : undefined,
      modifiedOn: expense.modifiedOn != null ? expense.modifiedOn.toISOString() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.voucherDate = res.body.voucherDate ? new Date(res.body.voucherDate) : undefined;
      res.body.postDate = res.body.postDate ? new Date(res.body.postDate) : undefined;
      res.body.createdOn = res.body.createdOn ? new Date(res.body.createdOn) : undefined;
      res.body.modifiedOn = res.body.modifiedOn ? new Date(res.body.modifiedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((expense: IExpense) => {
        expense.voucherDate = expense.voucherDate ? new Date(expense.voucherDate) : undefined;
        expense.postDate = expense.postDate ? new Date(expense.postDate) : undefined;
        expense.createdOn = expense.createdOn ? new Date(expense.createdOn) : undefined;
        expense.modifiedOn = expense.modifiedOn ? new Date(expense.modifiedOn) : undefined;
      });
    }
    return res;
  }
}
