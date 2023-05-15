import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GerdisDetailComponent } from './gerdis-detail.component';

describe('Gerdis Management Detail Component', () => {
  let comp: GerdisDetailComponent;
  let fixture: ComponentFixture<GerdisDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GerdisDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ gerdis: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GerdisDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GerdisDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load gerdis on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.gerdis).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
