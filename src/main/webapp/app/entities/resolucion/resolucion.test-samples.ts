import dayjs from 'dayjs/esm';

import { IResolucion, NewResolucion } from './resolucion.model';

export const sampleWithRequiredData: IResolucion = {
  id: 55766,
  resolucion: 8801,
};

export const sampleWithPartialData: IResolucion = {
  id: 92408,
  resolucion: 66233,
  fecha: dayjs('2023-05-05'),
  resolucionb: 4273,
};

export const sampleWithFullData: IResolucion = {
  id: 3850,
  resolucion: 28854,
  fecha: dayjs('2023-05-05'),
  expediente: 'Consultor ',
  resolucionb: 45646,
};

export const sampleWithNewData: NewResolucion = {
  resolucion: 47202,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
