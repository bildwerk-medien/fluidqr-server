import { Component } from '@angular/core';

@Component({
  standalone: true,
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
})
export class FooterComponent {
  todayNumber: number = Date.now();
}
