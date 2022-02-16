import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGoogleUser } from '../google-user.model';

@Component({
  selector: 'jhi-google-user-detail',
  templateUrl: './google-user-detail.component.html',
})
export class GoogleUserDetailComponent implements OnInit {
  googleUser: IGoogleUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ googleUser }) => {
      this.googleUser = googleUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
