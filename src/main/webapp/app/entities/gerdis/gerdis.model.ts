export interface IGerdis {
  id: number;
  descripcion?: string | null;
}

export type NewGerdis = Omit<IGerdis, 'id'> & { id: null };
