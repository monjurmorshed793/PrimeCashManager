export interface IPayTo {
  id?: number;
  name?: string;
  description?: any;
  createdBy?: string;
  createdOn?: Date;
  modifiedBy?: string;
  modifiedOn?: Date;
}

export class PayTo implements IPayTo {
  constructor(
    public id?: number,
    public name?: string,
    public description?: any,
    public createdBy?: string,
    public createdOn?: Date,
    public modifiedBy?: string,
    public modifiedOn?: Date
  ) {}
}
