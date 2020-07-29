import { browser } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  ExpenseDtlComponentsPage,
  /* ExpenseDtlDeleteDialog, */
  ExpenseDtlUpdatePage
} from './expense-dtl.page-object';

const expect = chai.expect;

describe('ExpenseDtl e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let expenseDtlComponentsPage: ExpenseDtlComponentsPage;
  let expenseDtlUpdatePage: ExpenseDtlUpdatePage;
  /* let expenseDtlDeleteDialog: ExpenseDtlDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
  });

  it('should load ExpenseDtls', async () => {
    await navBarPage.goToEntity('expense-dtl');
    expenseDtlComponentsPage = new ExpenseDtlComponentsPage();
    expect(await expenseDtlComponentsPage.getTitle()).to.eq('primeCashManagerApp.expenseDtl.home.title');
  });

  it('should load create ExpenseDtl page', async () => {
    await expenseDtlComponentsPage.clickOnCreateButton();
    expenseDtlUpdatePage = new ExpenseDtlUpdatePage();
    expect(await expenseDtlUpdatePage.getPageTitle()).to.eq('primeCashManagerApp.expenseDtl.home.createOrEditLabel');
    await expenseDtlUpdatePage.cancel();
  });

  /* it('should create and save ExpenseDtls', async () => {
        const nbButtonsBeforeCreate = await expenseDtlComponentsPage.countDeleteButtons();

        await expenseDtlComponentsPage.clickOnCreateButton();
        await expenseDtlUpdatePage.setQuantityInput('9999999');
        await expenseDtlUpdatePage.setUnitPriceInput('9999999');
        await expenseDtlUpdatePage.setAmountInput('9999999');
        await expenseDtlUpdatePage.setCreatedByInput('createdBy');
        await expenseDtlUpdatePage.setCreatedOnInput('01/01/2001 02:30');
        await expenseDtlUpdatePage.setModifiedByInput('modifiedBy');
        await expenseDtlUpdatePage.setModifiedOnInput('01/01/2001 02:30');
        await expenseDtlUpdatePage.itemSelectLastOption();
        await expenseDtlUpdatePage.expenseSelectLastOption();
        expect(await expenseDtlUpdatePage.getQuantityInput()).to.eq('9999999', 'Expected quantity value to be equals to 9999999');
        expect(await expenseDtlUpdatePage.getUnitPriceInput()).to.eq('9999999', 'Expected unitPrice value to be equals to 9999999');
        expect(await expenseDtlUpdatePage.getAmountInput()).to.eq('9999999', 'Expected amount value to be equals to 9999999');
        expect(await expenseDtlUpdatePage.getCreatedByInput()).to.eq('createdBy', 'Expected CreatedBy value to be equals to createdBy');
        expect(await expenseDtlUpdatePage.getCreatedOnInput()).to.contain('01/01/2001 02:30', 'Expected createdOn value to be equals to 01/01/2001 02:30');
        expect(await expenseDtlUpdatePage.getModifiedByInput()).to.eq('modifiedBy', 'Expected ModifiedBy value to be equals to modifiedBy');
        expect(await expenseDtlUpdatePage.getModifiedOnInput()).to.contain('01/01/2001 02:30', 'Expected modifiedOn value to be equals to 01/01/2001 02:30');

        await expenseDtlUpdatePage.save();
        expect(await expenseDtlUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await expenseDtlComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last ExpenseDtl', async () => {
        const nbButtonsBeforeDelete = await expenseDtlComponentsPage.countDeleteButtons();
        await expenseDtlComponentsPage.clickOnLastDeleteButton();

        expenseDtlDeleteDialog = new ExpenseDtlDeleteDialog();
        await expenseDtlDeleteDialog.clickOnConfirmButton();

        expect(await expenseDtlComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
