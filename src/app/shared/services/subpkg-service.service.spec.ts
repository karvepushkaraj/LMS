import { TestBed } from '@angular/core/testing';

import { SubpkgServiceService } from './subpkg-service.service';

describe('SubpkgServiceService', () => {
  let service: SubpkgServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubpkgServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
