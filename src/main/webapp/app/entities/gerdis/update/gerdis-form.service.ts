import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGerdis, NewGerdis } from '../gerdis.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGerdis for edit and NewGerdisFormGroupInput for create.
 */
type GerdisFormGroupInput = IGerdis | PartialWithRequiredKeyOf<NewGerdis>;

type GerdisFormDefaults = Pick<NewGerdis, 'id'>;

type GerdisFormGroupContent = {
  id: FormControl<IGerdis['id'] | NewGerdis['id']>;
  descripcion: FormControl<IGerdis['descripcion']>;
};

export type GerdisFormGroup = FormGroup<GerdisFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GerdisFormService {
  createGerdisFormGroup(gerdis: GerdisFormGroupInput = { id: null }): GerdisFormGroup {
    const gerdisRawValue = {
      ...this.getFormDefaults(),
      ...gerdis,
    };
    return new FormGroup<GerdisFormGroupContent>({
      id: new FormControl(
        { value: gerdisRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      descripcion: new FormControl(gerdisRawValue.descripcion, {
        validators: [Validators.maxLength(25)],
      }),
    });
  }

  getGerdis(form: GerdisFormGroup): IGerdis | NewGerdis {
    return form.getRawValue() as IGerdis | NewGerdis;
  }

  resetForm(form: GerdisFormGroup, gerdis: GerdisFormGroupInput): void {
    const gerdisRawValue = { ...this.getFormDefaults(), ...gerdis };
    form.reset(
      {
        ...gerdisRawValue,
        id: { value: gerdisRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GerdisFormDefaults {
    return {
      id: null,
    };
  }
}
