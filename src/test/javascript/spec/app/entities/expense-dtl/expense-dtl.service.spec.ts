import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ExpenseDtlService } from 'app/entities/expense-dtl/expense-dtl.service';
import { IExpenseDtl, ExpenseDtl } from 'app/shared/model/expense-dtl.model';

describe('Service Tests', () => {
  describe('ExpenseDtl Service', () => {
    let injector: TestBed;
    let service: ExpenseDtlService;
    let httpMock: HttpTestingController;
    let elemDefault: IExpenseDtl;
    let expectedResult: IExpenseDtl | IExpenseDtl[] | boolean | null;
    let currentDate: Date;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ExpenseDtlService);
      httpMock = injector.get(HttpTestingController);
      currentDate = new Date();

      elemDefault = new ExpenseDtl(123, 123, 123, 123, 'AAAAAAA', currentDate, 'AAAAAAA', currentDate, 'AAAAAAA', 123, 'AAAAAAA', 123);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
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

      it('should create a ExpenseDtl', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdOn: currentDate.toISOString(),
            modifiedOn: currentDate.toISOString()
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdOn: currentDate,
            modifiedOn: currentDate
          },
          returnedFromService
        );

        service.create(new ExpenseDtl()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ExpenseDtl', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantity: 1,
            unitPrice: 1,
            amount: 1,
            createdBy: 'BBBBBB',
            createdOn: currentDate.toISOString(),
            modifiedBy: 'BBBBBB',
            modifiedOn: currentDate.toISOString()
          },
          elemDefault
        );

        const expected = Object.assign(
          {
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

      it('should return a list of ExpenseDtl', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantity: 1,
            unitPrice: 1,
            amount: 1,
            createdBy: 'BBBBBB',
            createdOn: currentDate.toISOString(),
            modifiedBy: 'BBBBBB',
            modifiedOn: currentDate.toISOString()
          },
          elemDefault
        );

        const expected = Object.assign(
          {
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

      it('should delete a ExpenseDtl', () => {
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
