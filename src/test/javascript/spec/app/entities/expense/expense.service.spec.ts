import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { DatePipe } from '@angular/common';
import { ExpenseService } from 'app/entities/expense/expense.service';
import { IExpense, Expense } from 'app/shared/model/expense.model';
import { MonthType } from 'app/shared/model/enumerations/month-type.model';

describe('Service Tests', () => {
  describe('Expense Service', () => {
    let injector: TestBed;
    let service: ExpenseService;
    let httpMock: HttpTestingController;
    let elemDefault: IExpense;
    let expectedResult: IExpense | IExpense[] | boolean | null;
    let currentDate: Date;
    let datePipe: DatePipe;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [DatePipe]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ExpenseService);
      httpMock = injector.get(HttpTestingController);
      currentDate = new Date();
      datePipe = injector.get(DatePipe);

      elemDefault = new Expense(
        123,
        'AAAAAAA',
        123,
        currentDate,
        MonthType.JANUARY,
        undefined,
        123,
        false,
        currentDate,
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        currentDate,
        undefined,
        'AAAAAAA',
        123
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            voucherDate: datePipe.transform(currentDate, DATE_FORMAT),
            postDate: currentDate.toISOString(),
            createdOn: currentDate.toISOString(),
            modifiedOn: currentDate.toISOString()
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Expense', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            voucherDate: datePipe.transform(currentDate, DATE_FORMAT),
            postDate: currentDate.toISOString(),
            createdOn: currentDate.toISOString(),
            modifiedOn: currentDate.toISOString()
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            voucherDate: currentDate,
            postDate: currentDate,
            createdOn: currentDate,
            modifiedOn: currentDate
          },
          returnedFromService
        );

        service.create(new Expense()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Expense', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            loginId: 'BBBBBB',
            voucherNo: 1,
            voucherDate: datePipe.transform(currentDate, DATE_FORMAT),
            month: 'BBBBBB',
            notes: 'BBBBBB',
            totalAmount: 1,
            isPosted: true,
            postDate: currentDate.toISOString(),
            createdBy: 'BBBBBB',
            createdOn: currentDate.toISOString(),
            modifiedBy: 'BBBBBB',
            modifiedOn: currentDate.toISOString()
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            voucherDate: currentDate,
            postDate: currentDate,
            createdOn: currentDate,
            modifiedOn: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Expense', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            loginId: 'BBBBBB',
            voucherNo: 1,
            voucherDate: datePipe.transform(currentDate, DATE_FORMAT),
            month: 'BBBBBB',
            notes: 'BBBBBB',
            totalAmount: 1,
            isPosted: true,
            postDate: currentDate.toISOString(),
            createdBy: 'BBBBBB',
            createdOn: currentDate.toISOString(),
            modifiedBy: 'BBBBBB',
            modifiedOn: currentDate.toISOString()
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            voucherDate: currentDate,
            postDate: currentDate,
            createdOn: currentDate,
            modifiedOn: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Expense', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
