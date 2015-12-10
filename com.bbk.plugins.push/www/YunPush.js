var exec = require('cordova/exec');

exports.setAlias = function(arg0, success, error) {
    exec(success, error, "YunPush", "setAlias", [arg0]);
};

exports.start = function(arg0, success, error) {
    exec(success, error, "YunPush", "start", [arg0]);
};

exports.stop = function(arg0, success, error) {
    exec(success, error, "YunPush", "stop", [arg0]);
};

exports.resume = function(arg0, success, error) {
    exec(success, error, "YunPush", "resume", [arg0]);
};