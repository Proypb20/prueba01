import { IResolucion } from 'app/entities/resolucion/resolucion.model';
import { IGerdis } from 'app/entities/gerdis/gerdis.model';

export interface IResofom {
  id: number;
  limite_fc?: number | null;
  limite_fom?: number | null;
  resolucion?: Pick<IResolucion, 'id' | 'resolucion'> | null;
  gerdis?: Pick<IGerdis, 'id' | 'descripcion'> | null;
}

export type NewResofom = Omit<IResofom, 'id'> & { id: null };
