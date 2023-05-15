import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../resofom.test-samples';

import { ResofomFormService } from './resofom-form.service';

describe('Resofom Form Service', () => {
  let service: ResofomFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResofomFormService);
  });

  describe('Service methods', () => {
    describe('createResofomFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createResofomFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            limite_fc: expect.any(Object),
            limite_fom: expect.any(Object),
            resolucion: expect.any(Object),
            gerdis: expect.any(Object),
          })
        );
      });

      it('passing IResofom should create a new form with FormGroup', () => {
        const formGroup = service.createResofomFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            limite_fc: expect.any(Object),
            limite_fom: expect.any(Object),
            resolucion: expect.any(Object),
            gerdis: expect.any(Object),
          })
        );
      });
    });

    describe('getResofom', () => {
      it('should return NewResofom for default Resofom initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createResofomFormGroup(sampleWithNewData);

        const resofom = service.getResofom(formGroup) as any;

        expect(resofom).toMatchObject(sampleWithNewData);
      });

      it('should return NewResofom for empty Resofom initial value', () => {
        const formGroup = service.createResofomFormGroup();

        const resofom = service.getResofom(formGroup) as any;

        expect(resofom).toMatchObject({});
      });

      it('should return IResofom', () => {
        const formGroup = service.createResofomFormGroup(sampleWithRequiredData);

        const resofom = service.getResofom(formGroup) as any;

        expect(resofom).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IResofom should not enable id FormControl', () => {
        const formGroup = service.createResofomFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewResofom should disable id FormControl', () => {
        const formGroup = service.createResofomFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
