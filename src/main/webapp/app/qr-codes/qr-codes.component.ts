import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-qr-codes',
  templateUrl: './qr-codes.component.html',
  styleUrls: ['qr-codes.component.scss'],
})
export class QrCodesComponent implements OnInit {
  message: string;

  constructor() {
    this.message = 'QrCodesComponent message';
  }

  ngOnInit(): void {}
}
