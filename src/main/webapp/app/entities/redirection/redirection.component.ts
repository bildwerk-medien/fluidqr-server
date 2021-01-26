import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRedirection } from 'app/shared/model/redirection.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { RedirectionService } from './redirection.service';
import { RedirectionDeleteDialogComponent } from './redirection-delete-dialog.component';

@Component({
  selector: 'jhi-redirection',
  templateUrl: './redirection.component.html',
})
export class RedirectionComponent implements OnInit, OnDestroy {
  redirections: IRedirection[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected redirectionService: RedirectionService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.redirections = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.redirectionService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IRedirection[]>) => this.paginateRedirections(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.redirections = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInRedirections();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRedirection): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInRedirections(): void {
    this.eventSubscriber = this.eventManager.subscribe('redirectionListModification', () => this.reset());
  }

  delete(redirection: IRedirection): void {
    const modalRef = this.modalService.open(RedirectionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.redirection = redirection;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRedirections(data: IRedirection[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.redirections.push(data[i]);
      }
    }
  }
}
