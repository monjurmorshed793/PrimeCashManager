import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { DatePipe } from '@angular/common';
import { DepositService } from 'app/entities/deposit/deposit.service';
import { IDeposit, Deposit } from 'app/shared/model/deposit.model';
import { DepositMedium } from 'app/shared/model/enumerations/deposit-medium.model';

describe('Service Tests', () => {
  describe('Deposit Service', () => {
    let injector: TestBed;
    let service: DepositService;
    let httpMock: HttpTestingController;
    let elemDefault: IDeposit;
    let expectedResult: IDeposit | IDeposit[] | boolean | null;
    let currentDate: Date;
    let datePipe: DatePipe;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [DatePipe]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(DepositService);
      httpMock = injector.get(HttpTestingController);
      currentDate = new Date();
      datePipe = injector.get(DatePipe);

      elemDefault = new Deposit(
        123,
        'AAAAAAA',
        123,
        'AAAAAAA',
        currentDate,
        DepositMedium.ATM,
        123,
        undefined,
        false,
        currentDate,
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        currentDate
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            depositDate: datePipe.transform(currentDate, DATE_FORMAT),
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

      it('should create a Deposit', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            depositDate: datePipe.transform(currentDate, DATE_FORMAT),
            postDate: currentDate.toISOString(),
            createdOn: currentDate.toISOString(),
            modifiedOn: currentDate.toISOString()
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            depositDate: currentDate,
            postDate: currentDate,
            createdOn: currentDate,
            modifiedOn: currentDate
          },
          returnedFromService
        );

        service.create(new Deposit()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Deposit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            loginId: 'BBBBBB',
            depositNo: 1,
            depositBy: 'BBBBBB',
            depositDate: datePipe.transform(currentDate, DATE_FORMAT),
            medium: 'BBBBBB',
            amount: 1,
            note: 'BBBBBB',
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
            depositDate: currentDate,
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

      it('should return a list of Deposit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            loginId: 'BBBBBB',
            depositNo: 1,
            depositBy: 'BBBBBB',
            depositDate: datePipe.transform(currentDate, DATE_FORMAT),
            medium: 'BBBBBB',
            amount: 1,
            note: 'BBBBBB',
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
            depositDate: currentDate,
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

      it('should delete a Deposit', () => {
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
