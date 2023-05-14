import dayjs from 'dayjs/esm';

import { IResolucion, NewResolucion } from './resolucion.model';

export const sampleWithRequiredData: IResolucion = {
  id: 55766,
  resolucion: 'deposit',
};

export const sampleWithPartialData: IResolucion = {
  id: 70015,
  fecha: dayjs('2023-05-06'),
  expediente: 'maximizada',
  resolucion: 'synthesize neural Berkshire',
};

export const sampleWithFullData: IResolucion = {
  id: 47202,
  fecha: dayjs('2023-05-05'),
  expediente: 'red',
  resolucion: 'Bermuda Azul',
};

export const sampleWithNewData: NewResolucion = {
  resolucion: 'Teclado',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
