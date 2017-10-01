Cordova Plugin Lockscreen [developing]
======

This is a starting point for cordova plugin using Adenda Media on ionic Apps.

## Installation
Download and import AdendaSDK at libs folder on platforms/android/ and then
```bash
cordova plugin add https://github.com/gempe/cordova-plugin-lockscreen.git --save
```

## Code Example
Sample with ionic 3
```javascript
import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

declare var AdendaCordovaPlugin;

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  constructor(public navCtrl: NavController) {
    // Initialize Adenda
    AdendaCordovaPlugin.initialize("YOUR_APP_ID", "YOUR_APP_KEY", function() {});
  }

  // Start lockscreen
  startLockscreen(): void {
    AdendaCordovaPlugin.startLockscreen(function(res){ console.log(res); });
  }
  // Stop lockscreen
  stopLockscreen(): void {
    AdendaCordovaPlugin.stopLockscreen(function(res){ console.log(res); });
  }

  //
  addImageResource(): void {
    AdendaCordovaPlugin.addImageResource("http://res.cloudinary.com/hrscywv4p/image/upload/c_limit,h_540,w_720/v8pyn3jsji4z5cgswyh9.png", "Gempe Developer", "http://gempe.com.br/", "1", "true", function(res){ console.log(res); });
  }

  //
  addCustomHtmlContent(): void {
    AdendaCordovaPlugin.addCustomHtmlContent("http://www1.cbn.com/cbnnews", "http://www1.cbn.com/cbnnews", "2", "true", function(res){ console.log(res); });
  }

  //
  addCustomVideoContent(): void {
    AdendaCordovaPlugin.addCustomVideoContent("https://youtu.be/WyMCSxty9zQ", "https://youtu.be/WyMCSxty9zQ", "3", "true", "true", function(res){ console.log(res); });
  }

}
```