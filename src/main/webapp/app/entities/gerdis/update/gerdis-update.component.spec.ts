import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GerdisFormService } from './gerdis-form.service';
import { GerdisService } from '../service/gerdis.service';
import { IGerdis } from '../gerdis.model';

import { GerdisUpdateComponent } from './gerdis-update.component';

describe('Gerdis Management Update Component', () => {
  let comp: GerdisUpdateComponent;
  let fixture: ComponentFixture<GerdisUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gerdisFormService: GerdisFormService;
  let gerdisService: GerdisService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GerdisUpdateComponent],
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
      .overrideTemplate(GerdisUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GerdisUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gerdisFormService = TestBed.inject(GerdisFormService);
    gerdisService = TestBed.inject(GerdisService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const gerdis: IGerdis = { id: 456 };

      activatedRoute.data = of({ gerdis });
      comp.ngOnInit();

      expect(comp.gerdis).toEqual(gerdis);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGerdis>>();
      const gerdis = { id: 123 };
      jest.spyOn(gerdisFormService, 'getGerdis').mockReturnValue(gerdis);
      jest.spyOn(gerdisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gerdis });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gerdis }));
      saveSubject.complete();

      // THEN
      expect(gerdisFormService.getGerdis).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gerdisService.update).toHaveBeenCalledWith(expect.objectContaining(gerdis));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGerdis>>();
      const gerdis = { id: 123 };
      jest.spyOn(gerdisFormService, 'getGerdis').mockReturnValue({ id: null });
      jest.spyOn(gerdisService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gerdis: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gerdis }));
      saveSubject.complete();

      // THEN
      expect(gerdisFormService.getGerdis).toHaveBeenCalled();
      expect(gerdisService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGerdis>>();
      const gerdis = { id: 123 };
      jest.spyOn(gerdisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gerdis });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gerdisService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
