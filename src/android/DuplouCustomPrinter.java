package com.duplou.cordova.plugin.customprinter;

// CUSTOM Printer Imports
import it.custom.printer.api.android.CustomPrinter;
import it.custom.printer.api.android.CustomAndroidAPI;
import it.custom.printer.api.android.PrinterFont;
import it.custom.printer.api.android.CustomException;

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


/**
* This class prints tickets using CUSTOM Printer Java API
*/
public class DuplouCustomPrinter extends CordovaPlugin {

    // Initializes the avaliable usb printers connected to the device
    static UsbDevice[] usbDeviceList = null;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("print")) {
            String text = args.getString(0);
            this.print(text, callbackContext);
            return true;
        }
        return false;
    }

    // Print Function
    private void print(String text, CallbackContext callbackContext) {
        // Checks if the string is empty
        if (text != null && text.length() > 0) {
            try {
                // Activity Context
                Context context = this.cordova.getActivity().getApplicationContext();
                //Get the list of devices
                usbDeviceList = CustomAndroidAPI.EnumUsbDevices(context);

                if ((usbDeviceList == null) || (usbDeviceList.length == 0)){
                    //Show Error
                    callbackContext.error("No connected printer found, please verify it is indeed connected");
                    return;
                } else {
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
}