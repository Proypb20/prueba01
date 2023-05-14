import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IResolucion } from '../resolucion.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../resolucion.test-samples';

import { ResolucionService, RestResolucion } from './resolucion.service';

const requireRestSample: RestResolucion = {
  ...sampleWithRequiredData,
  fecha: sampleWithRequiredData.fecha?.format(DATE_FORMAT),
};

describe('Resolucion Service', () => {
  let service: ResolucionService;
  let httpMock: HttpTestingController;
  let expectedResult: IResolucion | IResolucion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ResolucionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Resolucion', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const resolucion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(resolucion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Resolucion', () => {
      const resolucion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(resolucion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Resolucion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Resolucion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Resolucion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addResolucionToCollectionIfMissing', () => {
      it('should add a Resolucion to an empty array', () => {
        const resolucion: IResolucion = sampleWithRequiredData;
        expectedResult = service.addResolucionToCollectionIfMissing([], resolucion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resolucion);
      });

      it('should not add a Resolucion to an array that contains it', () => {
        const resolucion: IResolucion = sampleWithRequiredData;
        const resolucionCollection: IResolucion[] = [
          {
            ...resolucion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addResolucionToCollectionIfMissing(resolucionCollection, resolucion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Resolucion to an array that doesn't contain it", () => {
        const resolucion: IResolucion = sampleWithRequiredData;
        const resolucionCollection: IResolucion[] = [sampleWithPartialData];
        expectedResult = service.addResolucionToCollectionIfMissing(resolucionCollection, resolucion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resolucion);
      });

      it('should add only unique Resolucion to an array', () => {
        const resolucionArray: IResolucion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const resolucionCollection: IResolucion[] = [sampleWithRequiredData];
        expectedResult = service.addResolucionToCollectionIfMissing(resolucionCollection, ...resolucionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const resolucion: IResolucion = sampleWithRequiredData;
        const resolucion2: IResolucion = sampleWithPartialData;
        expectedResult = service.addResolucionToCollectionIfMissing([], resolucion, resolucion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resolucion);
        expect(expectedResult).toContain(resolucion2);
      });

      it('should accept null and undefined values', () => {
        const resolucion: IResolucion = sampleWithRequiredData;
        expectedResult = service.addResolucionToCollectionIfMissing([], null, resolucion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resolucion);
      });

      it('should return initial array if no Resolucion is added', () => {
        const resolucionCollection: IResolucion[] = [sampleWithRequiredData];
        expectedResult = service.addResolucionToCollectionIfMissing(resolucionCollection, undefined, null);
        expect(expectedResult).toEqual(resolucionCollection);
      });
    });

    describe('compareResolucion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareResolucion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareResolucion(entity1, entity2);
        const compareResult2 = service.compareResolucion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareResolucion(entity1, entity2);
        const compareResult2 = service.compareResolucion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareResolucion(entity1, entity2);
        const compareResult2 = service.compareResolucion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
