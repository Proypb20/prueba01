import { IResofom, NewResofom } from './resofom.model';

export const sampleWithRequiredData: IResofom = {
  id: 18568,
};

export const sampleWithPartialData: IResofom = {
  id: 73356,
  limite_fom: 70504,
};

export const sampleWithFullData: IResofom = {
  id: 97546,
  limite_fc: 23708,
  limite_fom: 57428,
};

export const sampleWithNewData: NewResofom = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
