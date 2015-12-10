/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/

package com.bbk.plugins.push;

import io.yunba.android.manager.YunBaManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.json.JSONArray;
import org.json.JSONException;

public class YunPush extends CordovaPlugin {
    private static final String TAG = "YUNPUSH";
    @Override
    protected void pluginInitialize() {

        YunPushReceiver.mActivity = cordova.getActivity();
        YunBaManager.start(getApplicationContext());

        YunBaManager.subscribe(getApplicationContext(), new String[]{"t1"}, new IMqttActionListener() {

            @Override
            public void onSuccess(IMqttToken arg0) {
                LOG.d(TAG, "Subscribe topic succeed");
            }

            @Override
            public void onFailure(IMqttToken arg0, Throwable arg1) {
                LOG.d(TAG, "Subscribe topic failed");
            }
        });
    }
    
    
    private Context getApplicationContext() {
        
        
        return cordova.getActivity().getApplicationContext();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        LOG.d(TAG, "onDestroy");
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("setAlias")) {
            setAlias(args.getString(0), callbackContext);
            start();
        } else if (action.equals("start")) {
            start();
        } else if (action.equals("stop")) {
            stop();
        } else if (action.equals("resume")){
            resume();
        }

        return true;
    }

    @Override
    public Object onMessage(String id, Object data) {
        LOG.d(TAG, "onMessage");
        return null;
    }
    
    private void setAlias(final String alias, final CallbackContext callbackContext) {
        YunBaManager.setAlias(getApplicationContext(), alias, new IMqttActionListener() {
            
            @Override
            public void onSuccess(IMqttToken arg) {
                StringBuilder showMsg = new StringBuilder();
                showMsg.append("[Demo] setAlias alias ")
                .append(" = ").append(alias).append(" succeed");    
                callbackContext.success(showMsg.toString());
            }
            
            @Override
            public void onFailure(IMqttToken arg0, Throwable arg) {
                StringBuilder showMsg = new StringBuilder();
                showMsg.append("[Demo] setAlias alias ")
                .append(" = ").append(alias).append(" failed");
                callbackContext.success(showMsg.toString());
            }
        });
    }

    private void start(){
        YunBaManager.start(getApplicationContext());
    }

    private void stop(){
        YunBaManager.stop(getApplicationContext());
    }

    private void resume(){
        YunBaManager.resume(getApplicationContext());
    }
    
}
