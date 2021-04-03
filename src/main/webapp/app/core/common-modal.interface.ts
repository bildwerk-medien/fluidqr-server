import { NgForm } from '@angular/forms';

export declare interface CommonModal {
  cancel(): void;
  submit(f: NgForm): void;
}
