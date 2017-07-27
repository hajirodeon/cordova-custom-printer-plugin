package com.duplou.cordova.plugin.customprinter;

// CUSTOM Printer Imports
/*
import it.custom.printer.api.android.CustomPrinter;
import it.custom.printer.api.android.CustomAndroidAPI;
import it.custom.printer.api.android.PrinterFont;
import it.custom.printer.api.android.CustomException;
*/

// Cordova Imports
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

// JSON Imports 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Android Imports
import android.hardware.usb.UsbDevice;
import android.content.Context;
import android.util.Log;


/**
* This class echoes a string called from JavaScript.
*/
public class DuplouCustomPrinter extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("echo")) {
            String message = args.getString(0);
            this.echo(message, callbackContext);
            return true;
        }
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    /*

    // Initializes the avaliable usb printers connected to the device
    static UsbDevice[] usbDeviceList = null;

    // Activity Context
    private Context context = this.cordova.getActivity().getApplicationContext(); 

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        
        if (action.equals("echo")) {
            String message = args.getString(0);
            this.echo(message, callbackContext);
            return true;
        }

        if (action.equals("print")){
            String text = args.getString(0);
            this.print(text, callbackContext);
            return true;
        }

        return false;
    }

    // Test Function
    private void echo(String message, CallbackContext callbackContext) {
        Log.d("ECHO","Entro la funcion");
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument");
        }
    }

    // Print Function
    private void print(String text, CallbackContext callbackContext){
        Log.d("PRINT","Entro la funcion");
        if (text != null && text.length() > 0) {
            // Checks the connected usb printers
            try {
                //Get the list of devices
                usbDeviceList = CustomAndroidAPI.EnumUsbDevices(context);

                if ((usbDeviceList == null) || (usbDeviceList.length == 0)){
                    //Show Error
                    callbackContext.error("No connected printer found, please verify it is indeed connected");
                    return;
                }else{
                    // Creates new printer object
                    CustomPrinter printer = new CustomAndroidAPI().getPrinterDriverUSB(usbDeviceList[0],context);

                    // Creates new font object
                    PrinterFont font = new PrinterFont();
                    //Fill class: NORMAL
                    font.setCharHeight(PrinterFont.FONT_SIZE_X1);                   //Height x1
                    font.setCharWidth(PrinterFont.FONT_SIZE_X1);                    //Width x1
                    font.setEmphasized(false);                                      //No Bold
                    font.setItalic(false);                                          //No Italic
                    font.setUnderline(false);                                       //No Underline
                    font.setJustification(PrinterFont.FONT_JUSTIFICATION_CENTER);   //Center
                    font.setInternationalCharSet(PrinterFont.FONT_CS_DEFAULT);      //Default International Chars

                    // Prints the ticket
                    printer.printText(text, font);

                    // Close connection with printer
                    printer.close();

                    // Returns result 
                    callbackContext.success("Print finished successfully");
                }                                   
            }catch(CustomException e){
                //Show Error
                callbackContext.error(e.getMessage());
                return;
            }catch (Exception e){
                //Show Error
                callbackContext.error(e.getMessage());
                return;
            }
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
    */
}