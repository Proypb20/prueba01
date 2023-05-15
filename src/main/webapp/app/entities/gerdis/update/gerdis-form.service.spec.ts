import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../gerdis.test-samples';

import { GerdisFormService } from './gerdis-form.service';

describe('Gerdis Form Service', () => {
  let service: GerdisFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GerdisFormService);
  });

  describe('Service methods', () => {
    describe('createGerdisFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGerdisFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descripcion: expect.any(Object),
          })
        );
      });

      it('passing IGerdis should create a new form with FormGroup', () => {
        const formGroup = service.createGerdisFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descripcion: expect.any(Object),
          })
        );
      });
    });

    describe('getGerdis', () => {
      it('should return NewGerdis for default Gerdis initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createGerdisFormGroup(sampleWithNewData);

        const gerdis = service.getGerdis(formGroup) as any;

        expect(gerdis).toMatchObject(sampleWithNewData);
      });

      it('should return NewGerdis for empty Gerdis initial value', () => {
        const formGroup = service.createGerdisFormGroup();

        const gerdis = service.getGerdis(formGroup) as any;

        expect(gerdis).toMatchObject({});
      });

      it('should return IGerdis', () => {
        const formGroup = service.createGerdisFormGroup(sampleWithRequiredData);

        const gerdis = service.getGerdis(formGroup) as any;

        expect(gerdis).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGerdis should not enable id FormControl', () => {
        const formGroup = service.createGerdisFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGerdis should disable id FormControl', () => {
        const formGroup = service.createGerdisFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
