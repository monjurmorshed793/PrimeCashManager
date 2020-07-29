import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PrimeCashManagerTestModule } from '../../../test.module';
import { ExpenseDtlUpdateComponent } from 'app/entities/expense-dtl/expense-dtl-update.component';
import { ExpenseDtlService } from 'app/entities/expense-dtl/expense-dtl.service';
import { ExpenseDtl } from 'app/shared/model/expense-dtl.model';

describe('Component Tests', () => {
  describe('ExpenseDtl Management Update Component', () => {
    let comp: ExpenseDtlUpdateComponent;
    let fixture: ComponentFixture<ExpenseDtlUpdateComponent>;
    let service: ExpenseDtlService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PrimeCashManagerTestModule],
        declarations: [ExpenseDtlUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ExpenseDtlUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExpenseDtlUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExpenseDtlService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ExpenseDtl(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(comp.editForm.value);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = null;
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(comp.editForm.value);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
