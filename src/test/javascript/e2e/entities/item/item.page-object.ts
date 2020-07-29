import { element, by, ElementFinder, protractor } from 'protractor';

export class ItemComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-item div table .ui-button-danger'));
  title = element.all(by.css('jhi-item div h2#page-heading span')).first();

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

export class ItemUpdatePage {
  pageTitle = element(by.id('jhi-item-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nameInput = element(by.id('field_name'));
  descriptionInput = element(by.id('field_description'));
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

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
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

export class ItemDeleteDialog {
  private confirmButton = element(by.css('p-confirmdialog .ui-dialog-footer button:first-of-type'));

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
