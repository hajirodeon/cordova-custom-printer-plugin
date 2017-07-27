var DuplouCustomPrinter = function(){};

DuplouCustomPrinter.prototype.echo = function(str){
    cordova.exec(
        function(echoValue){alert(echoValue);},     // Success Callback
        function(error){alert(error);},             // Error callback
        "DuplouCustomPrinter",                      // Java Class
        "echo",                                     // Java class method
        [str]                                      // Arguments
    );
};

/*
DuplouCustomPrinter.prototype.echo =  function(message) {
    cordova.exec(function(success){
        alert(success);
    }, function(error){
        alert(error);
    }, "DuplouCustomPrinter", "echo", [message]);
};


DuplouCustomPrinter.prototype.print =  function(text) {
    cordova.exec(function(success){
    	alert(success);
    }, function(error){
    	alert(error);
    }, "DuplouCustomPrinter", "print", [text]);
};
*/

var duplouCustomPrinter = new DuplouCustomPrinter();

module.exports = duplouCustomPrinter;
