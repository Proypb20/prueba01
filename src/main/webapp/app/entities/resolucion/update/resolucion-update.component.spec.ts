import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ResolucionFormService } from './resolucion-form.service';
import { ResolucionService } from '../service/resolucion.service';
import { IResolucion } from '../resolucion.model';

import { ResolucionUpdateComponent } from './resolucion-update.component';

describe('Resolucion Management Update Component', () => {
  let comp: ResolucionUpdateComponent;
  let fixture: ComponentFixture<ResolucionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let resolucionFormService: ResolucionFormService;
  let resolucionService: ResolucionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ResolucionUpdateComponent],
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
      .overrideTemplate(ResolucionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResolucionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    resolucionFormService = TestBed.inject(ResolucionFormService);
    resolucionService = TestBed.inject(ResolucionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const resolucion: IResolucion = { id: 456 };

      activatedRoute.data = of({ resolucion });
      comp.ngOnInit();

      expect(comp.resolucion).toEqual(resolucion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResolucion>>();
      const resolucion = { id: 123 };
      jest.spyOn(resolucionFormService, 'getResolucion').mockReturnValue(resolucion);
      jest.spyOn(resolucionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resolucion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resolucion }));
      saveSubject.complete();

      // THEN
      expect(resolucionFormService.getResolucion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(resolucionService.update).toHaveBeenCalledWith(expect.objectContaining(resolucion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResolucion>>();
      const resolucion = { id: 123 };
      jest.spyOn(resolucionFormService, 'getResolucion').mockReturnValue({ id: null });
      jest.spyOn(resolucionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resolucion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resolucion }));
      saveSubject.complete();

      // THEN
      expect(resolucionFormService.getResolucion).toHaveBeenCalled();
      expect(resolucionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResolucion>>();
      const resolucion = { id: 123 };
      jest.spyOn(resolucionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resolucion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(resolucionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
