import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IQrCode } from 'app/shared/model/qr-code.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { QrCodeService } from './qr-code.service';
import { QrCodeDeleteDialogComponent } from './qr-code-delete-dialog.component';

@Component({
  selector: 'jhi-qr-code',
  templateUrl: './qr-code.component.html',
})
export class QrCodeComponent implements OnInit, OnDestroy {
  qrCodes: IQrCode[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected qrCodeService: QrCodeService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.qrCodes = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.qrCodeService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IQrCode[]>) => this.paginateQrCodes(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.qrCodes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInQrCodes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IQrCode): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInQrCodes(): void {
    this.eventSubscriber = this.eventManager.subscribe('qrCodeListModification', () => this.reset());
  }

  delete(qrCode: IQrCode): void {
    const modalRef = this.modalService.open(QrCodeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.qrCode = qrCode;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateQrCodes(data: IQrCode[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.qrCodes.push(data[i]);
      }
    }
  }
}
