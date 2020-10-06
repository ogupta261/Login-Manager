import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterQrcodeComponent } from './register-qrcode.component';

describe('RegisterQrcodeComponent', () => {
  let component: RegisterQrcodeComponent;
  let fixture: ComponentFixture<RegisterQrcodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegisterQrcodeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterQrcodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
