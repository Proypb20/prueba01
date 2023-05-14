import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../resolucion.test-samples';

import { ResolucionFormService } from './resolucion-form.service';

describe('Resolucion Form Service', () => {
  let service: ResolucionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResolucionFormService);
  });

  describe('Service methods', () => {
    describe('createResolucionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createResolucionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fecha: expect.any(Object),
            expediente: expect.any(Object),
            resolucion: expect.any(Object),
          })
        );
      });

      it('passing IResolucion should create a new form with FormGroup', () => {
        const formGroup = service.createResolucionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fecha: expect.any(Object),
            expediente: expect.any(Object),
            resolucion: expect.any(Object),
          })
        );
      });
    });

    describe('getResolucion', () => {
      it('should return NewResolucion for default Resolucion initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createResolucionFormGroup(sampleWithNewData);

        const resolucion = service.getResolucion(formGroup) as any;

        expect(resolucion).toMatchObject(sampleWithNewData);
      });

      it('should return NewResolucion for empty Resolucion initial value', () => {
        const formGroup = service.createResolucionFormGroup();

        const resolucion = service.getResolucion(formGroup) as any;

        expect(resolucion).toMatchObject({});
      });

      it('should return IResolucion', () => {
        const formGroup = service.createResolucionFormGroup(sampleWithRequiredData);

        const resolucion = service.getResolucion(formGroup) as any;

        expect(resolucion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IResolucion should not enable id FormControl', () => {
        const formGroup = service.createResolucionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewResolucion should disable id FormControl', () => {
        const formGroup = service.createResolucionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
