import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ResolucionDetailComponent } from './resolucion-detail.component';

describe('Resolucion Management Detail Component', () => {
  let comp: ResolucionDetailComponent;
  let fixture: ComponentFixture<ResolucionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ResolucionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ resolucion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ResolucionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ResolucionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load resolucion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.resolucion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
