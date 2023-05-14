import dayjs from 'dayjs/esm';

export interface IResolucion {
  id: number;
  fecha?: dayjs.Dayjs | null;
  expediente?: string | null;
  resolucion?: string | null;
}

export type NewResolucion = Omit<IResolucion, 'id'> & { id: null };
