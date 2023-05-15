import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IResofom, NewResofom } from '../resofom.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResofom for edit and NewResofomFormGroupInput for create.
 */
type ResofomFormGroupInput = IResofom | PartialWithRequiredKeyOf<NewResofom>;

type ResofomFormDefaults = Pick<NewResofom, 'id'>;

type ResofomFormGroupContent = {
  id: FormControl<IResofom['id'] | NewResofom['id']>;
  limite_fc: FormControl<IResofom['limite_fc']>;
  limite_fom: FormControl<IResofom['limite_fom']>;
  resolucion: FormControl<IResofom['resolucion']>;
  gerdis: FormControl<IResofom['gerdis']>;
};

export type ResofomFormGroup = FormGroup<ResofomFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResofomFormService {
  createResofomFormGroup(resofom: ResofomFormGroupInput = { id: null }): ResofomFormGroup {
    const resofomRawValue = {
      ...this.getFormDefaults(),
      ...resofom,
    };
    return new FormGroup<ResofomFormGroupContent>({
      id: new FormControl(
        { value: resofomRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      limite_fc: new FormControl(resofomRawValue.limite_fc),
      limite_fom: new FormControl(resofomRawValue.limite_fom),
      resolucion: new FormControl(resofomRawValue.resolucion, {
        validators: [Validators.required],
      }),
      gerdis: new FormControl(resofomRawValue.gerdis, {
        validators: [Validators.required],
      }),
    });
  }

  getResofom(form: ResofomFormGroup): IResofom | NewResofom {
    return form.getRawValue() as IResofom | NewResofom;
  }

  resetForm(form: ResofomFormGroup, resofom: ResofomFormGroupInput): void {
    const resofomRawValue = { ...this.getFormDefaults(), ...resofom };
    form.reset(
      {
        ...resofomRawValue,
        id: { value: resofomRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ResofomFormDefaults {
    return {
      id: null,
    };
  }
}
