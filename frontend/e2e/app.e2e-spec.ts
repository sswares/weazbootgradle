import {WeazbootgradlePage} from './app.po';

describe('weazbootgradle App', () => {
  let page: WeazbootgradlePage;

  beforeEach(() => {
    page = new WeazbootgradlePage();
  });

  it('should display message saying app works', () => {
    WeazbootgradlePage.navigateTo();
    expect(WeazbootgradlePage.getParagraphText()).toEqual('Weazbootgradle');
  });
});
