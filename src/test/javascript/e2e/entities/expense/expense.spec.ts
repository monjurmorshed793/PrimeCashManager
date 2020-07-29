import { browser } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  ExpenseComponentsPage,
  /* ExpenseDeleteDialog, */
  ExpenseUpdatePage
} from './expense.page-object';

const expect = chai.expect;

describe('Expense e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let expenseComponentsPage: ExpenseComponentsPage;
  let expenseUpdatePage: ExpenseUpdatePage;
  /* let expenseDeleteDialog: ExpenseDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
  });

  it('should load Expenses', async () => {
    await navBarPage.goToEntity('expense');
    expenseComponentsPage = new ExpenseComponentsPage();
    expect(await expenseComponentsPage.getTitle()).to.eq('primeCashManagerApp.expense.home.title');
  });

  it('should load create Expense page', async () => {
    await expenseComponentsPage.clickOnCreateButton();
    expenseUpdatePage = new ExpenseUpdatePage();
    expect(await expenseUpdatePage.getPageTitle()).to.eq('primeCashManagerApp.expense.home.createOrEditLabel');
    await expenseUpdatePage.cancel();
  });

  /* it('should create and save Expenses', async () => {
        const nbButtonsBeforeCreate = await expenseComponentsPage.countDeleteButtons();

        await expenseComponentsPage.clickOnCreateButton();
        await expenseUpdatePage.setLoginIdInput('loginId');
        await expenseUpdatePage.setVoucherNoInput('9999999');
        await expenseUpdatePage.setVoucherDateInput('12/31/2000');
        await expenseUpdatePage.monthSelectLastOption();
        await expenseUpdatePage.setNotesInput('notes');
        await expenseUpdatePage.setTotalAmountInput('9999999');
        const isPostedBeforeClick = await expenseUpdatePage.isIsPostedInputSelected();
        await expenseUpdatePage.getIsPostedInput().click();
        await expenseUpdatePage.setPostDateInput('01/01/2001 02:30');
        await expenseUpdatePage.setCreatedByInput('createdBy');
        await expenseUpdatePage.setCreatedOnInput('01/01/2001 02:30');
        await expenseUpdatePage.setModifiedByInput('modifiedBy');
        await expenseUpdatePage.setModifiedOnInput('01/01/2001 02:30');
        await expenseUpdatePage.payToSelectLastOption();
        expect(await expenseUpdatePage.getLoginIdInput()).to.eq('loginId', 'Expected LoginId value to be equals to loginId');
        expect(await expenseUpdatePage.getVoucherNoInput()).to.eq('9999999', 'Expected voucherNo value to be equals to 9999999');
        expect(await expenseUpdatePage.getVoucherDateInput()).to.eq('12/31/2000', 'Expected voucherDate value to be equals to 12/31/2000');
        expect(await expenseUpdatePage.getNotesInput()).to.eq('notes', 'Expected Notes value to be equals to notes');
        expect(await expenseUpdatePage.getTotalAmountInput()).to.eq('9999999', 'Expected totalAmount value to be equals to 9999999');
        expect(await expenseUpdatePage.isIsPostedInputSelected(), 'Expected isPosted to change after click').to.eq(!isPostedBeforeClick);
        expect(await expenseUpdatePage.getPostDateInput()).to.contain('01/01/2001 02:30', 'Expected postDate value to be equals to 01/01/2001 02:30');
        expect(await expenseUpdatePage.getCreatedByInput()).to.eq('createdBy', 'Expected CreatedBy value to be equals to createdBy');
        expect(await expenseUpdatePage.getCreatedOnInput()).to.contain('01/01/2001 02:30', 'Expected createdOn value to be equals to 01/01/2001 02:30');
        expect(await expenseUpdatePage.getModifiedByInput()).to.eq('modifiedBy', 'Expected ModifiedBy value to be equals to modifiedBy');
        expect(await expenseUpdatePage.getModifiedOnInput()).to.contain('01/01/2001 02:30', 'Expected modifiedOn value to be equals to 01/01/2001 02:30');

        await expenseUpdatePage.save();
        expect(await expenseUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await expenseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Expense', async () => {
        const nbButtonsBeforeDelete = await expenseComponentsPage.countDeleteButtons();
        await expenseComponentsPage.clickOnLastDeleteButton();

        expenseDeleteDialog = new ExpenseDeleteDialog();
        await expenseDeleteDialog.clickOnConfirmButton();

        expect(await expenseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
