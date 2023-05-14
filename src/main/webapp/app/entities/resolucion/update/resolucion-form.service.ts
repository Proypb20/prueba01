import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IResolucion, NewResolucion } from '../resolucion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResolucion for edit and NewResolucionFormGroupInput for create.
 */
type ResolucionFormGroupInput = IResolucion | PartialWithRequiredKeyOf<NewResolucion>;

type ResolucionFormDefaults = Pick<NewResolucion, 'id'>;

type ResolucionFormGroupContent = {
  id: FormControl<IResolucion['id'] | NewResolucion['id']>;
  fecha: FormControl<IResolucion['fecha']>;
  expediente: FormControl<IResolucion['expediente']>;
  resolucion: FormControl<IResolucion['resolucion']>;
};

export type ResolucionFormGroup = FormGroup<ResolucionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResolucionFormService {
  createResolucionFormGroup(resolucion: ResolucionFormGroupInput = { id: null }): ResolucionFormGroup {
    const resolucionRawValue = {
      ...this.getFormDefaults(),
      ...resolucion,
    };
    return new FormGroup<ResolucionFormGroupContent>({
      id: new FormControl(
        { value: resolucionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      fecha: new FormControl(resolucionRawValue.fecha),
      expediente: new FormControl(resolucionRawValue.expediente, {
        validators: [Validators.maxLength(10)],
      }),
      resolucion: new FormControl(resolucionRawValue.resolucion, {
        validators: [Validators.required],
      }),
    });
  }

  getResolucion(form: ResolucionFormGroup): IResolucion | NewResolucion {
    return form.getRawValue() as IResolucion | NewResolucion;
  }

  resetForm(form: ResolucionFormGroup, resolucion: ResolucionFormGroupInput): void {
    const resolucionRawValue = { ...this.getFormDefaults(), ...resolucion };
    form.reset(
      {
        ...resolucionRawValue,
        id: { value: resolucionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ResolucionFormDefaults {
    return {
      id: null,
    };
  }
}
