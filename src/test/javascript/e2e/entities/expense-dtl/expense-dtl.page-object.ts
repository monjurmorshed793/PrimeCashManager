import { element, by, ElementFinder, protractor } from 'protractor';

export class ExpenseDtlComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-expense-dtl div table .ui-button-danger'));
  title = element.all(by.css('jhi-expense-dtl div h2#page-heading span')).first();

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

export class ExpenseDtlUpdatePage {
  pageTitle = element(by.id('jhi-expense-dtl-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  quantityInput = element(by.id('field_quantity'));
  unitPriceInput = element(by.id('field_unitPrice'));
  amountInput = element(by.id('field_amount'));
  createdByInput = element(by.id('field_createdBy'));
  createdOnInput = element(by.id('field_createdOn'));
  modifiedByInput = element(by.id('field_modifiedBy'));
  modifiedOnInput = element(by.id('field_modifiedOn'));

  itemSelect = element(by.id('field_item'));
  expenseSelect = element(by.id('field_expense'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setQuantityInput(quantity: string): Promise<void> {
    await this.quantityInput.sendKeys(quantity);
  }

  async getQuantityInput(): Promise<string> {
    return await this.quantityInput.getAttribute('value');
  }

  async setUnitPriceInput(unitPrice: string): Promise<void> {
    await this.unitPriceInput.sendKeys(unitPrice);
  }

  async getUnitPriceInput(): Promise<string> {
    return await this.unitPriceInput.getAttribute('value');
  }

  async setAmountInput(amount: string): Promise<void> {
    await this.amountInput.sendKeys(amount);
  }

  async getAmountInput(): Promise<string> {
    return await this.amountInput.getAttribute('value');
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

  async itemSelectLastOption(): Promise<void> {
    await this.itemSelect.click();
    await this.itemSelect
      .all(by.tagName('.ui-dropdown-item'))
      .last()
      .click();
  }

  getItemSelect(): ElementFinder {
    return this.itemSelect;
  }

  async getItemSelectedOption(): Promise<string> {
    return await this.itemSelect.element(by.css('.ui-dropdown-label')).getText();
  }

  async expenseSelectLastOption(): Promise<void> {
    await this.expenseSelect.click();
    await this.expenseSelect
      .all(by.tagName('.ui-dropdown-item'))
      .last()
      .click();
  }

  getExpenseSelect(): ElementFinder {
    return this.expenseSelect;
  }

  async getExpenseSelectedOption(): Promise<string> {
    return await this.expenseSelect.element(by.css('.ui-dropdown-label')).getText();
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

export class ExpenseDtlDeleteDialog {
  private confirmButton = element(by.css('p-confirmdialog .ui-dialog-footer button:first-of-type'));

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
