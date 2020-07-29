import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { PrimeCashManagerTestModule } from '../../../test.module';
import { ExpenseDetailComponent } from 'app/entities/expense/expense-detail.component';
import { Expense } from 'app/shared/model/expense.model';

describe('Component Tests', () => {
  describe('Expense Management Detail Component', () => {
    let comp: ExpenseDetailComponent;
    let fixture: ComponentFixture<ExpenseDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ expense: new Expense(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PrimeCashManagerTestModule],
        declarations: [ExpenseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ExpenseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExpenseDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load expense on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.expense).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
