import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PayToService } from 'app/entities/pay-to/pay-to.service';
import { IPayTo, PayTo } from 'app/shared/model/pay-to.model';

describe('Service Tests', () => {
  describe('PayTo Service', () => {
    let injector: TestBed;
    let service: PayToService;
    let httpMock: HttpTestingController;
    let elemDefault: IPayTo;
    let expectedResult: IPayTo | IPayTo[] | boolean | null;
    let currentDate: Date;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PayToService);
      httpMock = injector.get(HttpTestingController);
      currentDate = new Date();

      elemDefault = new PayTo(123, 'AAAAAAA', undefined, 'AAAAAAA', currentDate, 'AAAAAAA', currentDate);
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

      it('should create a PayTo', () => {
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

        service.create(new PayTo()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PayTo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
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

      it('should return a list of PayTo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
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

      it('should delete a PayTo', () => {
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
