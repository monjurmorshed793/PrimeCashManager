import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { PrimeCashManagerTestModule } from '../../../test.module';
import { PayToDetailComponent } from 'app/entities/pay-to/pay-to-detail.component';
import { PayTo } from 'app/shared/model/pay-to.model';

describe('Component Tests', () => {
  describe('PayTo Management Detail Component', () => {
    let comp: PayToDetailComponent;
    let fixture: ComponentFixture<PayToDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ payTo: new PayTo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PrimeCashManagerTestModule],
        declarations: [PayToDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PayToDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PayToDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load payTo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.payTo).toEqual(jasmine.objectContaining({ id: 123 }));
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
