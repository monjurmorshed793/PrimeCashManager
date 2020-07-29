export interface IExpenseDtl {
  id?: number;
  quantity?: number;
  unitPrice?: number;
  amount?: number;
  createdBy?: string;
  createdOn?: Date;
  modifiedBy?: string;
  modifiedOn?: Date;
  itemName?: string;
  itemId?: number;
  expenseVoucherNo?: string;
  expenseId?: number;
}

export class ExpenseDtl implements IExpenseDtl {
  constructor(
    public id?: number,
    public quantity?: number,
    public unitPrice?: number,
    public amount?: number,
    public createdBy?: string,
    public createdOn?: Date,
    public modifiedBy?: string,
    public modifiedOn?: Date,
    public itemName?: string,
    public itemId?: number,
    public expenseVoucherNo?: string,
    public expenseId?: number
  ) {}
}
