var exec = require('cordova/exec');

exports.coolMethod = function(arg0, success, error) {
    exec(success, error, "CustomPrinter", "coolMethod", [arg0]);
};
