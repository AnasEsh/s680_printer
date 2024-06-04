package com.example.s680_printer

import android.graphics.Bitmap
import android.util.Log
import com.trendit.basesdk.POSDeviceManager
import com.trendit.basesdk.device.printer.OnPrintTaskListener
import com.trendit.basesdk.device.printer.PrinterConstants
import com.trendit.basesdk.device.printer.format.BarCodeFormat
import com.trendit.basesdk.device.printer.format.BitmapFormat
import com.trendit.basesdk.device.printer.format.PrintAlign
import com.trendit.basesdk.device.printer.format.PrintBarCodeHriLocation
import com.trendit.basesdk.device.printer.format.PrintBarCodeType
import com.trendit.basesdk.device.printer.format.PrintDensity
import com.trendit.basesdk.device.printer.format.QrCodeFormat
import com.trendit.basesdk.device.printer.format.TextFormat

object PrinterService {
    lateinit var headingFormat: TextFormat;
    lateinit var leftBodyFormat: TextFormat;
    lateinit var rightBodyFormat: TextFormat;
    lateinit var centerBodyFormat: TextFormat;
    val onPrintListener=object: OnPrintTaskListener(){
        override fun onPrintResult(p0: Int) {
            when(p0){
                PrinterConstants.TASK_STATUS_ERR -> Log.e("Print result", "Error");
                PrinterConstants.TASK_STATUS_CANCEL -> Log.i("Print result", "Cancelled");
                PrinterConstants.TASK_STATUS_FAIL -> Log.e("Print result", "Failed");
                PrinterConstants.TASK_STATUS_SUCCESS -> Log.i("Print result", "Success");
                PrinterConstants.TASK_STATUS_OUT_OF_PAPER -> Log.e("Print result", "Out of paper");
                PrinterConstants.TASK_STATUS_OVER_HEAT -> Log.e("Print result", "Overheated");
                PrinterConstants.TASK_STATUS_LOW_POWER -> Log.e("Print result", "Low dev power");
                else ->Log.i("Print result","Unknown state");
            }
            Log.i("Print result int is",p0.toString());
        }
    };

    init {
        setHeadingFormat();
        setRowLeftFormat();
        setRowCenterFormat()
        setRowRightFormat();
    }

    public fun printLines(lines:List<String>):Unit{
        POSDeviceManager.getInstance().printerDevice.apply {

            this.printDensity= PrintDensity.FORMAT_DENSITY_DARKER;

            for(l in lines)
                printText(centerBodyFormat, "\n ${l} \n")
            printDottedLines(headingFormat,2);
            printText(headingFormat,"\n \n");
            startPrint(onPrintListener);
        };
    }

    public fun fromBitMap(image: Bitmap){

        POSDeviceManager.getInstance().printerDevice.apply {

            val format=BitmapFormat();
            format.align=PrintAlign.FORMAT_ALIGN_CENTER;
            format.width=(image.width*1.1).toInt();
            format.height=(image.height*1.1).toInt();
            printText( headingFormat,"\n");
            printBitmap(format,image);
            cutPaper();
            startPrint(onPrintListener);
        }

    }
    public fun printQrCode(text:String){
        POSDeviceManager.getInstance().printerDevice.apply {
            val qrFormat=QrCodeFormat();
            qrFormat.height=200;
            qrFormat.width=200;
            qrFormat.align=PrintAlign.FORMAT_ALIGN_CENTER;
            printQrCode(qrFormat,text);
            printText(defaultTextFormat,"\n\n\n");
            cutPaper();
            startPrint(onPrintListener);
        }
    }

    public fun printBarcode(productCode:String){
        POSDeviceManager.getInstance().printerDevice.apply {
            val format=BarCodeFormat();
            format.barCodeType=PrintBarCodeType.BARCODE_TYPE_CODE128;
            format.align=PrintAlign.FORMAT_ALIGN_CENTER;
            format.hriLocation=PrintBarCodeHriLocation.FORMAT_BARCODE_HRI_LOCATION_DOWN;
            format.barCodeType
            printBarCode(format,productCode);
            printText(defaultTextFormat,"\n \n \n")
            cutPaper();
            startPrint(onPrintListener);
        }
    }

    public fun tstPrinter(){
        POSDeviceManager.getInstance().printerDevice.apply {

            this.printDensity= PrintDensity.FORMAT_DENSITY_DARKER;
            printText(headingFormat,"\n\nBeyond Technology\n\n\n");

            printText(headingFormat,"\n\nPrinter Test\n\n");

            printText(headingFormat,"\n\n \n\n");
            startPrint(onPrintListener);
        };
    }
    private fun setHeadingFormat(){
        val format= TextFormat();
        format.fontSize=35;
        format.align= PrintAlign.FORMAT_ALIGN_CENTER;
        format.isBold=true;
        headingFormat=format;
    }
    private fun setRowLeftFormat(){
        val format= TextFormat();
        format.align= PrintAlign.FORMAT_ALIGN_LEFT;
        format.isBold=false;
        leftBodyFormat=format;
    }

    private fun setRowRightFormat(){
        val format= TextFormat();
        format.align= PrintAlign.FORMAT_ALIGN_RIGHT;
        format.isBold=false;
        rightBodyFormat=format;
    }
    private fun setRowCenterFormat(){
        val format= TextFormat();
        format.align= PrintAlign.FORMAT_ALIGN_RIGHT;
        format.isBold=false;
        centerBodyFormat=format;
    }

}
