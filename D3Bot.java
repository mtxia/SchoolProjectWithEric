import java.awt.Robot;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;

import java.io.File;
import java.util.Date;
import jxl.*;
import jxl.read.biff.BiffException;
import java.io.IOException;

import javax.imageio.ImageIO;


/* usage:
 javac -classpath jxl.jar MyClass.java
 java -classpath jxl.jar;. MyClass
 */
public class D3Bot{
    
    // coordinates of "Unique Item Name" box in auction house
    private static final int AH_ITEM_NAME_X_LOC = 740;
    private static final int AH_ITEM_NAME_Y_LOC = 416;
    // coordinates of "Search" button in auction house
    private static final int AH_SEARCH_X_LOC = 733;
    private static final int AH_SEARCH_Y_LOC = 683;
    // coordinates of first item in autofill
    private static final int AH_SELECT_FIRST_ITEM_X_LOC = 656;
    private static final int AH_SELECT_FIRST_ITEM_Y_LOC = 472;
    // coordinates of USD/Gold button on AH
    private static final int AH_USD_X_LOC = 1456;
    private static final int AH_USD_Y_LOC = 121;
    // coordinates of OK button when you switch currency
    private static final int AH_CURRENCY_SWITCH_OK_X_LOC = 1055;
    private static final int AH_CURRENCY_SWITCH_OK_Y_LOC = 386;
    // coordinates of Buyout button 
    private static final int AH_BUYOUT_X_LOC = 1421;
    private static final int AH_BUYOUT_Y_LOC = 255;
    // coordinates of Equipment button
    private static final int AH_EQUIPMENT_X_LOC = 796;
    private static final int AH_EQUIPMENT_Y_LOC = 220;
    
    public static void beginAuctionArbitrageBot(SmartRobot r){
        try{
            String wbFile = ".\\D3AHBotData.xls";    // excel file
            String ws = "GAH vs RMAH gaps";          // relevant sheet
            File f = new File(wbFile);
            Workbook wb = Workbook.getWorkbook(f);
            Sheet arb = wb.getSheet(ws);             //arbitrage worksheet
            
            int itemNameCol = 0; // column of item names
            int numItems = 64; // number of items (rows) to search
            boolean readingDollar = true;
            
            // record RMAH and Gold Prices
            do{
                for (int row = 1; row < numItems + 1; row++){
                    String itemName = arb.getCell(itemNameCol, row).getContents();  // item name
                    System.out.println("Searching item in row " + row + "\t" + itemName);
                    
                    // click on search input box
                    r.leftClick(D3Bot.AH_ITEM_NAME_X_LOC, D3Bot.AH_ITEM_NAME_Y_LOC);
                    r.delay(1000);
                    
                    // ctrl + a to replace previous item name
                    r.keyPress(KeyEvent.VK_CONTROL);
                    r.keyPress(KeyEvent.VK_A);
                    r.delay(50);
                    r.keyRelease(KeyEvent.VK_V);
                    r.keyRelease(KeyEvent.VK_CONTROL);
                    
                    // type item name and select first one from autofill
                    r.type(itemName);
                    r.delay(1000);
                    r.leftClick(D3Bot.AH_SELECT_FIRST_ITEM_X_LOC, D3Bot.AH_SELECT_FIRST_ITEM_Y_LOC);
                    r.delay(500);
                    
                    // click search
                    r.leftClick(D3Bot.AH_SEARCH_X_LOC, D3Bot.AH_SEARCH_Y_LOC);
                    r.delay(1300);
                    
                    // capture screen to OCR price
                    int topLeftPointX = 866;
                    int topLeftPointY = 262;
                    int width = 586;
                    int height = 100;
                    /*// capture screen for $
                     if (readingDollar){
                     topLeftPointX = 1398;
                     topLeftPointY = 275;
                     width = 40;
                     height = 13;
                     }
                     // capture screen for gold
                     else{
                     width = 10;
                     }*/
                    BufferedImage image = r.createScreenCapture(new Rectangle(topLeftPointX, topLeftPointY, width, height));
                    String currency = readingDollar ? "Dollar" : "Gold";
                    File output = new File(itemName + "_" + currency + ".png");
                    ImageIO.write(image, "png", output);
                    output = null;
                    /*OCRFacade facade=new OCRFacade();
                     try {
                     String text=facade.recognizeFile(itemName + ".png", "eng");
                     
                     System.out.println("Text in the image is: ");
                     System.out.println(text);
                     
                     } catch (Exception e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                     }*/
                    
                    // wait a bit until next search
                    r.delay(2200);
                }
                                
                // switch to gold
                r.leftClick(AH_USD_X_LOC, AH_USD_Y_LOC);
                r.delay(1000);
                r.leftClick(AH_CURRENCY_SWITCH_OK_X_LOC, AH_CURRENCY_SWITCH_OK_Y_LOC);
                r.delay(800);
                //r.leftClick(AH_BUYOUT_X_LOC, AH_BUYOUT_Y_LOC);
                //r.delay(1100);                
                r.leftClick(AH_EQUIPMENT_X_LOC, AH_EQUIPMENT_Y_LOC);
                r.delay(900);
                readingDollar = !readingDollar;
            } while (!readingDollar);     
        } catch (IOException e){
            System.out.println(e);
        } catch (BiffException b){
            System.out.println(b);
        }
    }
    
    public static void main(String[] args){
        try{
            SmartRobot r = new SmartRobot();
            D3Bot.beginAuctionArbitrageBot(r);
        }
        catch (AWTException a){
        }
    }
}