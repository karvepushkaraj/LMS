import { TestBed } from '@angular/core/testing';

import { LibrarySectionServiceService } from './library-section-service.service';

describe('LibrarySectionServiceService', () => {
  let service: LibrarySectionServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LibrarySectionServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
