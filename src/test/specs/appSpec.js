describe('app', function () {
    var subject = require('app.js');

    it('requires ui.router', function () {
        expect(subject.requires).toContain('ui.router')
    });
});