var exec = require('cordova/exec');

var PLUGIN_NAME = 'AdendaCordovaPlugin';

var AdendaCordovaPlugin = {
  echo: function(phrase, cb) {
    exec(cb, null, PLUGIN_NAME, 'echo', [phrase]);
  },
  getDate: function(cb) {
    exec(cb, null, PLUGIN_NAME, 'getDate', []);
  },
  initialize: function(app_id, app_key, cb) {
    exec(cb, null, PLUGIN_NAME, 'initialize', [app_id, app_key]);
  },
  alert: function(cb) {
    exec(cb, null, PLUGIN_NAME, 'alert', []);
  },
  startLockscreen: function(cb) {
    exec(cb, null, PLUGIN_NAME, 'startLockscreen', []);
  },
  stopLockscreen: function(cb) {
    exec(cb, null, PLUGIN_NAME, 'stopLockscreen', []);
  },
  // Setup image resource at lockscreen
  addImageResource: function(imageUrl, text, uri, identifier, GTC, cb) {
    exec(cb, null, PLUGIN_NAME, 'addImageResource', [imageUrl, text, uri, identifier, GTC]);
  },
  // Setup custom HTML content at lockscreen
  addCustomHtmlContent: function(url, uri, identifier, GTC, cb) {
    exec(cb, null, PLUGIN_NAME, 'addCustomHtmlContent', [url, uri, identifier, GTC]);
  },
  // Setup custom video content at lockscreen
  addCustomVideoContent: function(url, uri, identifier, expandOnRotation, GTC, cb) {
    exec(cb, null, PLUGIN_NAME, 'addCustomVideoContent', [url, uri, identifier, expandOnRotation, GTC]);
  }
};

module.exports = AdendaCordovaPlugin;