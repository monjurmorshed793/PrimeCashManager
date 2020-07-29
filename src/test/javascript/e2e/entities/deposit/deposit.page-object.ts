import { element, by, ElementFinder, protractor } from 'protractor';

export class DepositComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-deposit div table .ui-button-danger'));
  title = element.all(by.css('jhi-deposit div h2#page-heading span')).first();

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

export class DepositUpdatePage {
  pageTitle = element(by.id('jhi-deposit-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  loginIdInput = element(by.id('field_loginId'));
  depositNoInput = element(by.id('field_depositNo'));
  depositByInput = element(by.id('field_depositBy'));
  depositDateInput = element(by.id('field_depositDate'));
  mediumSelect = element(by.id('field_medium'));
  amountInput = element(by.id('field_amount'));
  noteInput = element(by.id('field_note'));
  isPostedInput = element(by.id('field_isPosted'));
  postDateInput = element(by.id('field_postDate'));
  createdByInput = element(by.id('field_createdBy'));
  createdOnInput = element(by.id('field_createdOn'));
  modifiedByInput = element(by.id('field_modifiedBy'));
  modifiedOnInput = element(by.id('field_modifiedOn'));

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

  async setDepositNoInput(depositNo: string): Promise<void> {
    await this.depositNoInput.sendKeys(depositNo);
  }

  async getDepositNoInput(): Promise<string> {
    return await this.depositNoInput.getAttribute('value');
  }

  async setDepositByInput(depositBy: string): Promise<void> {
    await this.depositByInput.sendKeys(depositBy);
  }

  async getDepositByInput(): Promise<string> {
    return await this.depositByInput.getAttribute('value');
  }

  async setDepositDateInput(depositDate: string): Promise<void> {
    await this.depositDateInput.element(by.css('.ui-inputtext')).sendKeys(protractor.Key.chord(protractor.Key.CONTROL, 'a'));
    await this.depositDateInput.element(by.css('.ui-inputtext')).sendKeys(depositDate);
    await this.depositDateInput.element(by.css('.ui-inputtext')).sendKeys(protractor.Key.ESCAPE);
  }

  async getDepositDateInput(): Promise<string> {
    return await this.depositDateInput.element(by.css('.ui-inputtext')).getAttribute('value');
  }

  async mediumSelectLastOption(): Promise<void> {
    await this.mediumSelect.click();
    await this.mediumSelect
      .all(by.tagName('.ui-dropdown-item'))
      .last()
      .click();
  }

  async getMediumSelect(): Promise<string> {
    return await this.mediumSelect.element(by.css('.ui-dropdown-label')).getText();
  }

  async setAmountInput(amount: string): Promise<void> {
    await this.amountInput.sendKeys(amount);
  }

  async getAmountInput(): Promise<string> {
    return await this.amountInput.getAttribute('value');
  }

  async setNoteInput(note: string): Promise<void> {
    await this.noteInput.sendKeys(note);
  }

  async getNoteInput(): Promise<string> {
    return await this.noteInput.getAttribute('value');
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

export class DepositDeleteDialog {
  private confirmButton = element(by.css('p-confirmdialog .ui-dialog-footer button:first-of-type'));

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
