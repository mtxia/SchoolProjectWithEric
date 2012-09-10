import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.*;
import java.awt.*;
import java.awt.datatransfer.*;

// a smarter robot class than robot
public class SmartRobot extends Robot{
    
    public SmartRobot() throws AWTException{
        super();
    }
    
    // left clicks at (x,y)
    public void leftClick(int x, int y){
        mouseMove(x, y);
        mousePress(InputEvent.BUTTON1_MASK);
        mouseRelease(InputEvent.BUTTON1_MASK);
    }
    
    // types text
    public void type(String text){ 
        writeToClipboard(text);
        pasteClipboard();
    }
    
    private void writeToClipboard(String s){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(s);
        clipboard.setContents(transferable, null);
    }
    
    private void pasteClipboard(){
        keyPress(KeyEvent.VK_CONTROL);
        keyPress(KeyEvent.VK_V);
        delay(50);
        keyRelease(KeyEvent.VK_V);
        keyRelease(KeyEvent.VK_CONTROL); 
    }
    
    public static void main(String[] args){
    }
}