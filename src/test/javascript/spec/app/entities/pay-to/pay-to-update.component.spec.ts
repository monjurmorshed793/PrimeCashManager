import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PrimeCashManagerTestModule } from '../../../test.module';
import { PayToUpdateComponent } from 'app/entities/pay-to/pay-to-update.component';
import { PayToService } from 'app/entities/pay-to/pay-to.service';
import { PayTo } from 'app/shared/model/pay-to.model';

describe('Component Tests', () => {
  describe('PayTo Management Update Component', () => {
    let comp: PayToUpdateComponent;
    let fixture: ComponentFixture<PayToUpdateComponent>;
    let service: PayToService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PrimeCashManagerTestModule],
        declarations: [PayToUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PayToUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PayToUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PayToService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PayTo(123);
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
