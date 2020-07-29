import { DepositMedium } from 'app/shared/model/enumerations/deposit-medium.model';

export interface IDeposit {
  id?: number;
  loginId?: string;
  depositNo?: number;
  depositBy?: string;
  depositDate?: Date;
  medium?: DepositMedium;
  amount?: number;
  note?: any;
  isPosted?: boolean;
  postDate?: Date;
  createdBy?: string;
  createdOn?: Date;
  modifiedBy?: string;
  modifiedOn?: Date;
}

export class Deposit implements IDeposit {
  constructor(
    public id?: number,
    public loginId?: string,
    public depositNo?: number,
    public depositBy?: string,
    public depositDate?: Date,
    public medium?: DepositMedium,
    public amount?: number,
    public note?: any,
    public isPosted?: boolean,
    public postDate?: Date,
    public createdBy?: string,
    public createdOn?: Date,
    public modifiedBy?: string,
    public modifiedOn?: Date
  ) {}
}
