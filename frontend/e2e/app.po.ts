import {browser, by, element} from 'protractor';

export class WeazbootgradlePage {
  navigateTo() {
    return browser.get('/');
  }

  getParagraphText() {
    return element(by.css('h1')).getText();
  }
}
