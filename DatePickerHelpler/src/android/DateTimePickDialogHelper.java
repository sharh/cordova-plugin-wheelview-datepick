package com.bbk.plugins.datepickerhelpler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DateTimePickDialogHelper {
    
    private Activity activity;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private WheelView hour;
    private WheelView minute;
    private String initDateTime;
    private AlertDialog ad;
    private Resources resources;
    private String packageName;
    
    private final boolean cyclic = true;
    private int fontSize = 28;
    
    
    public DateTimePickDialogHelper(Activity activity, int fontSize) {
        
        this.activity = activity;
        this.fontSize = fontSize;
        this.packageName = activity.getPackageName();
        this.resources = activity.getResources();
        Calendar calendar = Calendar.getInstance();
        this.initDateTime = calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH)+1) + "月"
                +(calendar.get(Calendar.DAY_OF_MONTH) + 1)+"日 "+calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }
    
    public DateTimePickDialogHelper(Activity activity, String initDateTime) {
        
        this.activity = activity;
        this.initDateTime = initDateTime;
        this.packageName = activity.getPackageName();
        this.resources = activity.getResources();
    }
    
    public DateTimePickDialogHelper(Activity activity, String initDateTime, int fontSize) {
        
        this.activity = activity;
        this.initDateTime = initDateTime;
        this.fontSize = fontSize;
        this.packageName = activity.getPackageName();
        this.resources = activity.getResources();
    }
    
    
    public void dateTimePicKDialog(final CallbackContext callbackContext) {
        
        //get layout
        Calendar calendar = Calendar.getInstance();
        LinearLayout dateTimeLayout = (LinearLayout) activity
                .getLayoutInflater().inflate(getLayout("date_layout"), null);
        month = (WheelView) dateTimeLayout.findViewById(getId("month"));
        year = (WheelView) dateTimeLayout.findViewById(getId("year"));
        day = (WheelView) dateTimeLayout.findViewById(getId("day"));
        hour = (WheelView) dateTimeLayout.findViewById(getId("hour"));
        minute = (WheelView) dateTimeLayout.findViewById(getId("mins"));
        //set cyclic
        month.setCyclic(cyclic);
//        year.setCyclic(cyclic);
        day.setCyclic(cyclic);
        hour.setCyclic(cyclic);
        minute.setCyclic(cyclic);
        //set before change
        int startYear = calendar.get(Calendar.YEAR);
        if(initDateTime != null){
            DateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            try {
                Date date = format1.parse(initDateTime);
                calendar.setTime(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //add change listener
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month, day, hour, minute);
            }
        };
        
        OnWheelChangedListener listener2 = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays2(year, month, day, hour, minute);
            }
        };
        //set month
        int curMonth = calendar.get(Calendar.MONTH);
        String months[] = new String[] {"1月", "2月", "3月", "4月", "5月",
                "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        month.setViewAdapter(new DateArrayAdapter(activity.getApplicationContext(), months, curMonth));
        month.setCurrentItem(curMonth);
        month.addChangingListener(listener);
    
        // set year
        int curYear = calendar.get(Calendar.YEAR);
        year.setViewAdapter(new DateNumericAdapter(activity.getApplicationContext(), startYear, startYear + 1, curYear > startYear ? 1 : 0));
        year.addChangingListener(listener);
        
        //set hour
        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        hour.setViewAdapter(new DateNumericAdapter(activity.getApplicationContext(), 0, 23, curHour));
        hour.setCurrentItem(curHour);
        hour.addChangingListener(listener);
        
        //set minute
        int curMinute = calendar.get(Calendar.MINUTE);
        minute.setViewAdapter(new DateNumericAdapter(activity.getApplicationContext(), 0, 59, curMinute));
        minute.setCurrentItem(curMinute);
        minute.addChangingListener(listener);
        
        //update changes
        updateDays(year, month, day, hour, minute);
        
        //set day, after the year and month changed
        day.addChangingListener(listener2);
        day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        //build a dialog
        ad = new AlertDialog.Builder(activity)
        .setTitle(initDateTime)
        .setView(dateTimeLayout)
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, initDateTime));
            }
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, ""));
            }
        }).show();
        
        ad.setOnDismissListener(new DialogInterface.OnDismissListener() {
            
            @Override
            public void onDismiss(DialogInterface dialog) {
                
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, ""));
            }
        });
    }
    
    
    public int getId(String name) {
        
        
        return resources.getIdentifier(name, "id", packageName);
    }
    
    public int getLayout(String name) {
        
        
        return resources.getIdentifier(name, "layout", packageName);
    }
    
    public int getDrawable(String name) {
        
        
        return resources.getIdentifier(name, "drawable", packageName);
    }
    
     /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView year, WheelView month, WheelView day, WheelView hour, WheelView mins) {
        Calendar calendar = Calendar.getInstance();

        
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());
        calendar.set(Calendar.HOUR_OF_DAY, hour.getCurrentItem());
        calendar.set(Calendar.MINUTE, mins.getCurrentItem());
        //get the maxdays in a month
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //set the month range
        day.setViewAdapter(new DateNumericAdapter(activity.getApplicationContext(), 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        //if maxdays changes, check the curDay and update it
        day.setCurrentItem(curDay - 1, true);
        
        this.initDateTime = calendar.get(Calendar.YEAR) + "年" + getDoubleString(month.getCurrentItem() + 1) + "月" + getDoubleString(day.getCurrentItem() + 1) + "日 "
                + getDoubleString(hour.getCurrentItem()) +":"+ getDoubleString(mins.getCurrentItem());
        if(ad != null){
            ad.setTitle(this.initDateTime);
            
        }
    }
    
    
    void updateDays2(WheelView year, WheelView month, WheelView day, WheelView hour, WheelView mins) {
        Calendar calendar = Calendar.getInstance();
        
        
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());
        calendar.set(Calendar.MINUTE, day.getCurrentItem() + 1);
        calendar.set(Calendar.HOUR_OF_DAY, hour.getCurrentItem());
        calendar.set(Calendar.MINUTE, mins.getCurrentItem());
        
        this.initDateTime = calendar.get(Calendar.YEAR) + "年" + getDoubleString(month.getCurrentItem() + 1) + "月" + getDoubleString(day.getCurrentItem() + 1) + "日 "
                + getDoubleString(hour.getCurrentItem()) +":"+ getDoubleString(mins.getCurrentItem());
        if(ad != null){
            ad.setTitle(this.initDateTime);
            
        }
        
        
    }
    
    
    private String getDoubleString(int number){
        
        
        
        return number < 10 ? "0" + number : "" + number;
    }
    
    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(fontSize);
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
    
    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(fontSize);
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
}
