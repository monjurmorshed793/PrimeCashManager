import { IExpenseDtl } from 'app/shared/model/expense-dtl.model';
import { MonthType } from 'app/shared/model/enumerations/month-type.model';

export interface IExpense {
  id?: number;
  loginId?: string;
  voucherNo?: number;
  voucherDate?: Date;
  month?: MonthType;
  notes?: any;
  totalAmount?: number;
  isPosted?: boolean;
  postDate?: Date;
  createdBy?: string;
  createdOn?: Date;
  modifiedBy?: string;
  modifiedOn?: Date;
  expanseDtls?: IExpenseDtl[];
  payToName?: string;
  payToId?: number;
}

export class Expense implements IExpense {
  constructor(
    public id?: number,
    public loginId?: string,
    public voucherNo?: number,
    public voucherDate?: Date,
    public month?: MonthType,
    public notes?: any,
    public totalAmount?: number,
    public isPosted?: boolean,
    public postDate?: Date,
    public createdBy?: string,
    public createdOn?: Date,
    public modifiedBy?: string,
    public modifiedOn?: Date,
    public expanseDtls?: IExpenseDtl[],
    public payToName?: string,
    public payToId?: number
  ) {}
}
