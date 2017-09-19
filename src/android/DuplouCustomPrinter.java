package com.duplou.cordova.plugin.customprinter;

// CUSTOM Printer Imports
import it.custom.printer.api.android.CustomPrinter;
import it.custom.printer.api.android.CustomAndroidAPI;
import it.custom.printer.api.android.PrinterFont;

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

// Java Imports
import java.util.Date;
import java.text.SimpleDateFormat;


/*
* This class prints tickets using CUSTOM Printer Java API
*/
public class DuplouCustomPrinter extends CordovaPlugin {

    // Initializes the avaliable usb printers connected to the device
    static UsbDevice[] usbDeviceList = null;
    static CustomPrinter printer = null;

    // Propiedades de la impresion
    static JSONObject datosCompania = null;
    static JSONObject datosTicket = null;
    static JSONObject nombreZonas = null;
    static int numeroDeImpresiones = 1;
    static double ticketTotal = 0;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("print")) {
            this.print(args, callbackContext);
            return true;
        }
        return false;
    }

    // Print Function
    private void print(JSONArray args, CallbackContext callbackContext) {

        try{
            datosCompania = args.getJSONObject(0);
            datosTicket = args.getJSONObject(1);  
            nombreZonas = args.getJSONObject(2);  
            numeroDeImpresiones = args.getInt(3);
        }catch(Exception e){
            //Show Error
            callbackContext.error("Error getting list of devices : " + e.getMessage());
        }
        
        // Activity Context
        Context context = this.cordova.getActivity().getApplicationContext();

        // Creates custom font
        PrinterFont fontNormal = new PrinterFont();
        PrinterFont fontTitle = new PrinterFont();

        // Obtiene la fecha del momento
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy, kk:mm");
        String fecha = sdf.format(new Date());
        
        try {
            //Get the list of devices
            usbDeviceList = CustomAndroidAPI.EnumUsbDevices(context);                               
        }catch (Exception e){
            //Show Error
            callbackContext.error("Error getting list of devices : " + e.getMessage());
        }

        // START
        if ((usbDeviceList == null) || (usbDeviceList.length == 0)){
            //Show Error
            callbackContext.error("No connected printer found, please verify it is indeed connected");
            return;
        }else{
            
            // Creates new printer object
            try{
                printer = new CustomAndroidAPI().getPrinterDriverUSB(usbDeviceList[0], context);
            }catch(Exception e){
                callbackContext.error("Error creating object : " + e.getMessage());
                return;
            }

            // Customizes font
            try{
                // Normal
                fontNormal.setCharHeight(PrinterFont.FONT_SIZE_X1);                   // Height x1
                fontNormal.setCharWidth(PrinterFont.FONT_SIZE_X1);                    // Width x1
                fontNormal.setEmphasized(false);                                      // No Bold
                fontNormal.setItalic(false);                                          // No Italic
                fontNormal.setUnderline(false);                                       // No Underline
                fontNormal.setJustification(PrinterFont.FONT_JUSTIFICATION_CENTER);   // Center
                fontNormal.setInternationalCharSet(PrinterFont.FONT_CS_DEFAULT);      // Default International Chars

                // Title 
                fontTitle.setCharHeight(PrinterFont.FONT_SIZE_X2);                   // Height x2
                fontTitle.setCharWidth(PrinterFont.FONT_SIZE_X2);                    // Width x2
                fontTitle.setEmphasized(true);                                       // Bold
                fontTitle.setItalic(false);                                          // No Italic
                fontTitle.setUnderline(false);                                       // No Underline
                fontTitle.setJustification(PrinterFont.FONT_JUSTIFICATION_CENTER);   // Center
                fontTitle.setInternationalCharSet(PrinterFont.FONT_CS_DEFAULT);      // Default International Chars
            }catch(Exception e){
                callbackContext.error("Error creating fonts" + e.getMessage());
                return;
            }  
            
            try{
                ticketTotal = datosTicket.getDouble("price") * (double) numeroDeImpresiones;
            }catch(Exception e){
                callbackContext.error("Error printing Excep: " + e.getMessage());
                return;
            }

            // Repite el proceso de impresión las veces que el usuario lo haya colocado
            for(int i = 1; i <= numeroDeImpresiones; i++){
                // Prints the ticket
                try{
                    // Datos de la compañía
                    printer.printTextLF(datosCompania.getString("company"), fontTitle); // Nombre de la compañía
                    
                    printer.feed(1);

                    printer.printTextLF(datosCompania.getString("address"), fontNormal); // Dirección de la compañía
                    printer.printTextLF(datosCompania.getString("city")); // Ciudad de la compañía
                    printer.printTextLF("Email: " + datosCompania.getString("email")); // Ciudad de la compañía
                    printer.printTextLF("Tel: " + datosCompania.getString("tel")); // Ciudad de la compañía
                    printer.printTextLF("Piva: " + datosCompania.getString("piva")); // Piva de la compañía

                    printer.feed(1);

                    printer.printTextLF("----------------------------------------"); // Línea de división
                    printer.printTextLF("Bus: 4");
                    printer.printTextLF("Emett: 10504");
                    printer.printTextLF("Serie: 1");
                    printer.printTextLF("Big. N.: 013571");
                    printer.printTextLF("----------------------------------------"); // Línea de división

                    printer.feed(1);

                    printer.printTextLF("Data - Ora", fontTitle);
                    printer.printTextLF(fecha); // Fecha y hora

                    printer.feed(1);

                    printer.printTextLF(datosTicket.getString("description"),fontNormal); // Nombre del ticket
                    printer.printTextLF("€ " + datosTicket.getString("price"), fontTitle); // Precio de un sólo ticket
                    printer.printTextLF(i + "/" + String.valueOf(numeroDeImpresiones)); // Número de impresión
                    printer.printTextLF("€ " + datosTicket.getString("price") + " / € " + String.valueOf(ticketTotal)); // Precio unitario / Precio Total

                    printer.feed(1);

                    printer.printTextLF("TRATTA",fontNormal);
                    printer.printTextLF(nombreZonas.getString("start") + " --> " + nombreZonas.getString("stop"),fontTitle);
                    printer.printTextLF("IL PRESENTE TITOLO DI VAGGGIO E' PERSONALE, NUN CEDIBLE ED HA VALIDITA' UNA SIGNOLA CORSA",fontNormal);

                    printer.feed(5);

                    printer.cut(CustomPrinter.CUT_TOTAL);
                }catch(Exception e){
                    callbackContext.error("Error printing Excep: " + e.getMessage());
                    return;
                }
            }

            // Returns result 
            callbackContext.success("Print finished successfully");

        }   
    }
}