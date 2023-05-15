import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ResofomFormService } from './resofom-form.service';
import { ResofomService } from '../service/resofom.service';
import { IResofom } from '../resofom.model';
import { IResolucion } from 'app/entities/resolucion/resolucion.model';
import { ResolucionService } from 'app/entities/resolucion/service/resolucion.service';
import { IGerdis } from 'app/entities/gerdis/gerdis.model';
import { GerdisService } from 'app/entities/gerdis/service/gerdis.service';

import { ResofomUpdateComponent } from './resofom-update.component';

describe('Resofom Management Update Component', () => {
  let comp: ResofomUpdateComponent;
  let fixture: ComponentFixture<ResofomUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let resofomFormService: ResofomFormService;
  let resofomService: ResofomService;
  let resolucionService: ResolucionService;
  let gerdisService: GerdisService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ResofomUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ResofomUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResofomUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    resofomFormService = TestBed.inject(ResofomFormService);
    resofomService = TestBed.inject(ResofomService);
    resolucionService = TestBed.inject(ResolucionService);
    gerdisService = TestBed.inject(GerdisService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Resolucion query and add missing value', () => {
      const resofom: IResofom = { id: 456 };
      const resolucion: IResolucion = { id: 67181 };
      resofom.resolucion = resolucion;

      const resolucionCollection: IResolucion[] = [{ id: 10243 }];
      jest.spyOn(resolucionService, 'query').mockReturnValue(of(new HttpResponse({ body: resolucionCollection })));
      const additionalResolucions = [resolucion];
      const expectedCollection: IResolucion[] = [...additionalResolucions, ...resolucionCollection];
      jest.spyOn(resolucionService, 'addResolucionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resofom });
      comp.ngOnInit();

      expect(resolucionService.query).toHaveBeenCalled();
      expect(resolucionService.addResolucionToCollectionIfMissing).toHaveBeenCalledWith(
        resolucionCollection,
        ...additionalResolucions.map(expect.objectContaining)
      );
      expect(comp.resolucionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Gerdis query and add missing value', () => {
      const resofom: IResofom = { id: 456 };
      const gerdis: IGerdis = { id: 31270 };
      resofom.gerdis = gerdis;

      const gerdisCollection: IGerdis[] = [{ id: 70943 }];
      jest.spyOn(gerdisService, 'query').mockReturnValue(of(new HttpResponse({ body: gerdisCollection })));
      const additionalGerdis = [gerdis];
      const expectedCollection: IGerdis[] = [...additionalGerdis, ...gerdisCollection];
      jest.spyOn(gerdisService, 'addGerdisToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resofom });
      comp.ngOnInit();

      expect(gerdisService.query).toHaveBeenCalled();
      expect(gerdisService.addGerdisToCollectionIfMissing).toHaveBeenCalledWith(
        gerdisCollection,
        ...additionalGerdis.map(expect.objectContaining)
      );
      expect(comp.gerdisSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const resofom: IResofom = { id: 456 };
      const resolucion: IResolucion = { id: 73737 };
      resofom.resolucion = resolucion;
      const gerdis: IGerdis = { id: 23762 };
      resofom.gerdis = gerdis;

      activatedRoute.data = of({ resofom });
      comp.ngOnInit();

      expect(comp.resolucionsSharedCollection).toContain(resolucion);
      expect(comp.gerdisSharedCollection).toContain(gerdis);
      expect(comp.resofom).toEqual(resofom);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResofom>>();
      const resofom = { id: 123 };
      jest.spyOn(resofomFormService, 'getResofom').mockReturnValue(resofom);
      jest.spyOn(resofomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resofom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resofom }));
      saveSubject.complete();

      // THEN
      expect(resofomFormService.getResofom).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(resofomService.update).toHaveBeenCalledWith(expect.objectContaining(resofom));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResofom>>();
      const resofom = { id: 123 };
      jest.spyOn(resofomFormService, 'getResofom').mockReturnValue({ id: null });
      jest.spyOn(resofomService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resofom: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resofom }));
      saveSubject.complete();

      // THEN
      expect(resofomFormService.getResofom).toHaveBeenCalled();
      expect(resofomService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResofom>>();
      const resofom = { id: 123 };
      jest.spyOn(resofomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resofom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(resofomService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareResolucion', () => {
      it('Should forward to resolucionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(resolucionService, 'compareResolucion');
        comp.compareResolucion(entity, entity2);
        expect(resolucionService.compareResolucion).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareGerdis', () => {
      it('Should forward to gerdisService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(gerdisService, 'compareGerdis');
        comp.compareGerdis(entity, entity2);
        expect(gerdisService.compareGerdis).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
