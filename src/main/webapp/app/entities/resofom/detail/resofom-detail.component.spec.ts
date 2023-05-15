import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ResofomDetailComponent } from './resofom-detail.component';

describe('Resofom Management Detail Component', () => {
  let comp: ResofomDetailComponent;
  let fixture: ComponentFixture<ResofomDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ResofomDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ resofom: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ResofomDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ResofomDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load resofom on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.resofom).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
