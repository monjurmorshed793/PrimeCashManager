import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrimeCashManagerTestModule } from '../../../test.module';
import { ExpenseDtlDetailComponent } from 'app/entities/expense-dtl/expense-dtl-detail.component';
import { ExpenseDtl } from 'app/shared/model/expense-dtl.model';

describe('Component Tests', () => {
  describe('ExpenseDtl Management Detail Component', () => {
    let comp: ExpenseDtlDetailComponent;
    let fixture: ComponentFixture<ExpenseDtlDetailComponent>;
    const route = ({ data: of({ expenseDtl: new ExpenseDtl(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PrimeCashManagerTestModule],
        declarations: [ExpenseDtlDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ExpenseDtlDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExpenseDtlDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load expenseDtl on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.expenseDtl).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
