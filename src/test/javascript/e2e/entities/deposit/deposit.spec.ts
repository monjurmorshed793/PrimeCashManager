import { browser } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DepositComponentsPage, DepositDeleteDialog, DepositUpdatePage } from './deposit.page-object';

const expect = chai.expect;

describe('Deposit e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let depositComponentsPage: DepositComponentsPage;
  let depositUpdatePage: DepositUpdatePage;
  let depositDeleteDialog: DepositDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
  });

  it('should load Deposits', async () => {
    await navBarPage.goToEntity('deposit');
    depositComponentsPage = new DepositComponentsPage();
    expect(await depositComponentsPage.getTitle()).to.eq('primeCashManagerApp.deposit.home.title');
  });

  it('should load create Deposit page', async () => {
    await depositComponentsPage.clickOnCreateButton();
    depositUpdatePage = new DepositUpdatePage();
    expect(await depositUpdatePage.getPageTitle()).to.eq('primeCashManagerApp.deposit.home.createOrEditLabel');
    await depositUpdatePage.cancel();
  });

  it('should create and save Deposits', async () => {
    const nbButtonsBeforeCreate = await depositComponentsPage.countDeleteButtons();

    await depositComponentsPage.clickOnCreateButton();
    await depositUpdatePage.setLoginIdInput('loginId');
    await depositUpdatePage.setDepositNoInput('9999999');
    await depositUpdatePage.setDepositByInput('depositBy');
    await depositUpdatePage.setDepositDateInput('12/31/2000');
    await depositUpdatePage.mediumSelectLastOption();
    await depositUpdatePage.setAmountInput('9999999');
    await depositUpdatePage.setNoteInput('note');
    const isPostedBeforeClick = await depositUpdatePage.isIsPostedInputSelected();
    await depositUpdatePage.getIsPostedInput().click();
    await depositUpdatePage.setPostDateInput('01/01/2001 02:30');
    await depositUpdatePage.setCreatedByInput('createdBy');
    await depositUpdatePage.setCreatedOnInput('01/01/2001 02:30');
    await depositUpdatePage.setModifiedByInput('modifiedBy');
    await depositUpdatePage.setModifiedOnInput('01/01/2001 02:30');
    expect(await depositUpdatePage.getLoginIdInput()).to.eq('loginId', 'Expected LoginId value to be equals to loginId');
    expect(await depositUpdatePage.getDepositNoInput()).to.eq('9999999', 'Expected depositNo value to be equals to 9999999');
    expect(await depositUpdatePage.getDepositByInput()).to.eq('depositBy', 'Expected DepositBy value to be equals to depositBy');
    expect(await depositUpdatePage.getDepositDateInput()).to.eq('12/31/2000', 'Expected depositDate value to be equals to 12/31/2000');
    expect(await depositUpdatePage.getAmountInput()).to.eq('9999999', 'Expected amount value to be equals to 9999999');
    expect(await depositUpdatePage.getNoteInput()).to.eq('note', 'Expected Note value to be equals to note');
    expect(await depositUpdatePage.isIsPostedInputSelected(), 'Expected isPosted to change after click').to.eq(!isPostedBeforeClick);
    expect(await depositUpdatePage.getPostDateInput()).to.contain(
      '01/01/2001 02:30',
      'Expected postDate value to be equals to 01/01/2001 02:30'
    );
    expect(await depositUpdatePage.getCreatedByInput()).to.eq('createdBy', 'Expected CreatedBy value to be equals to createdBy');
    expect(await depositUpdatePage.getCreatedOnInput()).to.contain(
      '01/01/2001 02:30',
      'Expected createdOn value to be equals to 01/01/2001 02:30'
    );
    expect(await depositUpdatePage.getModifiedByInput()).to.eq('modifiedBy', 'Expected ModifiedBy value to be equals to modifiedBy');
    expect(await depositUpdatePage.getModifiedOnInput()).to.contain(
      '01/01/2001 02:30',
      'Expected modifiedOn value to be equals to 01/01/2001 02:30'
    );

    await depositUpdatePage.save();
    expect(await depositUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await depositComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Deposit', async () => {
    const nbButtonsBeforeDelete = await depositComponentsPage.countDeleteButtons();
    await depositComponentsPage.clickOnLastDeleteButton();

    depositDeleteDialog = new DepositDeleteDialog();
    await depositDeleteDialog.clickOnConfirmButton();

    expect(await depositComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
