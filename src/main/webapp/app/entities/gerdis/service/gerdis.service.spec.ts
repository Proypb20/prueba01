import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGerdis } from '../gerdis.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../gerdis.test-samples';

import { GerdisService } from './gerdis.service';

const requireRestSample: IGerdis = {
  ...sampleWithRequiredData,
};

describe('Gerdis Service', () => {
  let service: GerdisService;
  let httpMock: HttpTestingController;
  let expectedResult: IGerdis | IGerdis[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GerdisService);
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

    it('should create a Gerdis', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const gerdis = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(gerdis).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Gerdis', () => {
      const gerdis = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(gerdis).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Gerdis', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Gerdis', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Gerdis', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGerdisToCollectionIfMissing', () => {
      it('should add a Gerdis to an empty array', () => {
        const gerdis: IGerdis = sampleWithRequiredData;
        expectedResult = service.addGerdisToCollectionIfMissing([], gerdis);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gerdis);
      });

      it('should not add a Gerdis to an array that contains it', () => {
        const gerdis: IGerdis = sampleWithRequiredData;
        const gerdisCollection: IGerdis[] = [
          {
            ...gerdis,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGerdisToCollectionIfMissing(gerdisCollection, gerdis);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Gerdis to an array that doesn't contain it", () => {
        const gerdis: IGerdis = sampleWithRequiredData;
        const gerdisCollection: IGerdis[] = [sampleWithPartialData];
        expectedResult = service.addGerdisToCollectionIfMissing(gerdisCollection, gerdis);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gerdis);
      });

      it('should add only unique Gerdis to an array', () => {
        const gerdisArray: IGerdis[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const gerdisCollection: IGerdis[] = [sampleWithRequiredData];
        expectedResult = service.addGerdisToCollectionIfMissing(gerdisCollection, ...gerdisArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gerdis: IGerdis = sampleWithRequiredData;
        const gerdis2: IGerdis = sampleWithPartialData;
        expectedResult = service.addGerdisToCollectionIfMissing([], gerdis, gerdis2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gerdis);
        expect(expectedResult).toContain(gerdis2);
      });

      it('should accept null and undefined values', () => {
        const gerdis: IGerdis = sampleWithRequiredData;
        expectedResult = service.addGerdisToCollectionIfMissing([], null, gerdis, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gerdis);
      });

      it('should return initial array if no Gerdis is added', () => {
        const gerdisCollection: IGerdis[] = [sampleWithRequiredData];
        expectedResult = service.addGerdisToCollectionIfMissing(gerdisCollection, undefined, null);
        expect(expectedResult).toEqual(gerdisCollection);
      });
    });

    describe('compareGerdis', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGerdis(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGerdis(entity1, entity2);
        const compareResult2 = service.compareGerdis(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGerdis(entity1, entity2);
        const compareResult2 = service.compareGerdis(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGerdis(entity1, entity2);
        const compareResult2 = service.compareGerdis(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
