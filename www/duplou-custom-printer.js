var DuplouCustomPrinter = function(){};

// Java Class Name
var PLUGIN_NAME = "DuplouCustomPrinter";

DuplouCustomPrinter.prototype.print = function(text, successCallback, errorCallback){
    cordova.exec(
        successCallback,
        errorCallback,
        PLUGIN_NAME,
        "print", // Native Method Name
        [text] // Arguments
    );
};

var duplouCustomPrinter = new DuplouCustomPrinter();

module.exports = duplouCustomPrinter;
