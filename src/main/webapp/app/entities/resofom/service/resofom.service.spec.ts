import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IResofom } from '../resofom.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../resofom.test-samples';

import { ResofomService } from './resofom.service';

const requireRestSample: IResofom = {
  ...sampleWithRequiredData,
};

describe('Resofom Service', () => {
  let service: ResofomService;
  let httpMock: HttpTestingController;
  let expectedResult: IResofom | IResofom[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ResofomService);
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

    it('should create a Resofom', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const resofom = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(resofom).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Resofom', () => {
      const resofom = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(resofom).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Resofom', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Resofom', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Resofom', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addResofomToCollectionIfMissing', () => {
      it('should add a Resofom to an empty array', () => {
        const resofom: IResofom = sampleWithRequiredData;
        expectedResult = service.addResofomToCollectionIfMissing([], resofom);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resofom);
      });

      it('should not add a Resofom to an array that contains it', () => {
        const resofom: IResofom = sampleWithRequiredData;
        const resofomCollection: IResofom[] = [
          {
            ...resofom,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addResofomToCollectionIfMissing(resofomCollection, resofom);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Resofom to an array that doesn't contain it", () => {
        const resofom: IResofom = sampleWithRequiredData;
        const resofomCollection: IResofom[] = [sampleWithPartialData];
        expectedResult = service.addResofomToCollectionIfMissing(resofomCollection, resofom);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resofom);
      });

      it('should add only unique Resofom to an array', () => {
        const resofomArray: IResofom[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const resofomCollection: IResofom[] = [sampleWithRequiredData];
        expectedResult = service.addResofomToCollectionIfMissing(resofomCollection, ...resofomArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const resofom: IResofom = sampleWithRequiredData;
        const resofom2: IResofom = sampleWithPartialData;
        expectedResult = service.addResofomToCollectionIfMissing([], resofom, resofom2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resofom);
        expect(expectedResult).toContain(resofom2);
      });

      it('should accept null and undefined values', () => {
        const resofom: IResofom = sampleWithRequiredData;
        expectedResult = service.addResofomToCollectionIfMissing([], null, resofom, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resofom);
      });

      it('should return initial array if no Resofom is added', () => {
        const resofomCollection: IResofom[] = [sampleWithRequiredData];
        expectedResult = service.addResofomToCollectionIfMissing(resofomCollection, undefined, null);
        expect(expectedResult).toEqual(resofomCollection);
      });
    });

    describe('compareResofom', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareResofom(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareResofom(entity1, entity2);
        const compareResult2 = service.compareResofom(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareResofom(entity1, entity2);
        const compareResult2 = service.compareResofom(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareResofom(entity1, entity2);
        const compareResult2 = service.compareResofom(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
