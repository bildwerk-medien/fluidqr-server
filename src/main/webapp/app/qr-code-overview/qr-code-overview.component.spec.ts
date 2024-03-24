import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QrCodeOverviewComponent } from './qr-code-overview.component';

describe('QrCodeOverviewComponent', () => {
  let component: QrCodeOverviewComponent;
  let fixture: ComponentFixture<QrCodeOverviewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QrCodeOverviewComponent],
    });
    fixture = TestBed.createComponent(QrCodeOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
