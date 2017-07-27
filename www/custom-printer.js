var CustomPrinter = function(){};

CustomPrinter.prototype.echo =  function(message) {
    cordova.exec(function(success){
    	alert(success);
    }, function(error){
    	alert(error);
    }, "CustomPrinter", "echo", [message]);
};

CustomPrinter.prototype.print =  function(text) {
    cordova.exec(function(success){
    	alert(success);
    }, function(error){
    	alert(error);
    }, "CustomPrinter", "print", [text]);
};

var customPrinter = new CustomPrinter();

module.exports = customPrinter;
