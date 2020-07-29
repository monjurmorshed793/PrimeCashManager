import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DatePipe } from '@angular/common';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDeposit } from 'app/shared/model/deposit.model';

type EntityResponseType = HttpResponse<IDeposit>;
type EntityArrayResponseType = HttpResponse<IDeposit[]>;

@Injectable({ providedIn: 'root' })
export class DepositService {
  public resourceUrl = SERVER_API_URL + 'api/deposits';

  constructor(protected http: HttpClient, protected datePipe: DatePipe) {}

  create(deposit: IDeposit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deposit);
    return this.http
      .post<IDeposit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(deposit: IDeposit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deposit);
    return this.http
      .put<IDeposit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDeposit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDeposit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(deposit: IDeposit): IDeposit {
    const copy: IDeposit = Object.assign({}, deposit, {
      depositDate: deposit.depositDate != null ? this.datePipe.transform(deposit.depositDate, DATE_FORMAT) : undefined,
      postDate: deposit.postDate != null ? deposit.postDate.toISOString() : undefined,
      createdOn: deposit.createdOn != null ? deposit.createdOn.toISOString() : undefined,
      modifiedOn: deposit.modifiedOn != null ? deposit.modifiedOn.toISOString() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.depositDate = res.body.depositDate ? new Date(res.body.depositDate) : undefined;
      res.body.postDate = res.body.postDate ? new Date(res.body.postDate) : undefined;
      res.body.createdOn = res.body.createdOn ? new Date(res.body.createdOn) : undefined;
      res.body.modifiedOn = res.body.modifiedOn ? new Date(res.body.modifiedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((deposit: IDeposit) => {
        deposit.depositDate = deposit.depositDate ? new Date(deposit.depositDate) : undefined;
        deposit.postDate = deposit.postDate ? new Date(deposit.postDate) : undefined;
        deposit.createdOn = deposit.createdOn ? new Date(deposit.createdOn) : undefined;
        deposit.modifiedOn = deposit.modifiedOn ? new Date(deposit.modifiedOn) : undefined;
      });
    }
    return res;
  }
}
