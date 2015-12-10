package com.bbk.plugins.datepickerhelpler;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class DatePickerHelpler extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("datePicker")) {
            String datetime = args.getString(0);
            this.datePicker(datetime, callbackContext);
            return true;
        }
        return false;
    }

    private void datePicker(final String datetime, final CallbackContext callbackContext) {
        final CordovaInterface cordova = this.cordova;
        Runnable runnable = new Runnable() {
            public void run() {
                
//              DatePickerDialog datePickerDialog = new DatePickerDialog(webView.getContext(),  AlertDialog.THEME_HOLO_LIGHT, , year, monthOfYear, dayOfMonth)
                
                DateTimePickDialogHelper dateTimePicKDialog = new DateTimePickDialogHelper(
                        cordova.getActivity(), datetime);
                dateTimePicKDialog.dateTimePicKDialog(callbackContext);
            };
        };
        this.cordova.getActivity().runOnUiThread(runnable);
    }
}
