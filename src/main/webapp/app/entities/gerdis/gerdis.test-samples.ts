import { IGerdis, NewGerdis } from './gerdis.model';

export const sampleWithRequiredData: IGerdis = {
  id: 83990,
};

export const sampleWithPartialData: IGerdis = {
  id: 48482,
  descripcion: 'Seguridada global deposit',
};

export const sampleWithFullData: IGerdis = {
  id: 7718,
  descripcion: 'Ladrillo Colegio',
};

export const sampleWithNewData: NewGerdis = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
