import { element, by, ElementFinder, protractor } from 'protractor';

export class ExpenseComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-expense div table .ui-button-danger'));
  title = element.all(by.css('jhi-expense div h2#page-heading span')).first();

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class ExpenseUpdatePage {
  pageTitle = element(by.id('jhi-expense-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  loginIdInput = element(by.id('field_loginId'));
  voucherNoInput = element(by.id('field_voucherNo'));
  voucherDateInput = element(by.id('field_voucherDate'));
  monthSelect = element(by.id('field_month'));
  notesInput = element(by.id('field_notes'));
  totalAmountInput = element(by.id('field_totalAmount'));
  isPostedInput = element(by.id('field_isPosted'));
  postDateInput = element(by.id('field_postDate'));
  createdByInput = element(by.id('field_createdBy'));
  createdOnInput = element(by.id('field_createdOn'));
  modifiedByInput = element(by.id('field_modifiedBy'));
  modifiedOnInput = element(by.id('field_modifiedOn'));

  payToSelect = element(by.id('field_payTo'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setLoginIdInput(loginId: string): Promise<void> {
    await this.loginIdInput.sendKeys(loginId);
  }

  async getLoginIdInput(): Promise<string> {
    return await this.loginIdInput.getAttribute('value');
  }

  async setVoucherNoInput(voucherNo: string): Promise<void> {
    await this.voucherNoInput.sendKeys(voucherNo);
  }

  async getVoucherNoInput(): Promise<string> {
    return await this.voucherNoInput.getAttribute('value');
  }

  async setVoucherDateInput(voucherDate: string): Promise<void> {
    await this.voucherDateInput.element(by.css('.ui-inputtext')).sendKeys(protractor.Key.chord(protractor.Key.CONTROL, 'a'));
    await this.voucherDateInput.element(by.css('.ui-inputtext')).sendKeys(voucherDate);
    await this.voucherDateInput.element(by.css('.ui-inputtext')).sendKeys(protractor.Key.ESCAPE);
  }

  async getVoucherDateInput(): Promise<string> {
    return await this.voucherDateInput.element(by.css('.ui-inputtext')).getAttribute('value');
  }

  async monthSelectLastOption(): Promise<void> {
    await this.monthSelect.click();
    await this.monthSelect
      .all(by.tagName('.ui-dropdown-item'))
      .last()
      .click();
  }

  async getMonthSelect(): Promise<string> {
    return await this.monthSelect.element(by.css('.ui-dropdown-label')).getText();
  }

  async setNotesInput(notes: string): Promise<void> {
    await this.notesInput.sendKeys(notes);
  }

  async getNotesInput(): Promise<string> {
    return await this.notesInput.getAttribute('value');
  }

  async setTotalAmountInput(totalAmount: string): Promise<void> {
    await this.totalAmountInput.sendKeys(totalAmount);
  }

  async getTotalAmountInput(): Promise<string> {
    return await this.totalAmountInput.getAttribute('value');
  }

  getIsPostedInput(): ElementFinder {
    return this.isPostedInput;
  }

  isIsPostedInputSelected(): Promise<boolean> {
    return this.getIsPostedInput()
      .element(by.css('input[type="checkbox"]'))
      .isSelected() as Promise<boolean>;
  }

  async setPostDateInput(postDate: string): Promise<void> {
    await this.postDateInput.element(by.css('.ui-inputtext')).sendKeys(protractor.Key.chord(protractor.Key.CONTROL, 'a'));
    await this.postDateInput.element(by.css('.ui-inputtext')).sendKeys(postDate);
    await this.postDateInput.element(by.css('.ui-inputtext')).sendKeys(protractor.Key.ESCAPE);
  }

  async getPostDateInput(): Promise<string> {
    return await this.postDateInput.element(by.css('.ui-inputtext')).getAttribute('value');
  }

  async setCreatedByInput(createdBy: string): Promise<void> {
    await this.createdByInput.sendKeys(createdBy);
  }

  async getCreatedByInput(): Promise<string> {
    return await this.createdByInput.getAttribute('value');
  }

  async setCreatedOnInput(createdOn: string): Promise<void> {
    await this.createdOnInput.element(by.css('.ui-inputtext')).sendKeys(protractor.Key.chord(protractor.Key.CONTROL, 'a'));
    await this.createdOnInput.element(by.css('.ui-inputtext')).sendKeys(createdOn);
    await this.createdOnInput.element(by.css('.ui-inputtext')).sendKeys(protractor.Key.ESCAPE);
  }

  async getCreatedOnInput(): Promise<string> {
    return await this.createdOnInput.element(by.css('.ui-inputtext')).getAttribute('value');
  }

  async setModifiedByInput(modifiedBy: string): Promise<void> {
    await this.modifiedByInput.sendKeys(modifiedBy);
  }

  async getModifiedByInput(): Promise<string> {
    return await this.modifiedByInput.getAttribute('value');
  }

  async setModifiedOnInput(modifiedOn: string): Promise<void> {
    await this.modifiedOnInput.element(by.css('.ui-inputtext')).sendKeys(protractor.Key.chord(protractor.Key.CONTROL, 'a'));
    await this.modifiedOnInput.element(by.css('.ui-inputtext')).sendKeys(modifiedOn);
    await this.modifiedOnInput.element(by.css('.ui-inputtext')).sendKeys(protractor.Key.ESCAPE);
  }

  async getModifiedOnInput(): Promise<string> {
    return await this.modifiedOnInput.element(by.css('.ui-inputtext')).getAttribute('value');
  }

  async payToSelectLastOption(): Promise<void> {
    await this.payToSelect.click();
    await this.payToSelect
      .all(by.tagName('.ui-dropdown-item'))
      .last()
      .click();
  }

  getPayToSelect(): ElementFinder {
    return this.payToSelect;
  }

  async getPayToSelectedOption(): Promise<string> {
    return await this.payToSelect.element(by.css('.ui-dropdown-label')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class ExpenseDeleteDialog {
  private confirmButton = element(by.css('p-confirmdialog .ui-dialog-footer button:first-of-type'));

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
