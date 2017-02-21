import {browser, by, element} from 'protractor';

export class WeazbootgradlePage {
  static navigateTo() {
    return browser.get('/');
  }

  static getParagraphText() {
    return element(by.css('app-root h1')).getText();
  }
}
