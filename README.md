<!--
# license: Licensed to the Apache Software Foundation (ASF) under one
#         or more contributor license agreements.  See the NOTICE file
#         distributed with this work for additional information
#         regarding copyright ownership.  The ASF licenses this file
#         to you under the Apache License, Version 2.0 (the
#         "License"); you may not use this file except in compliance
#         with the License.  You may obtain a copy of the License at
#
#           http://www.apache.org/licenses/LICENSE-2.0
#
#         Unless required by applicable law or agreed to in writing,
#         software distributed under the License is distributed on an
#         "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#         KIND, either express or implied.  See the License for the
#         specific language governing permissions and limitations
#         under the License.
-->

## Installation

    git clone https://github.com/sharh/cordova-plugin-wheelview-datepick.git
    cordova plugin add cordova-plugin-wheelview-datepick/com.bbk.plugins.push

## Methods

- `DatePickerHelpler.datePicker(msg, success, error);`

## DatePickerHelpler.datePicker
- msg: date format string, YYYY年MM月DD日 HH:mm
- success: success callback
-    if the dialog positiveButton is clicked, return the format date string as a success call param.
-    other way, unless the plugin called is error, success callback return '' as a success call param.
- error: error callback


### Example
    
    //only the positiveButton of the dialog is clicked, msg will be a format date string: YYYY年MM月DD日 HH:mm. else msg will be a null string: ''.
    function success(msg) {
        console.log(msg);
    }
    
    DatePickerHelpler.datePicker('2015年12月8日 12:12', success, error);

    //also, you can call it this way:
    //a default msg of current time will be send, and succes and error are :
    function success(msg) {
        console.log(msg);
    }

    DatePickerHelpler.datePicker();

### Supported Platforms

- Android
