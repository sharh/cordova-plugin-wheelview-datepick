var exec = require('cordova/exec');

// arg0: a date format string-YYYY年MM月DD日 HH:mm
exports.datePicker = function(arg0, success, error) {
	success = success || function(msg){console.log(msg)};
	error = error || function(msg){console.log(msg)};
	if(!arg0){
		var date = new Date();
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		var h = date.getHours();
		var mins = date.getMinutes();

		arg0 = y + '年' + m + '月' + d + '日 ' + h + ':' + mins;
	}
    exec(success, error, "DatePickerHelpler", "datePicker", [arg0]);
};
