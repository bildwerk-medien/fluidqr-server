import { browser, element, by, ExpectedConditions as ec } from 'protractor';

import { NavBarPage, SignInPage, PasswordPage, SettingsPage } from '../page-objects/jhi-page-objects';

const expect = chai.expect

describe('account', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';
  let passwordPage: PasswordPage;
  let settingsPage: SettingsPage;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage(true);
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
  });

  it('should fail to login with bad password', async () => {
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, 'foo');

    const expect2 = 'login.messages.error.authentication';
    const value2 = await element(by.css('.alert-danger')).getAttribute('jhiTranslate');
    expect(value2).to.eq(expect2);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
