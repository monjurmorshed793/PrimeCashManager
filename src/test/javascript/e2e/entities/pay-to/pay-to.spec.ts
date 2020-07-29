import { browser } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PayToComponentsPage, PayToDeleteDialog, PayToUpdatePage } from './pay-to.page-object';

const expect = chai.expect;

describe('PayTo e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let payToComponentsPage: PayToComponentsPage;
  let payToUpdatePage: PayToUpdatePage;
  let payToDeleteDialog: PayToDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
  });

  it('should load PayTos', async () => {
    await navBarPage.goToEntity('pay-to');
    payToComponentsPage = new PayToComponentsPage();
    expect(await payToComponentsPage.getTitle()).to.eq('primeCashManagerApp.payTo.home.title');
  });

  it('should load create PayTo page', async () => {
    await payToComponentsPage.clickOnCreateButton();
    payToUpdatePage = new PayToUpdatePage();
    expect(await payToUpdatePage.getPageTitle()).to.eq('primeCashManagerApp.payTo.home.createOrEditLabel');
    await payToUpdatePage.cancel();
  });

  it('should create and save PayTos', async () => {
    const nbButtonsBeforeCreate = await payToComponentsPage.countDeleteButtons();

    await payToComponentsPage.clickOnCreateButton();
    await payToUpdatePage.setNameInput('name');
    await payToUpdatePage.setDescriptionInput('description');
    await payToUpdatePage.setCreatedByInput('createdBy');
    await payToUpdatePage.setCreatedOnInput('01/01/2001 02:30');
    await payToUpdatePage.setModifiedByInput('modifiedBy');
    await payToUpdatePage.setModifiedOnInput('01/01/2001 02:30');
    expect(await payToUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await payToUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');
    expect(await payToUpdatePage.getCreatedByInput()).to.eq('createdBy', 'Expected CreatedBy value to be equals to createdBy');
    expect(await payToUpdatePage.getCreatedOnInput()).to.contain(
      '01/01/2001 02:30',
      'Expected createdOn value to be equals to 01/01/2001 02:30'
    );
    expect(await payToUpdatePage.getModifiedByInput()).to.eq('modifiedBy', 'Expected ModifiedBy value to be equals to modifiedBy');
    expect(await payToUpdatePage.getModifiedOnInput()).to.contain(
      '01/01/2001 02:30',
      'Expected modifiedOn value to be equals to 01/01/2001 02:30'
    );

    await payToUpdatePage.save();
    expect(await payToUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await payToComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last PayTo', async () => {
    const nbButtonsBeforeDelete = await payToComponentsPage.countDeleteButtons();
    await payToComponentsPage.clickOnLastDeleteButton();

    payToDeleteDialog = new PayToDeleteDialog();
    await payToDeleteDialog.clickOnConfirmButton();

    expect(await payToComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
