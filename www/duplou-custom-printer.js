var DuplouCustomPrinter = function(){};

// Java Class Name
var PLUGIN_NAME = "DuplouCustomPrinter";

DuplouCustomPrinter.prototype.print = function(datosCompania, datosTicket, nombreZonas, numeroDeImpresiones, successCallback, errorCallback){
    cordova.exec(
        successCallback,
        errorCallback,
        PLUGIN_NAME,
        "print", // Native Method Name
        [datosCompania, datosTicket, nombreZonas, numeroDeImpresiones] // Arguments
    );
};

var duplouCustomPrinter = new DuplouCustomPrinter();

module.exports = duplouCustomPrinter;
