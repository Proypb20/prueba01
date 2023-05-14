import dayjs from 'dayjs/esm';

export interface IResolucion {
  id: number;
  resolucion?: number | null;
  fecha?: dayjs.Dayjs | null;
  expediente?: string | null;
  resolucionb?: number | null;
}

export type NewResolucion = Omit<IResolucion, 'id'> & { id: null };
