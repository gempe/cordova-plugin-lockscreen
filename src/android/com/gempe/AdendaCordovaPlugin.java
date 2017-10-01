/**
 */
package com.gempe;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.util.Log;
import android.content.Context;
import android.app.Application;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.view.View;
import android.view.MotionEvent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import java.util.Date;
import java.util.HashSet;

import sdk.adenda.lockscreen.AdendaAgent;
import sdk.adenda.modules.AdendaGlobal;
import sdk.adenda.widget.AdendaButton;
import sdk.adenda.widget.AdendaButtonCallback;

public class AdendaCordovaPlugin extends CordovaPlugin {
  private static final String TAG = "AdendaCordovaPlugin";
  private AdendaButtonCallback mAdendaCallback;
  private AdendaButton mButton;
  private static AdendaAgent.LockScreenHelper lockScreenHelper;
  
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    Log.d(TAG, "Initializing MyCordovaPlugin");
  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if(action.equals("initialize")) {
      // Initialize Adenda with app_id and app_key
      String app_id = args.getString(0);
      String app_key = args.getString(1);
      Context context = this.cordova.getActivity().getApplicationContext();
      // Set Application ID
      AdendaAgent.setAppId(context, app_id);
      // Set Application Key
      AdendaAgent.setAppKey(context, app_key);
      // Unlock mode
      AdendaAgent.setUnlockType(context, AdendaAgent.ADENDA_UNLOCK_TYPE_DEFAULT);
      AdendaAgent.setEnableAds(context, true);
      AdendaAgent.setEnableCallAds(context, true);
      AdendaAgent.setEnableRotatingContent(context, false);
    } else if(action.equals("startLockscreen")) {
      final PluginResult result = new PluginResult(PluginResult.Status.OK, "start lockscreen");
      callbackContext.sendPluginResult(result);
      this.startLockscreen();
    } else if(action.equals("stopLockscreen")) {
      final PluginResult result = new PluginResult(PluginResult.Status.OK, "stop lockscreen");
      callbackContext.sendPluginResult(result);
      this.stopLockscreen();
    } else if(action.equals("addImageResource")) {
      this.addImageResource(args.getString(0), args.getString(1), args.getString(2), args.getString(3), args.getString(4));
    } else if(action.equals("addCustomHtmlContent")) {
      this.addCustomHtmlContent(args.getString(0), args.getString(1), args.getString(2), args.getString(3));
    } else if(action.equals("addCustomVideoContent")) {
      this.addCustomVideoContent(args.getString(0), args.getString(1), args.getString(2), args.getString(3), args.getString(4));
    } else if(action.equals("alert")) {
       final PluginResult result = new PluginResult(PluginResult.Status.OK, this.alert());
      callbackContext.sendPluginResult(result);
    } else if(action.equals("getDate")) {
      // An example of returning data back to the web layer
      final PluginResult result = new PluginResult(PluginResult.Status.OK, (new Date()).toString());
      callbackContext.sendPluginResult(result);
    }
    return true;
  }


// Start lockscreen Ads
public void startLockscreen() {
    final CordovaInterface cordova = this.cordova;
    final Application app = cordova.getActivity().getApplication();
    final String package_name = app.getPackageName();
    final Resources resources = app.getResources();

    cordova.getActivity().runOnUiThread(new Runnable() {
      public void run() {

        lockScreenHelper = new AdendaAgent.LockScreenHelper (cordova.getActivity(), new AdendaButtonCallback(){
          @Override public String getUserId() { return "456789";}
          @Override public String getUserGender() { return "m";}
          @Override public String getUserDob() { return "19940113";}
          @Override public float getUserLatitude() { return 0;}
          @Override public float getUserLongitude() { return 0;}

          @Override 
          public void onPreOptIn() {
            Toast.makeText(cordova.getActivity(), "onPreOptIn()",  Toast.LENGTH_LONG).show();
          }
          @Override 
          public void onPreOptOut() {
            Toast.makeText(cordova.getActivity(), "onPreOptOut()",  Toast.LENGTH_LONG).show();
          }
          @Override 
          public void onPostOptIn() {
            Toast.makeText(cordova.getActivity(), "onPostOptIn()",  Toast.LENGTH_LONG).show();
          }
          @Override 
          public void onPostOptOut() {
            Toast.makeText(cordova.getActivity(), "onPostOptOut()",  Toast.LENGTH_LONG).show();
          }
        });
        
        lockScreenHelper.startLockscreen();
      }
    });

}

// Stop lockscreen Ads
public void stopLockscreen() {
  final CordovaInterface cordova = this.cordova;
  final Application app = cordova.getActivity().getApplication();
  final String package_name = app.getPackageName();
  final Resources resources = app.getResources();

  cordova.getActivity().runOnUiThread(new Runnable() {
    public void run() {
      if(lockScreenHelper != null)
        lockScreenHelper.stopLockscreen();
    }
  });

}

// Add Custom remote image resource
public void addImageResource(String imageUrl, String text, String uri, String identifier, String GTC) {
  AdendaAgent.addRemoteImageResource(cordova.getActivity(), imageUrl, text, uri, identifier, Boolean.parseBoolean(GTC));
}

// Add custom HTML resource
public void addCustomHtmlContent(String url, String uri, String identifier, String GTC) {
  AdendaAgent.addCustomHtmlContent(cordova.getActivity(), url, uri, identifier, Boolean.parseBoolean(GTC));
}

// Add custom video resource
public void addCustomVideoContent(String url, String uri, String identifier, String expandOnRotation, String GTC) {
  AdendaAgent.addCustomVideoContent(cordova.getActivity(), url, uri, identifier, Boolean.parseBoolean(expandOnRotation), Boolean.parseBoolean(GTC));
}

public String alert() {
  final CordovaInterface cordova = this.cordova;
  final Application app = cordova.getActivity().getApplication();
  final String package_name = app.getPackageName();
  final Resources resources = app.getResources();

  cordova.getActivity().runOnUiThread(new Runnable() {
    public void run() {
      AlertDialog.Builder dlg = new AlertDialog.Builder(cordova.getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
      int layout = resources.getIdentifier("custom_layout", "layout", package_name);

      LayoutInflater inflater = cordova.getActivity().getLayoutInflater();
      View customview = inflater.inflate(layout, null);
      dlg.setView(customview);
      dlg.create();
      dlg.show();

        // Set Adenda button
      mButton = (AdendaButton) customview.findViewById(resources.getIdentifier("lock_in_button", "id", package_name));
      mButton.setAdendaCallback(new AdendaButtonCallback(){
          @Override public String getUserId() { return "456789";}
          @Override public String getUserGender() { return "m";}
          @Override public String getUserDob() { return "19940113";}
          @Override public float getUserLatitude() { return 0;}
          @Override public float getUserLongitude() { return 0;}
          @Override 
          public void onPreOptIn() {
            Toast.makeText(cordova.getActivity(), "onPreOptIn()",  Toast.LENGTH_LONG).show();
          }
          @Override 
          public void onPreOptOut() {
            Toast.makeText(cordova.getActivity(), "onPreOptOut()",  Toast.LENGTH_LONG).show();
          }
          @Override 
          public void onPostOptIn() {
            Toast.makeText(cordova.getActivity(), "onPostOptIn()",  Toast.LENGTH_LONG).show();
          }
          @Override 
          public void onPostOptOut() {
            Toast.makeText(cordova.getActivity(), "onPostOptOut()",  Toast.LENGTH_LONG).show();
          }
      });

      AdendaAgent.LockScreenHelper lockScreenHelper = new AdendaAgent.LockScreenHelper (cordova.getActivity(), new AdendaButtonCallback(){
              @Override public String getUserId() { return "456789";}
              @Override public String getUserGender() { return "m";}
              @Override public String getUserDob() { return "19940113";}
              @Override public float getUserLatitude() { return 0;}
              @Override public float getUserLongitude() { return 0;}

              @Override 
              public void onPreOptIn() {
                Toast.makeText(cordova.getActivity(), "onPreOptIn()",  Toast.LENGTH_LONG).show();
              }
              @Override 
              public void onPreOptOut() {
                Toast.makeText(cordova.getActivity(), "onPreOptOut()",  Toast.LENGTH_LONG).show();
              }
              @Override 
              public void onPostOptIn() {
                Toast.makeText(cordova.getActivity(), "onPostOptIn()",  Toast.LENGTH_LONG).show();
              }
              @Override 
              public void onPostOptOut() {
                Toast.makeText(cordova.getActivity(), "onPostOptOut()",  Toast.LENGTH_LONG).show();
              }
        });
        lockScreenHelper.startLockscreen();
    }
  });

  int l1 = resources.getIdentifier("custom_layout", "layout", package_name);
  int l2 = resources.getIdentifier("lock_in_button", "id", package_name);
  return (package_name+"l1->"+l1+"l2->"+l2);

}

}
