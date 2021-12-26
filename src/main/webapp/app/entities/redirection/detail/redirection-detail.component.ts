import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRedirection } from '../redirection.model';

@Component({
  selector: 'jhi-redirection-detail',
  templateUrl: './redirection-detail.component.html',
})
export class RedirectionDetailComponent implements OnInit {
  redirection: IRedirection | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ redirection }) => {
      this.redirection = redirection;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
